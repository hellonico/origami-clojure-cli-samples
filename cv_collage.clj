":";exec clj -M $(basename $0) $1 $2

(require '[opencv4.core :as cv] '[opencv4.filter :as f] '[clojure.tools.cli :refer [parse-opts]])
(import '[org.opencv.core Mat])

(def cli-options
  [["-i1" "--mat1 image" "Image 1" :default "resources/cat.jpg"]
   ["-i2" "--mat2 image" "Image 2" :default "resources/cat.jpg"]
   ["-c" "--collage fn" "Collage function" :default {:class origami.render.LeftRight :percentage 0.3}]
   ["-o" "--output FILE" "Output file" :default (str "out/collage.png")]
   ["-h" "--help"]])

(let [{:keys [options _ errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
    (do (println "Usage: ./cv_collage.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [mat1 mat2 collage output]} options]
        (println "Collage with options:" options)
        (cv/imwrite (.apply (f/s->render collage) (into-array Mat (map cv/imread [mat1 mat2])))  output)))))