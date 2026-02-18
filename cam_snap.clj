#!/bin/sh
#_(

DEPS='
{:mvn/repos
 {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 :deps
 {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}
  org.clojure/clojure {:mvn/version "1.11.3"}
  origami/filters {:mvn/version "1.48"}
  clojure.java-time/clojure.java-time {:mvn/version "1.4.2"}
  org.clojure/tools.cli {:mvn/version "1.1.230"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(require
 '[java-time.api :as time]
 '[clojure.string :as str]
 '[opencv4.core :refer [new-mat imwrite]]
 '[opencv4.utils :as u]
 '[clojure.tools.cli :refer [parse-opts]])

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default 0]
   ["-d" "--dir FOLDER" "A folder where to save the output" :default "out"]
   ["-o" "--output FILE" "Output file"
    :default (str (time/format (time/formatter "yyyy-MM-dd-HH-mm-ss") (time/local-date-time)) ".png")]
   ["-n" "--number N" "Number of pictures to take" 
    :default 1 
    :parse-fn #(Integer/parseInt %)]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_snap.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam output dir number]} options
            _ (.mkdirs (clojure.java.io/as-file dir))]
        (println "Snap with options:" options)
        (if (= 1 number)
           (u/take-one cam (str dir "/" output))
           (dotimes [i number]
             (let [parts (str/split output #"\.")
                   base  (str/join "." (butlast parts))
                   ext   (last parts)
                   fname (str base "_" i "." ext)
                   path  (str dir "/" fname)]
               (println "Taking snap" (inc i) "/" number " -> " path)
               (u/take-one cam path)
               ;; u/take-one takes some time to init cam, so delay is implicit
               )))))))
