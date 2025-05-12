#
sed -i '' 's#origami/origami {:mvn/version "4\.[^"]*"}#origami/origami {:mvn/version "4.11.0-3"}#' *.clj

sed -i '' 's#origami/filters {:mvn/version "1\.[^"]*"}#origami/filters {:mvn/version "1.49"}#' *.clj
