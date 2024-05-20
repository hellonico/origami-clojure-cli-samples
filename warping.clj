":";exec clj -M $(basename $0) $1 $2

(ns opencv4.video.warping
  (:require
   [opencv4.core :refer :all]
   [opencv4.filter :as f]
   [opencv4.utils :as u]))

(def points1 (atom [[0 0] [200 0] [30 300] [500 300]]))
(def points2 (atom [[70 10] [200 52] [28 200] [389 0]]))

(defn warp [buffer]
  (-> buffer
      (warp-perspective!
       (get-perspective-transform
        (u/matrix-to-matofpoint2f @points1)
        (u/matrix-to-matofpoint2f @points2))
       (size buffer))))

(defn to-gray [buffer]
  (-> buffer (cvt-color! COLOR_RGB2GRAY) (cvt-color! COLOR_GRAY2RGB)))

;; (def filter_s (or (first *command-line-args*) "filters.edn"))
;; (def fi (f/s->fn-filter filter_s))

(u/simple-cam-window
 {:video {:device 0} :frame {:width 650  :height 400 :title "Warping Cello"}}
 (comp (origami.filters.FPS.) warp))

(doseq [i (range 0 1000)]
  (Thread/sleep 10)
  (reset! points1 [[0 0] [200 i] [30 300] [500 300]])
  (reset! points1 [[0 0] [200 i] [30 300] [500 i]]))
