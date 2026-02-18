#!/bin/sh
#_(DEPS=' {:mvn/repos
           {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
           :deps
           {origami/origami {:mvn/version "4.13.0-2-SNAPSHOT"}}} 'exec clj -Sdeps "$DEPS" -M "$0" "$@")
(require '[opencv4.core :refer [CV_8UC1 dump]])
(import '[org.opencv.core Mat])

(dump (Mat/eye 3 3 CV_8UC1))
