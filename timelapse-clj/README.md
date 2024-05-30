# Timelapse

The optimal lapse time for creating a time-lapse video depends on the subject and the effect you want to achieve. Here are some general guidelines for different scenarios:

    Sunrise/Sunset:
        Lapse time: 2-10 seconds
        Capture the gradual change in lighting and colors over 30-60 minutes.
    
    Cloud Movement:
        Lapse time: 1-10 seconds
        Depending on the speed of the clouds, a faster lapse time (1-3 seconds) will work for fast-moving clouds, while a slower lapse time (5-10 seconds) is better for slow-moving clouds.
    
    Stars/Astronomy:
        Lapse time: 15-60 seconds
        This allows you to capture the movement of stars over several hours.
    
    Cityscapes/Traffic:
        Lapse time: 1-5 seconds
        For busy city streets, a lapse time of 1-2 seconds captures the hustle and bustle, while 3-5 seconds is better for slower traffic.
    
    Plant Growth:
        Lapse time: 10-30 minutes
        Depending on the plant and the growth rate, you might need to capture over days or weeks.
    
    Construction Projects:
        Lapse time: 10-60 minutes
        For long-term projects, capturing frames every 10-60 minutes over weeks or months will show progress effectively.
    
    Crowd Movement (Events):
        Lapse time: 1-5 seconds
        Capture the flow and dynamics of large groups of people during events or festivals.

Calculation Example

To ensure a smooth video, you generally need 24-30 frames per second (fps). For a 10-second time-lapse video at 30 fps, you need 300 frames:

    Duration of the Event: Suppose you want to capture a 2-hour event.
    Total Frames Needed: 300 frames.
    Lapse Time: 2 hours = 7200 seconds / 300 frames ≈ 24 seconds per frame.

Tips

    Test and Adjust: It’s often necessary to experiment with different lapse times to get the desired effect.
    Manual Settings: Use manual exposure and focus settings to avoid fluctuations in the time-lapse video.
    Stability: Use a tripod or a stable mount to keep the camera steady.

By adjusting the lapse time based on your subject and the desired duration of the final video, you can create compelling and dynamic time-lapse videos.


# Install steps
- install java

- install clj

_on linux_

```bash
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
chmod +x linux-install.sh
sudo ./linux-install.sh
```

_on osx_
```bash
brew install clojure
```


# Run

```bash
Usage: timelapse.clj <cam-index | cam-settings> <folder> <lapse-in-second>
```

_The script below:_

-  uses first camera with the given resolution, 
- stores pictures in the out folder
- stores pictures as png
- wait 10s between shots
- uses "yyyy_MM_dd_HH_mm_ss" to format the timestamps

```bash
./timelapse.clj\
 -c "{:device 0 :frame-width 3840 :frame-height 2160 :exposure -10}"\
 -d out\ 
 -f png\ 
 -l 10\ 
 -o "yyyy_MM_dd_HH_mm_ss" 
```