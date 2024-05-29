":";exec clj -M $(basename $0) "$@"

;
; Simple Script that turns a picture into a manga drawing
;

(require 
  '[opencv4.core :as cv :refer [COLOR_RGB2GRAY THRESH_BINARY cvt-color! gaussian-blur! threshold! new-size imread imwrite dilate! CV_8UC1]] 
  '[clojure.tools.cli :refer [parse-opts]]) 

(defn manga[in out]
    (-> in
         (imread)
         (cvt-color! COLOR_RGB2GRAY)
         (gaussian-blur! (new-size 7 7) 1.5 1.5)
         (threshold! 100 255 THRESH_BINARY)
         (imwrite out)))

(def cli-options
  [["-i" "--input FILE" :id :input :default "" :validate [#(.exists (clojure.java.io/as-file %)) "input must be a file"]]
   ["-o" "--output FILE" "Output file" :default "manga.png"]
   ["-h" "--help"]])

(let [{:keys [options arguments errors summary]} (parse-opts *command-line-args* cli-options)]
  (if (:help options)
   (do (println "Usage: ./manga.clj") (println summary))
    (if (not (nil? errors))
      (println errors)
      (let [{:keys [input output]} options]
        (println "Manga with options:" options)
        (manga input output)))))