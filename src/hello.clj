(ns hello
	(:require 
		[opencv4.utils :as u]
		[opencv4.core :refer :all]))

(defn -main[]
(-> "https://raw.githubusercontent.com/hellonico/origami/master/doc/cat_in_bowl.jpeg"
    (u/mat-from-url)
    (u/resize-by 0.3)
    (imwrite "cat.jpg")))