#!/bin/sh
#_(
exec clj -M "$0" "$@"
)

(println "This is the location of the file that was used for this script:\n"*file*)