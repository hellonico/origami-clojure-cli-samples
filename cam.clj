":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.utils :as u] '[opencv4.filter :as f])

(def filter_s (or (first *command-line-args*) "filters.edn"))
(def fi (f/s->fn-filter filter_s))

(u/simple-cam-window {:video {:device 0 :height 3264 :width 2448 :fps 20}} [fi])

;; (def fi (f/s->fn-filter "{:aperture 3, :blur 7, :class origami.filters.cartoon.NewCartoon2, :kmeans 8, :lineSize 17}"))
;; (def fi (f/s->fn-filter "{:class origami.filters.cartoon.NewCartoon}"))
