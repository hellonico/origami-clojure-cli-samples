#!/bin/sh
#_(


DEPS='
{:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(require '[opencv4.core :refer :all])

(def kernel (get-structuring-element MORPH_RECT (new-size 11 11)))

(-> (or (first *command-line-args*) "resources/cat.jpg")
    (imread CV_8UC1)
    (dilate! kernel)
    (imwrite "out/kernel.png"))
