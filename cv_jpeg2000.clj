#!/bin/sh
#_(


DEPS='
{:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(require '[opencv4.core :refer [imread imwrite]])

(-> (or (first *command-line-args*) "resources/cat.jpg")
    imread
    (imwrite "out/output.hdp"))
