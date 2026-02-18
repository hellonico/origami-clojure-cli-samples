#!/bin/sh
#_(


DEPS='
{:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}}'

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)
(require '[opencv4.core :refer [imread imwrite CV_8UC1]])
(require '[opencv4.process :as p])
(require '[clojure.java.io :as io])

(defn process [out file]
  (-> file
      (imread CV_8UC1)
      (imwrite (str out "/bw_" (.getName (io/as-file file))))))

(p/parallel
 (or (first *command-line-args*) ".")
 (or (second *command-line-args*) "out")
 process)
