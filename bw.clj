":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.core :as cv]) 

(defn bw[args]
	(->  args
	cv/imread 
 	(cv/cvt-color! cv/COLOR_RGB2GRAY)
 	(cv/imwrite (str args "_bw.png"))))

(if (not *command-line-args*)
	(println 
		"\nTurn an image to black and white."
		"\nUsage: bw.clj <inputfile>")
	(bw (first *command-line-args*)))