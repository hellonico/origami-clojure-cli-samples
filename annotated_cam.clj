(require '[opencv4.utils :as u])

(defn java-filter [fi]
  (fn [mat] (.apply fi mat)))
(def fi (origami.filters.Annotate.))

(.setText fi "")
; not public
; (set! (.-text fi) "hello again")

(u/simple-cam-window (java-filter fi))

(defn set-interval [callback ms] 
  (future (while true (do (Thread/sleep ms) (callback)))))

(def job (set-interval #(.setText fi (str (java.util.Date.))) 1000))