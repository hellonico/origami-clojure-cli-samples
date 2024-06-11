":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.core :as cv ])
(require '[opencv4.video :as cvv ])
(require '[progrock.core :as pr])

(def output-file (or (first *command-line-args*)  "timelapse.mp4"))
(def input (or (second *command-line-args*)  "out"))
(def start-time  (.getEpochSecond (java.time.Instant/parse "2020-04-22T23:55:30.00Z")))

(def files  
  (sort-by #(.lastModified %) (file-seq (clojure.java.io/as-file input))))

; only works on mac
(def  fourcc 1196444237)
(def w (cvv/new-videowriter))

(println "Video Start > " (java.util.Date.))

(let [
	pic (cv/imread (.getPath (first files))) 
	_size (cv/new-size (.width pic ) (.height pic) )]
(.open w output-file  fourcc 12 _size))

(loop [files files bar (pr/progress-bar (count files))]
(if (empty? files) 
 (do (.release w) (pr/print (pr/done bar)))
 (let [file (first files) lm (.lastModified file)]
  (if (> lm start-time)
  (let [img (cv/imread (.getPath file)) ]
    ; (println "Adding " file)
		(doseq [_ (range 100)] (.write w img))
		(.release img)))
	(pr/print bar)
	(recur (rest files) (pr/tick bar)))))

(println "Video Finished > " (java.util.Date.))