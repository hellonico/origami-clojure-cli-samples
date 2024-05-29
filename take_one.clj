":";exec clj -M $(basename $0) "$@"

(require '[java-time.api :as time] '[opencv4.core :refer [new-videocapture new-mat imwrite]] '[opencv4.video :as v])
(def CAM_OPEN_TIME 50)

(defn take-one[cam output]
  (let[ capture (v/capture-device cam) target (new-mat)]
	(Thread/sleep CAM_OPEN_TIME)
	(.read capture target)
	; (.release capture)
	(imwrite target output)))

; (println (count *command-line-args*))
; (if (> 1 (count *command-line-args*))
	; (println "Usage: take_one.clj <cam-map|cam-index> <output-file>")
	(take-one 
	  (or (first *command-line-args*) "0")
	  (or (second *command-line-args*) (str "out/" (time/format (time/formatter  "yyyy-MM-dd-HH:mm:ss") (time/local-date-time)) ".png")))

; ./take_one.clj '{:device 0 :frame-width 640 :frame-height 360 :fps 60}' one.png  