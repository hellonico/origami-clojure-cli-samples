(require '[opencv4.utils :as u])

(defn java-filter [fi]
  (fn [mat] (.apply fi mat)))
(def fi (origami.filters.brandnew.Matrix.))
(set! (.-fontSize fi) 1)
(set! (.-numStreams fi) 1000)
(set! (.-japaneseCharacters fi) "abcdefghijklmnopqrstuvwxyz")
(u/simple-cam-window (java-filter fi))
