#!/bin/sh
#_(DEPS=' {:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}
            origami/filters {:mvn/version "1.48"}}} 'exec clj -Sdeps "$DEPS" -M "$0" "$@")
(require '[opencv4.utils :as u])

(defn java-filter [klass]
  (let [fi (.newInstance klass)]
    (fn [mat] (.apply fi mat))))

(def f (java-filter origami.filters.Cartoon))
; Other filters to try:
; (def f (java-filter origami.filters.SunGlasses$Red))
; (def f (java-filter origami.filters.Manga))

(u/simple-cam-window
 (comp (java-filter origami.filters.FPS) f))
