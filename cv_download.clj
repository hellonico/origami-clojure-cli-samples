#!/bin/sh
#_(DEPS=' {:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}} 'exec clj -Sdeps "$DEPS" -M "$0" "$@")
(require '[opencv4.utils :as u])

; Download an image from a URL and save it locally
(-> (first *command-line-args*)
    (u/mat-from-url)
    (opencv4.core/imwrite "out/downloaded.jpg"))
