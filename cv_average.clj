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

(defn average-color [src]
  (let [mean_ (-> src u/mat-from (set-to! (mean src)))]
    (-> src
        (u/mat-from)
        (set-to! (mean src))
        (cons [src])
        (vconcat!))))

(-> (first *command-line-args*)
    (u/mat-from-url)
    (u/resize-by 0.25)
    average-color
    (imwrite "out/average.jpg"))
