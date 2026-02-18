#!/bin/sh
#_(DEPS='
   {:mvn/repos
    {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
    :deps
    {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}
     origami/filters {:mvn/version "1.48"}}} 'exec clj -Sdeps "$DEPS" -M "$0" "$@")
(ns photomosaic
  (:require
   [opencv4.utils :as u]
   [opencv4.core :refer :all]))

(defn mean-average-bgr [mat]
  (let [_mean (new-matofdouble)]
    (-> mat clone
        (median-blur! 3)
        (mean-std-dev _mean (new-matofdouble)))
    _mean))

(defn collect-pictures
  ([top-folder] (collect-pictures top-folder "jpg"))
  ([top-folder ext]
   (->> top-folder
        clojure.java.io/as-file
        file-seq
        (filter #(.endsWith (.getName %) ext))
        (map #(.getPath %)))))

(defn indexing [files for-size]
  (println "Indexing" (count files) "images...")
  (zipmap files (map #(-> % imread (resize! for-size) mean-average-bgr) files)))

(defn apply-to-vals [m f]
  (into {} (for [[k v] m] [k (f v)])))

(defn find-closest [target indexed]
  (let [mean-bgr-target (mean-average-bgr target)]
    (first (sort-by val < (apply-to-vals indexed #(norm mean-bgr-target %))))))

(defn get-cache-image [cache path width height]
  (let [hit (.get cache path)]
    (if (not (nil? hit))
      hit
      (let [new-e (-> path imread (resize! (new-size width height)))]
        (.put cache path new-e)
        new-e))))

(defn tile [org indexed ^long grid-x ^long grid-y]
  (let [dst    (u/mat-from org)
        k      (atom 0)
        total  (* grid-x grid-y)
        width  (/ (.cols dst) grid-x)
        height (/ (.rows dst) grid-y)
        cache  (java.util.HashMap.)]
    (doseq [^long i (range 0 grid-y)]
      (doseq [^long j (range 0 grid-x)]
        (let [square (submat org (new-rect (* j width) (* i height) width height))
              best   (first (find-closest square indexed))
              img    (get-cache-image cache best width height)
              sub    (submat dst (new-rect (* j width) (* i height) width height))]
          (copy-to img sub)
          (swap! k inc)
          (print (str "\r" @k "/" total " (" (int (* 100.0 (/ @k total))) "%)"))
          (flush))))
    (println "\nDone!")
    dst))

(defn photomosaic [target-image images-folder grid-x grid-y]
  (let [target  (imread target-image)
        _       (println "Target image loaded:" target-image)
        files   (collect-pictures images-folder)
        _       (println "Found" (count files) "images in" images-folder)
        indexed (indexing files (new-size grid-x grid-y))
        _       (println "Indexed. Building mosaic" grid-x "x" grid-y "...")]
    (tile target indexed grid-x grid-y)))

(if (< (count *command-line-args*) 4)
  (println "Usage: photomosaic.clj <target-image> <folder-of-images> <grid-x> <grid-y> [output.png]")
  (let [input  (first *command-line-args*)
        folder (second *command-line-args*)
        x      (read-string (nth *command-line-args* 2))
        y      (read-string (nth *command-line-args* 3))
        output (or (nth *command-line-args* 4 nil) (str "photomosaic_" x "x" y ".png"))]
    (println "Mosaic parameters:" input folder x y "→" output)
    (-> (photomosaic input folder x y)
        (imwrite output))
    (println "Saved to:" output)))