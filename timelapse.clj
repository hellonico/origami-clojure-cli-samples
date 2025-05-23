#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 	:deps 
   { origami/origami {:mvn/version "4.11.0-6"}
     org.clojure/clojure {:mvn/version "1.11.3"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
     clojure.java-time/clojure.java-time {:mvn/version "1.4.2"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)

(require 
	'[opencv4.utils :as u]
  '[opencv4.core :refer [new-mat imwrite]] 
  '[opencv4.video :refer [capture-device ]] 
  '[clojure.tools.cli :refer [parse-opts]]	
  '[java-time.api :as time] ) 

(def counter (atom 0))
(defn timelapse[cam folder time-lapse picture-format date count]
	(.mkdir (clojure.java.io/as-file folder))
	(u/on-shutdown #(println "Quitting timelapse with " @counter " pictures."))
	(while (> count @counter)
		(swap! counter inc)
		(Thread/sleep (* 1000 time-lapse))
    (u/take-one cam (str folder "/" (time/format (time/formatter date) (time/local-date-time))  "." picture-format))))

(def cli-options
  [["-c" "--camera INDEX or EDN" "Video descriptor. Either camera index or an EDN map with properties." :default "0"]
   ["-d" "--dir DIR" "Folder where to store the pictures" :default "out"]
   [nil "--count COUNT" "How many pictures to take" :default Integer/MAX_VALUE :parse-fn #(Integer/parseInt %)]
   ["-f" "--format JPG|PNG" "Format for the output pictures" :default "png"]
   ["-o" "--output format" :default "yyyy-MM-dd-HH-mm-ss"]
   ["-l" "--lapse SECONDS" "Lapse between each pictures" :default 5 :parse-fn #(Integer/parseInt %)]
   ["-h" "--help"]])

(let [{:keys [options arguments errors summary]} (parse-opts *command-line-args* cli-options)]
	(if (:help options)
	 (u/print-help *file* summary)
	 (let [{:keys [camera dir lapse format output count]} options]
	 	(println "Timelapse me with options" options)
	 	(timelapse camera dir lapse format output count))))