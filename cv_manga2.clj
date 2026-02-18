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

(let [in   (first *command-line-args*)
      fd   (clojure.java.io/as-file in)
      out  (str "out/bw_" (.getName fd))]
  (println in ">" out)
  (-> in
      imread
      (cvt-color! COLOR_RGB2GRAY)
      (gaussian-blur! (new-size 7 7) 1.5 1.5)
      (threshold! 100 255 THRESH_BINARY)
      (imwrite out)))
