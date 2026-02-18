#!/bin/bash
# Bump origami/origami and origami/filters versions in all .clj scripts
# Usage: ./bump.sh [origami-version] [filters-version]
# Example: ./bump.sh 4.13.0-2-SNAPSHOT 1.48

ORIGAMI_VERSION=${1:-4.13.0-2-SNAPSHOT}
FILTERS_VERSION=${2:-1.48}

echo "Bumping origami/origami -> $ORIGAMI_VERSION"
echo "Bumping origami/filters -> $FILTERS_VERSION"

sed -i '' "s|origami/origami[[:space:]]*{:mvn/version \"[^\"]*\"}|origami/origami {:mvn/version \"$ORIGAMI_VERSION\"}|g" *.clj
sed -i '' "s|origami/filters[[:space:]]*{:mvn/version \"[^\"]*\"}|origami/filters {:mvn/version \"$FILTERS_VERSION\"}|g" *.clj

echo "Done. Updated files:"
grep -l "origami/origami\|origami/filters" *.clj
