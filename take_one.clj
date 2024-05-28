":";exec clj -M $(basename $0) "$1" $2

(require '[opencv4.core :refer [new-videocapture new-mat imwrite]] '[opencv4.video :as v])
(def CAM_OPEN_TIME 50)

(defn take-one[cam output]
  (let[ capture (v/capture-device cam) target (new-mat)]
	(Thread/sleep CAM_OPEN_TIME)
	(.read capture target)
	; (.release capture)
	(imwrite target output)))

(if (> 2 (count *command-line-args*))
	(println "Usage: take_one.clj <video-map> <output-file>")
	(take-one 
	  (or (first *command-line-args*) "0")
	  (or (second *command-line-args*) "one.png")))

; ./take_one.clj '{:device 0 :frame-width 640 :frame-height 360 :fps 60}' one.png  