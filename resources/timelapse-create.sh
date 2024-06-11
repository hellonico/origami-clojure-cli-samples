#!/bin/bash
if [ -z ${PATTERN} ]; then
  export PATTERN="test2/%7d.png"
fi

if [ -z ${AUDIO} ]; then
  export AUDIO=audio.mp3
fi

if [ -z ${OUTPUT} ]; then
  export OUTPUT=my-timelapse.mp4
fi

echo "Using PATTERN $PATTERN"
echo "Using audio $AUDIO"
echo "Using output $OUTPUT"

ffmpeg -framerate 1 -r 10 -y -loop 1 -pattern_type glob -i "$PATTERN" -i $AUDIO -s:v 1440x1080 -c:v libx264 -pix_fmt yuv420p -c:a aac -shortest $OUTPUT

# REFERENCES
# ffmpeg -framerate 20 -pattern_type glob -i "test2/*.png" -s:v 1440x1080 -c:v libx264 -crf 17 -pix_fmt yuv420p my-timelapse.mp4
# ffmpeg -framerate 20 -pattern_type glob -i "test2/*.png" -i audio.mp3 -s:v 1440x1080 -c:v libx264 -r 30 -pix_fmt yuv420p -c:a aac -shortest my-timelapse.mp4
# export PATTERN="test2/*.png" ; export AUDIO=audio.mp3 ; export OUTPUT=timelapse.mp4 ; ./timelapse-create.sh 