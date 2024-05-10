(require '[opencv4.core :refer [CV_8UC1 dump multiply! bitwise-not! clone]])
(import '[org.opencv.core Mat])

(let [
	mat (Mat/eye 3 3 CV_8UC1) 
	mat2 (-> mat clone bitwise-not!)]
	(dump mat)
	(dump mat2)
	(dump (multiply! mat mat2)))