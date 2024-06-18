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

(require '[opencv4.utils :as u] '[clojure.tools.cli :refer [parse-opts]])
(require '[clojure.java.io :as io])

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default (str (.getParent (io/as-file *file*)) "/resources/j5cam.edn")]
   ["-f" "--filter FILTER or FILE" :default (str (.getParent (io/as-file *file*)) "/resources/filters.edn")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_simple.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam filter]} options]
        (println "Simple Cam with options:" options)
        (u/simple-cam-window cam filter)))))