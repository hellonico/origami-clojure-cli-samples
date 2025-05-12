#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 	:deps 
   { origami/origami {:mvn/version "4.11.0-6"}
     org.clojure/clojure {:mvn/version "1.11.3"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
;
; Simple Script that turns a picture into a manga drawing
;


(require 
  '[opencv4.core :as cv :refer [COLOR_RGB2GRAY THRESH_BINARY cvt-color! gaussian-blur! threshold! new-size imread imwrite dilate! CV_8UC1]] 
  '[clojure.tools.cli :refer [parse-opts]]) 

(defn manga[in out]
    (-> in
         (imread)
         (cvt-color! COLOR_RGB2GRAY)
         (gaussian-blur! (new-size 7 7) 1.5 1.5)
         (threshold! 100 255 THRESH_BINARY)
         (imwrite out)))

(def cli-options
  [["-i" "--input FILE" :id :input :default "resources/cat.jpg" :validate [#(.exists (clojure.java.io/as-file %)) "input must be a file"]]
   ["-o" "--output FILE" "Output file" :default "out/manga.png"]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
   (do (println "Usage: ./manga.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [input output]} options]
        (println "Manga with options:" options)
        (manga input output)))))