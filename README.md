# ![android-sex-cache](static/icon.png)

# android-sex-cache [![Build Status](https://travis-ci.org/dtboy1995/android-sex-cache.svg?branch=0.0.1)](https://travis-ci.org/dtboy1995/android-sex-cache)
a file cache library that associated remote files and local files

# useful if you
- your application is rich media applications

# install
- add to your project gradle file

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}
```
- add to your module gradle file

```gradle
compile 'com.github.dtboy1995:android-sex-cache:0.0.5'
```

# usage
```java
// init once
Rl.init(context);
// debug mode
Rl.debug(true);
// put
Rl.put("remote path", "local path", true);
// get without checking local file and redownload
Rl.get("remote path");
// get with checking local file and redownload
Rl.get("remote path", new IRlStrict() {
    @Override
    public void get(String path) {

    }
});
// work in with Glide
Glide.with(context).load(Rl.get("picture path")).into(imageView);
```

# downloader

- I provide a default downloader to run *checking local file and redownload* and you can provide your downloader just extend RlDownloader abstract class
- The mehtods that must be implemented extend RlDownloader


 method | params1 | params2
:-: | :-: | :-:
start | String| IRlStrict
cancel | - | -

- Code sample

```java
// define the class extend RlDownloader
public class YourDownloader extends RlDownloader {
  
  private IRlStrict strict;

  @Override
    public void start(String remote, IRlStrict strict) {
        this.strict = strict;
        // remote is remote path
        // strict is IRlStrict instance
        // downloading
        strict.get(downloaded_path);
    }

    @Override
    public void cancel() {

    }
}
// call setter
Rl.downloader(YourDownloader.class);
```
