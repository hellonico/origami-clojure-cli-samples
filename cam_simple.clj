":";exec clj -M $(basename $0)  "$@"

(require '[opencv4.utils :as u] '[clojure.tools.cli :refer [parse-opts]])

(def filter_s (or (first *command-line-args*) (slurp "resources/filters.edn")))

(def cli-options
  [["-c" "--cam VIDEO" "A descriptor for the cam" :default "resources/j5cam.edn"]
   ["-f" "--filter FILTER or FILE" :default "resources/filters.edn"]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cam_simple.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam filter]} options]
        (println "Simple Cam with options:" options)
        (u/simple-cam-window cam filter)        ))))