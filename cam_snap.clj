":";exec clj -M $(basename $0) "$@"

(require 
 '[java-time.api :as time] 
 '[opencv4.core :refer [new-mat imwrite]] 
 '[clojure.tools.cli :refer [parse-opts]]
 '[opencv4.video :as v])

(def CAM_OPEN_TIME 50)

(defn take-one[cam output]
  (let[ capture (v/capture-device cam) target (new-mat)]
	(Thread/sleep CAM_OPEN_TIME)
	(.read capture target)
	; (.release capture)
	(imwrite target output)
	(println "Written:" output " > " target)))

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default 0]
   ["-o" "--output FILE" "Output file" :default (str "out/" (time/format (time/formatter  "yyyy-MM-dd-HH:mm:ss") (time/local-date-time)) ".png")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_snap.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam output]} options]
        (println "Snap with options:" options)
        (take-one cam output)))))