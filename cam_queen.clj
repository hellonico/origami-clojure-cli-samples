#!/bin/sh
#_(

DEPS='
{:mvn/repos
 {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 :deps
 {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}
  org.clojure/clojure {:mvn/version "1.11.3"}
  origami/filters {:mvn/version "1.48"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(require '[opencv4.core :refer :all]
         '[clojure.java.shell :refer [sh]])
(import '[org.opencv.videoio VideoCapture])
(import '[org.opencv.highgui HighGui])

(def cap (VideoCapture. 0))

(if (.isOpened cap)
  (let [snaps (atom [])
        start-time (System/currentTimeMillis)
        last-snap (atom start-time)
        frame (new-mat)]
    
    (println "Starting Queen Cam! Get ready for 4 poses!")
    (println "Taking a picture every 2 seconds...")
    
    ;; Initial Loop
    (loop []
      (if (and (< (count @snaps) 4) (.read cap frame))
        (do
          (let [now (System/currentTimeMillis)
                diff (- now @last-snap)]
            
            (if (> diff 2000)
              (let [fname (str "queen_snap_" (count @snaps) ".png")]
                (imwrite frame fname)
                (swap! snaps conj fname)
                (reset! last-snap now)
                (println "📸 Snap!" (count @snaps) "-> " fname)
                ;; Flash effect
                (let [white (new-mat)] 
                   (bitwise-not frame white) 
                   (HighGui/imshow "Queen Cam" white)
                   (HighGui/waitKey 100)))
              (HighGui/imshow "Queen Cam" frame)))
          
          (if (not= 27 (HighGui/waitKey 30))
            (recur)
            (println "Aborted by user.")))
        (println "Finished capturing.")))
    
    (.release cap)
    (HighGui/destroyAllWindows)
    
    (if (= 4 (count @snaps))
      (do
        (println "Processing 👑 Queen Mosaic...")
        (let [args (conj @snaps "queen_output.jpg")
              _    (println "Running: ./cv_queen.clj" args) 
              result (apply sh "./cv_queen.clj" args)]
           (println (:out result))
           (println (:err result))
           (if (zero? (:exit result))
             (do 
               (println "Done! Saved to queen_output.jpg")
               (try (sh "open" "queen_output.jpg") (catch Exception _)))
             (println "Error in processing."))))
      (println "Not enough pictures taken.")))
  (println "Could not open camera."))

(System/exit 0)
