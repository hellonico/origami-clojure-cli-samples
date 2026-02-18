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
(require '[opencv4.utils :as u])

(def in (or (first *command-line-args*) "resources/cat.jpg"))

(-> in
    imread
    (cvt-color! COLOR_BGR2GRAY)
    (gaussian-blur! (new-size 1 1) 1 1)
    (imwrite (str "out/cartoon_" (last (clojure.string/split in #"/")))))
