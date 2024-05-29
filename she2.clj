":";exec clj -M $(basename $0)

(require '[opencv4.core :as cv] '[opencv4.utils :as u]) 
(import '[org.opencv.core Mat Scalar])

(cv/dump 
	(cv/multiply! 
		(Mat/ones 5 5 cv/CV_8UC1) 
		(Mat/eye 5 5 cv/CV_8UC1)))