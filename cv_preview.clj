#!/bin/sh
#_(DEPS=' {:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}} 'exec clj -Sdeps "$DEPS" -M "$0" "$@")
(require '[opencv4.utils :as u])

(-> *command-line-args*
    first
    opencv4.core/imread
    (#(u/resize-by % 0.15))
    u/imshow)
