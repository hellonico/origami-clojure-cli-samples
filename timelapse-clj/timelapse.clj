":";exec clj -M $(basename $0) "$@"

(require 
	'[opencv4.utils :as u]
  '[opencv4.core :refer [new-mat imwrite]] 
  '[opencv4.video :refer [capture-device ]] 
  '[clojure.tools.cli :refer [parse-opts]]	
  '[java-time.api :as time] ) 

(def CAM_OPEN_TIME 100)

(defn take-one[cam output]
  (let[ capture (capture-device cam) target (new-mat)]
	(Thread/sleep CAM_OPEN_TIME)
	(.read capture target)
	(.release capture)
	(println "Writing: " output)
	(imwrite target output)))

(def counter (atom 0))
(defn timelapse[cam folder time-lapse picture-format]
	(.mkdir (clojure.java.io/as-file folder))
	(u/on-shutdown #(println "Quitting timelapse with " @counter " pictures."))
	(while true
		(swap! counter inc)
		(Thread/sleep (* 1000 time-lapse))
    (take-one cam (str folder "/" (time/format (time/formatter  "yyyy-MM-dd-HH:mm:ss") (time/local-date-time))  "." picture-format))))

(def cli-options
  [["-c" "--camera INDEX or EDN" "Video descriptor. Either camera index or an EDN map with properties." :default "0"]
   ["-d" "--dir DIR" "Folder where to store the pictures" :default "out"]
   ["-f" "--format JPG|PNG" "Format for the output pictures" :default "png"]
   ["-l" "--lapse SECONDS" "Lapse between each pictures" :default 5 :parse-fn #(Integer/parseInt %)]
   ["-h" "--help"]])

(let [{:keys [options arguments errors summary]} (parse-opts *command-line-args* cli-options)]
	(if (:help options)
	 (do (println "Usage: ./params.clj") (println summary))
	 (let [{:keys [camera dir lapse format]} options]
	 	(println "Timelapse me with options" options)
	 	(timelapse camera dir lapse format))))