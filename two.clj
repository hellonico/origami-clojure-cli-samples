":";exec clj -M $(basename $0) $1 $2

(require  '[opencv4.core :refer :all]  
          '[opencv4.utils :as u])

(defn render-two [ left right ]
(let [ output (new-mat (rows left) (* 2 (cols left))  CV_8UC3 (new-scalar 255 255 255))
ol_ (submat output (new-rect 0 0 (cols left) (rows left)))
or_ (submat output (new-rect (cols left) 0 (cols left) (rows left)))
]
(copy-to left ol_)
(resize! right (size or_))
(copy-to right or_)
; (u/resize-by output 0.3)
output))

(defn to-gray[buffer ]
(-> buffer (cvt-color! COLOR_RGB2GRAY) (cvt-color! COLOR_GRAY2RGB)))

(u/cams-window
  {:devices [{:device 0 :fn  to-gray} {:device 1 :fn identity}]
   :video {:fn render-two}
   :frame {:title "TwoCellos"}})