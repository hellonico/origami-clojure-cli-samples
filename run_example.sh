#!/bin/bash
# Run a Clojure CLI example interactively using fzf
# Usage: ./run_example.sh [script.clj]
# With no argument, uses fzf to interactively pick an example

if [ -z "$1" ]; then
  SCRIPT=$(ls *.clj | fzf \
    --prompt="Pick a Clojure Example: " \
    --height=80% \
    --border \
    --preview "awk '/^\)/{found=1; next} found{print}' {}")
  [ -z "$SCRIPT" ] && echo "No example selected." && exit 0
else
  SCRIPT=$1
fi

echo "▶ Running: $SCRIPT"
clj -M "$SCRIPT"
