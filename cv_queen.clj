#!/bin/sh
#_(

DEPS='
{:mvn/repos
 {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 :deps
 {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}
  origami/filters {:mvn/version "1.48"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(ns cv-queen
  (:require [opencv4.core :refer :all]
            [opencv4.utils :as u]
            [clojure.java.io :as io])
  (:import [org.opencv.core Core Scalar Mat CvType]))

(defn hex->rgb [hex]
  (let [hex (if (.startsWith hex "#") (subs hex 1) hex)
        r (Integer/valueOf (subs hex 0 2) 16)
        g (Integer/valueOf (subs hex 2 4) 16)
        b (Integer/valueOf (subs hex 4 6) 16)]
    (new-scalar b g r)))

(defn crop-to-square [mat size]
  (let [w (.cols mat)
        h (.rows mat)
        min-dim (clojure.core/min w h)
        x (int (/ (- w min-dim) 2))
        y (int (/ (- h min-dim) 2))
        cropped (submat mat (new-rect x y min-dim min-dim))]
    (resize! cropped (new-size size size))))

(defn duotone [mat fg-hex bg-hex]
  (let [gray (-> mat (cvt-color! COLOR_BGR2GRAY))
        ;; Otsu thresholding for binary mask
        binary (threshold! gray 128 255 (bit-or THRESH_BINARY THRESH_OTSU))
        
        fg-scalar (hex->rgb fg-hex)
        bg-scalar (hex->rgb bg-hex)
        
        fg-mat (new-mat (.size mat) CV_8UC3 fg-scalar)
        bg-mat (new-mat (.size mat) CV_8UC3 bg-scalar)
        
        mask (cvt-color binary COLOR_GRAY2BGR)
        inv-mask (new-mat)
        
        masked-bg (new-mat)
        masked-fg (new-mat)
        final (new-mat)]
    
    ;; Create inverse mask
    (bitwise-not mask inv-mask)
    
    ;; Combine: mask (white/bg) gets bg-color, inv-mask (black/fg) gets fg-color
    (bitwise-and mask bg-mat masked-bg)
    (bitwise-and inv-mask fg-mat masked-fg)
    
    (add masked-bg masked-fg final)
    final))

(defn -main [& args]
  (if (< (count args) 4)
    (println "Usage: cv_queen.clj img1 img2 img3 img4 [scale] [output.jpg]")
    (let [[i1 i2 i3 i4 scale out]
          (cond
            (= (count args) 4) (concat args [1.0 "hot_space_cover.jpg"])
            (= (count args) 5) (let [last (last args)]
                                 (if (re-matches #"\d+(\.\d+)?" last)
                                   (concat args [(Double/parseDouble last) "hot_space_cover.jpg"])
                                   (concat (take 4 args) [1.0 last])))
            :else (concat (take 4 args) [(Double/parseDouble (nth args 4)) (nth args 5)]))
          
          inputs [i1 i2 i3 i4]
          ;; Colors pairs [BG FG] matching original script
          colors [[ "#ff3838" "#3d5afe" ]   ;; TL
                  [ "#00d9ff" "#ff5722" ]   ;; TR
                  [ "#00c853" "#ff1493" ]   ;; BL
                  [ "#ffe600" "#9e9e9e" ]]  ;; BR
          
          mats (mapv imread inputs)]
      
      (if (some #(.empty %) mats)
        (println "Error: Could not read one or more inputs.")
        (let [dims (map #(clojure.core/min (.cols %) (.rows %)) mats)
              max-dim (apply clojure.core/max dims)
              tile-size (int (* max-dim scale))]

          (println "Tile size:" tile-size "x" tile-size)
          
          (let [tiles (mapv (fn [mat [bg fg]]
                              (-> mat
                                  clone
                                  (crop-to-square tile-size)
                                  (duotone fg bg)))
                            mats
                            colors)
                
                top-row (new-mat)
                bot-row (new-mat)
                full (new-mat)]
            
            (hconcat (java.util.ArrayList. [(nth tiles 0) (nth tiles 1)]) top-row)
            (hconcat (java.util.ArrayList. [(nth tiles 2) (nth tiles 3)]) bot-row)
            (vconcat (java.util.ArrayList. [top-row bot-row]) full)
            
            (imwrite full out)
            (println "🔥 Created:" out)))))))

(apply -main *command-line-args*)
