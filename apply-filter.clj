":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.core :as cv] '[opencv4.filter :as f]) 

(def img (first *command-line-args*))
(def filter_s (or (second *command-line-args*) "filters.edn"))
(def fi (f/s->fn-filter filter_s))

; (def out (or (and (> (count *command-line-args*) 1) (nth *command-line-args* 2)) (str "out/" (first *command-line-args*) "_filtered.png")))
(def out (str "out/" (first *command-line-args*) "_filtered.png"))
(-> img
	(cv/imread)
	(fi)
	(cv/imwrite out))