#!/bin/sh
#_(
   DEPS='
   {
  :deps 
   { org.clojure/tools.cli {:mvn/version "1.1.230"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)


(ns image-resizer
  (:import [java.awt Image Color]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO])
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :as cli]))

;; Default size
(def default-size [[2064 2752]])

;; Function to parse size argument safely
(defn parse-size [size]
  (let [size-str (if (sequential? size) (first size) size)]
    (map (fn [dim]
           (let [[w h] (map #(Integer/parseInt %) (str/split dim #"x"))]
             [w h]))
         (str/split size-str #","))))

;; Define CLI options
(def cli-options
  [["-s" "--size SIZE" "Resize dimensions (e.g., \"2064x2752,1920x1080\")"
    :parse-fn parse-size
    :default default-size]
   ["-f" "--folder FOLDER" "Input folder (default: ~/Desktop)"
    :default (str (System/getProperty "user.home") "/Desktop")]
   ["-o" "--format FORMAT" "Output format: jpg or png (default: jpg)"
    :default "jpg"
    :validate [#{"jpg" "png"} "Must be 'jpg' or 'png'"]]
   ["-h" "--help"]])

;; Function to resize image
(defn resize-image [file width height format]
  (let [input-img (ImageIO/read file)
        img-type (if (= format "jpg") BufferedImage/TYPE_INT_RGB BufferedImage/TYPE_INT_ARGB)
        resized-img (BufferedImage. width height img-type)
        graphics (.createGraphics resized-img)]
    (when (= format "jpg")
      (.setPaint graphics Color/WHITE) ;; White background for JPG
      (.fillRect graphics 0 0 width height))
    (.drawImage graphics input-img 0 0 width height nil)
    (.dispose graphics)
    resized-img))

;; Process images based on parameters
(defn process-images [opts]
  (let [{:keys [size folder format]} opts
        folder-file (io/file folder)
        files (filter #(re-matches #"Screen.*\.png" (.getName %))
                      (.listFiles folder-file))]
    (doseq [file files]
      (let [name (subs (.getName file) 0 (- (.length (.getName file)) 4))] ;; Strip `.png`
        (doseq [[width height] size]
          (let [output-file (io/file folder-file (str "resized_" name "_" width "x" height "." format))
                resized-img (resize-image file width height format)]
            (ImageIO/write resized-img format output-file)
            (println "Resized and saved:" (.getAbsolutePath output-file))))))))

;; Main function
; (defn -main [& args]
  (let [{:keys [options errors summary]} (cli/parse-opts *command-line-args* cli-options)]
    (cond
      (:help options) (println summary)
      errors (do (println "Errors:\n" (str/join "\n" errors))
                 (println summary))
      :else (process-images options)))
