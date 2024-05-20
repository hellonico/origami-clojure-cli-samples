":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.utils :as u])

(def fi (origami.filters.Annotate.))
(.setText fi "")

(u/simple-cam-window fi)

; TODO: move to core
(defn set-interval [callback ms] 
  (future (while true (do (Thread/sleep ms) (callback)))))

(def job (set-interval (fn[] (.setText fi (str (java.util.Date.)))) 1000))