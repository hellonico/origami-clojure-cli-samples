#!/bin/sh
#_(
   DEPS='
   {
 	:deps 
   { org.clojure/tools.cli {:mvn/version "1.1.230"}
   }}
   '

exec clj -Sdeps "$DEPS" -M "$0" "$@"

)

(require '[clojure.tools.cli :refer [parse-opts]])

(def cli-options
  [["-c" "--camera INDEX or EDN" "Video descriptor. Either camera index or an EDN map with properties." :default "0"]
   ["-d" "--dir DIR" "Folder where to store the pictures" :default "out"]
   ["-l" "--lapse SECONDS" "Lapse between each pictures" :default 5 :parse-fn #(Integer/parseInt %)]
   ["-h" "--help"]])

(let [{:keys [options arguments errors summary]} (parse-opts *command-line-args* cli-options)]
	(if (:help options)
	 (do (println "Usage: ./params.clj") (println summary))
	 (println "Run me with options\n" options)))