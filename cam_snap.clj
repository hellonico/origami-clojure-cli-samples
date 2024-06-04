":";exec clj -M $(basename $0) "$@"

(require 
 '[java-time.api :as time] 
 '[opencv4.core :refer [new-mat imwrite]]
 '[opencv4.utils :as u] 
 '[clojure.tools.cli :refer [parse-opts]])

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default 0]
   ["-d" "--dir FOLDER" "A folder where to save the output" :default "out"]
   ["-o" "--output FILE" "Output file" :default (str (time/format (time/formatter  "yyyy-MM-dd-HH:mm:ss") (time/local-date-time)) ".png")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_snap.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam output dir]} options]
        (println "Snap with options:" options)
        (u/take-one cam (str dir "/" output))))))