# ![android-sex-cache](static/icon.png)

# android-sex-cache [![Build Status](https://travis-ci.org/dtboy1995/android-sex-cache.svg?branch=0.0.1)](https://travis-ci.org/dtboy1995/android-sex-cache)
:sunrise_over_mountains: a library that associated remote files and local files

# install
```gradle
implementation 'org.ithot.android.cache:rl:0.0.5'
```

# usage
- ### permission (note the runtime permissions after Android6.0 (included))
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
```
- ### sample
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

- ### custom downloader
```java
public class YourDownloader extends RlDownloader {

  private IRlStrict strict;

  @Override
    public void start(String remote, IRlStrict strict) {
        this.strict = strict;
        // asynchronization code
        strict.get(finished_path);
    }

    @Override
    public void cancel() {
        // cancel download resource
    }
}
// call setter
Rl.downloader(YourDownloader.class);
```

- ### custom uploader
```java
public class YourUploader extends RlUploader {
    @Override
    public void upload(String remote, String local, RlUploaderStater stater) {
        // synchronizing upload (because IntentService is used to upload files queue)
        // upload succeed
        stater.done();
        // upload failed
        stater.undone();
    }
}
// call setter
Rl.uploader(YourUploader.class);
```
