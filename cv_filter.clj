":";exec clj -M $(basename $0) "$@"

(require '[opencv4.core :as cv] '[opencv4.filter :as f] '[clojure.tools.cli :refer [parse-opts]])  

(defn filtering [input filter_s output]
(-> input
	(cv/imread)
	(filter_s)
	(cv/imwrite output)))

(def helper (u/script-helper *file*))
(def cli-options
  [["-i" "--input FILE" :id :input :default "resources/cat.jpg" :validate [#(.exists (clojure.java.io/as-file %)) "input must be a file"]]
   ["-f" "--filter FILTER or FILE" :default  (str (helper :parent) "/resources/filters.edn")]
   ["-o" "--output FILE" "Output file" :default (str "out/filtered.png")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
   (do (println "Usage: cv_filter.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [input filter output]} options]
          (println "Filter with options:" options)
          (filtering input (f/s->fn-filter filter) output)))))