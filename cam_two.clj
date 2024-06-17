":";exec clj -J-Djava.library.path=natives  -M $(basename $0) "$@"

(require  '[opencv4.core :refer [cvt-color! COLOR_BGR2GRAY COLOR_GRAY2BGR]]  
          '[clojure.tools.cli :refer [parse-opts]]
          '[opencv4.utils :as u])

(defn to-gray[buffer ]
  (-> buffer (cvt-color! COLOR_BGR2GRAY) (cvt-color! COLOR_GRAY2BGR)))

(def cli-options
  [["-c1" "--cam1 VIDEO" "A descriptor for the cam1" :default {:device 0 :fn  to-gray}]
   ["-c2" "--cam2 VIDEO" "A descriptor for the cam2" :default {:device 1 :fn "resources/filters.edn"}]
   ["-r" "--render fn" "Render function" :default {:class origami.render.LeftRight :percentage 0.4}]
   ["-t" "--title TITLE" "Title for the window" :default "Two Cellos"]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: cam_two.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [cam1 cam2 render title]} options]
        (println "Two Cams with options:" options)
        (u/cams-window
         {:devices [cam1 cam2]
          ;:video {:fn {:class origami.render.CombinedMatsIntoTriangle}}
          :video {:fn render}
          :frame {:title title :width 500 :height 500}})))))