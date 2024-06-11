#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
    :deps 
    { origami/origami {:mvn/version "4.9.0-7"}
     org.clojure/clojure {:mvn/version "1.11.3"}
     origami/filters {:mvn/version "1.47"}
     clojure.java-time/clojure.java-time {:mvn/version "1.4.2"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
    }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"
)

(require 
 '[java-time.api :as time] 
 '[opencv4.core :refer [new-mat imwrite]]
 '[opencv4.utils :as u] 
 '[clojure.tools.cli :refer [parse-opts]])

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default 0]
   ["-d" "--dir FOLDER" "A folder where to save the output" :default "out"]
   ["-f" "--frames number of frames to use for one shot" "Frames number" :default 100 :parse-fn #(Integer/parseInt %)]
   ["-l" "--lapse MILLISECONDS" "Lapse between each frame" :default 50 :parse-fn #(Integer/parseInt %)]
   ["-o" "--output FILE" "Output file" :default (str (time/format (time/formatter  "yyyy-MM-dd-HH:mm:ss") (time/local-date-time)) ".png")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (u/print-help *file* summary)
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [lapse frames cam output dir]} options]
        (println "Long Exposure with options:" options)
        (u/long-exposure cam (str dir "/" output) frames lapse)))))