#!/bin/sh
#_(

   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
  :deps 
   { origami/origami {:mvn/version "4.9.0-8"}
     org.clojure/clojure {:mvn/version "1.11.3"}
     origami/filters {:mvn/version "1.48"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"
)

(require 
  '[opencv4.utils :as u] 
  '[opencv4.video :as v] 
  '[clojure.tools.cli :refer [parse-opts]])
(require '[clojure.java.io :as io])
(import '[org.opencv.videoio Videoio])

(def resolutions
  [[640 480] [800 600] [1024 768] [1280 720] [1280 800]
   [1280 1024] [1366 768] [1440 900] [1600 900] [1600 1200]
   [1680 1050] [1920 1080] [1920 1200] [2048 1080] [2560 1440]
   [2560 1600] [3840 2160] [4096 2160] [5120 2880] [7680 4320]])

(defn test-camera-resolutions[]
  (let [camera (v/capture-device {:device 0})]
    
(if-not (.isOpened camera)
      (println "Error: Camera is not available.")
      (doseq [[width height] resolutions]
        (println "Trying: " width "x" height)
        (.set camera Videoio/CAP_PROP_FRAME_WIDTH width)
        (.set camera Videoio/CAP_PROP_FRAME_HEIGHT height)
        
        (let [actual-width (int (.get camera Videoio/CAP_PROP_FRAME_WIDTH))
              actual-height (int (.get camera Videoio/CAP_PROP_FRAME_HEIGHT))]
          (when (and (= actual-width width) (= actual-height height))
            (println "Supported resolution:" width "x" height)))))

    (.release camera)

    ))

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default (str (.getParent (io/as-file *file*)) "/resources/j5cam.edn")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_resolutions.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam]} options]
        (println "Cam Testing with options:" options)
        (test-camera-resolutions)))))
