#!/bin/sh
#_(
    DEPS='
    {:mvn/repos
     {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
     :deps
     {origami/origami        {:mvn/version "4.9.0-8"}
      origami/filters        {:mvn/version "1.48"}
      ; Clojure 1.11 redefines some macros so back to 1.10
      ; https://clojureverse.org/t/tools-deps-test-runner-warnings/9548
      org.clojure/clojure    {:mvn/version "1.10.1"}
      org.clojure/core.async {:mvn/version "1.6.681"}
      hellonico/morse        {:mvn/version "0.4.3"}
      org.clojure/tools.cli  {:mvn/version "1.1.230"}}}
    '

    exec clj -Sdeps "$DEPS" -M "$0" "$@"
    )

; https://github.com/hellonico/morse/tree/master

(def base-url "https://api.telegram.org/bot")

(def token (System/getenv "TELEGRAM_TOKEN"))

(require
  '[morse.handlers :as h]
  '[morse.api :as t]
  '[clj-http.client :as http]
  '[morse.polling :as p]
  '[opencv4.filter :as f]
  '[clojure.java.io :as io]
  '[opencv4.utils :as u]
  '[opencv4.core :refer [imread clone] :as cv])

; (defn apply-cv [mat]
;   (-> mat
;       (cv/cvt-color! cv/COLOR_RGB2GRAY)
;       (cv/canny! 300.0 100.0 3 true)
;       (cv/bitwise-not!)))

(def current-filter (atom (origami.filters.NoOPFilter.)))

(defn mat-to-array [src]
  (let [matOfBytes (org.opencv.core.MatOfByte.)]
    (org.opencv.imgcodecs.Imgcodecs/imencode ".png" src matOfBytes)
    (.toArray matOfBytes)))

(defn send-mat [token chat-id options mat filename]
  "Send a BufferedImage as multipart-encoded."
  (let [
        url          (str base-url token "/sendPhoto")
        byte-array   (mat-to-array mat)
        base-form    [{:part-name "chat_id" :content (str chat-id)}
                      {:part-name "photo" :content byte-array :name filename :mime-type "image/png"}]
        options-form (for [[key value] options]
                       {:part-name (name key) :content value})
        form         (into base-form options-form)
        resp         (http/post url {:as :json :multipart form})]
    (-> resp :body)))

(defn get-file [response]
  (str "https://api.telegram.org/file/bot" token "/" (-> response :result :file_path)))

(defn handle-photo [message]
  (try
    (let [fid (-> message :photo last :file_id) filename (str "t/" fid ".png") response (t/get-file token fid) mat (-> response get-file (u/mat-from-url) (@current-filter))]
      ; TODO could use a buffer or last frame for reuse
      ; (cv/imwrite mat filename)

      ; old code
      ; (io/copy (io/input-stream (get-file response)) (io/file filename))
      ; (cv/imwrite (@current-filter (cv/imread filename)) filename)
      (send-mat token (-> message :chat :id) {} mat filename))
    (catch Exception e (.printStackTrace e))))

(defn handle-filter [message]
  (let [filter (:text message)]
    (try
        (reset! current-filter (f/s->fn-filter filter))
        (t/send-text token (-> message :chat :id) (str "Filter updated:" filter))
      (catch Exception e
        (t/send-text token (-> message :chat :id) (str "Filter update failed:" e))))))

(h/defhandler handler
              (h/command-fn "snap" (fn [{{id :id :as chat} :chat}]
                                      (send-mat token id {} (@current-filter (u/<cam {:device 0})) "snap.png")
                                      (t/send-text token id (str "Taken at:" (java.time.LocalDateTime/now) ))))

              (h/command-fn "start" (fn [{{id :id :as chat} :chat}]
                                      (println "Bot joined new chat: " chat)
                                      (t/send-text token id "Welcome!")))

              (h/command "help" {{id :id :as chat} :chat}
                         (println "Help was requested in " chat)
                         (t/send-text token id "Help is on the way"))

              (h/message message
                         (println message)
                         (if (not (nil? (-> message :photo)))
                           (handle-photo message)
                           (handle-filter message))))

(def channel (p/start token handler {:timeout 2 :long_polling_timeout 30}))
(println "Origami bot started...")

(.addShutdownHook (Runtime/getRuntime)
                  (Thread. ^Runnable (fn []
                                       (p/stop channel)
                                       (println "Origami bot stopped..."))))

(while true
  (Thread/sleep 50))