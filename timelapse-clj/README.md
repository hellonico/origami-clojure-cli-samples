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