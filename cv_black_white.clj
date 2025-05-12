#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:mvn/repos
   {"vendredi" {:url "https://repository.hellonico.info/repository/hellonico/"}}
  :deps 
   { origami/origami {:mvn/version "4.11.0-6"}
     org.clojure/clojure {:mvn/version "1.11.3"}
     origami/filters {:mvn/version "1.49"}
     clojure.java-time {:mvn/version "1.4.2"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)

(require '[opencv4.core :as cv]) 

(defn bw[args]
 (println args)
	(->  args
	 (cv/imread) 
 	(cv/cvt-color! cv/COLOR_RGB2GRAY)
 	(cv/imwrite (str args "_bw.png"))))

(if (not *command-line-args*)
	(println 
		"\nTurn an image to black and white."
		"\nUsage: bw.clj <inputfile>")
	(bw (first *command-line-args*)))