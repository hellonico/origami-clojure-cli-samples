#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
  :deps 
   { origami/origami {:mvn/version "4.11.0-6"}
     org.clojure/clojure {:mvn/version "1.11.3"}
     origami/filters {:mvn/version "1.49"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)

(require
   '[opencv4.core :refer :all]
   '[opencv4.utils :as u])

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

(u/simple-cam-window
 {:video {:device 0} :frame {:width 650  :height 400 :title "Warping Cello"}}
 (comp (origami.filters.FPS.) warp))

; do with tween
(doseq [i (range 0 1000)]
  (Thread/sleep 10)
  (reset! points1 [[0 0] [200 i] [30 300] [500 300]])
  (reset! points1 [[0 0] [200 i] [30 300] [500 i]]))
