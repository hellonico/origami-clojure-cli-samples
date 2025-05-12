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
     clojure.java-time/clojure.java-time {:mvn/version "1.4.2"}
     org.clojure/tools.cli {:mvn/version "1.1.230"}
    }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"
)

(require '[opencv4.utils :as u])

(def fi (origami.filters.Annotate.))
(.setText fi "")

(u/simple-cam-window fi)

; TODO: move to core
(defn set-interval [callback ms] 
  (future (while true (do (Thread/sleep ms) (callback)))))

(def job (set-interval (fn[] (.setText fi (str (java.util.Date.)))) 1000))