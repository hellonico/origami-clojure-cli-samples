":";exec clj -M $(basename $0)

(require '[opencv4.core :as cv] '[opencv4.utils :as u]) 
(import '[org.opencv.core Mat Scalar])
; (-> "https://raw.githubusercontent.com/hellonico/origami/master/doc/cat_in_bowl.jpeg"
;     (u/mat-from-url)
;     (u/resize-by 0.3)
;     (imwrite "cat.jpg"))

(cv/dump 
	(cv/multiply (Mat/eye 5 5 cv/CV_8UC1) (Mat/eye 1 2 cv/CV_8UC1))
	)