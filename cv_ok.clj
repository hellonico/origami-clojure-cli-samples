#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
 	:deps 
   { origami/origami {:mvn/version "4.9.0-8"}
     org.clojure/clojure {:mvn/version "1.11.3"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)

(require '[opencv4.core :refer [VERSION]]) 
(println "Using OpenCV Version: " VERSION "...")