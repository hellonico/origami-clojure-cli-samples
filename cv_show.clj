":";exec clj -M $(basename $0)

(require '[opencv4.core :refer [imread clone] :as cv]) 
(require '[opencv4.utils :as u])
(require '[nextjournal.beholder :as beholder])
(require '[opencv4.filter :as f] '[clojure.tools.cli :refer [parse-opts]]) 

(defn load-img [src filter]
	(-> src 
		imread
		((f/s->fn-filter filter))))

(defn reload[window src filter]
	(let [mat (load-img src filter)] 
				(cv/resize! mat (cv/new-size (.getWidth (.getSize window)) (.getHeight (.getSize window))))
				(u/re-show mat window)))

(defn showing[input filter]
	(let[mat (load-img input filter) window (u/show mat)]
		(reload window input filter)
		(beholder/watch #(do (println %) (reload window input filter) ) "resources")))

(def cli-options
  [["-i" "--mat image" "Image" :default "resources/cat.jpg"]
   ["-f" "--filter filter" "Filter" :default "resources/filters.edn"]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
   (do (println "Usage: ./cv_show.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [mat filter]} options]
        (println "Show with options:" options)
        (showing mat filter)))))