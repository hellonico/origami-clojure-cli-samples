#!/bin/sh
#_(

   #_DEPS is same format as deps.edn. Multiline is okay.
   DEPS='
   {:deps {clj-time {:mvn/version "0.14.2"}}}
   '

   #_You can put other options here
   OPTS='
   -J-Xms256m -J-Xmx256m -J-client
   '

exec clj $OPTS -Sdeps "$DEPS" "$0" "$@"

)
(require '[opencv4.core :refer [VERSION]] '[opencv4.utils :as u]) 
(println "Using OpenCV Version: " VERSION "...")