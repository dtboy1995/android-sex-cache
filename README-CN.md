# ![android-sex-cache](static/icon.png)

# android-sex-cache [![Build Status](https://travis-ci.org/dtboy1995/android-sex-cache.svg?branch=0.0.1)](https://travis-ci.org/dtboy1995/android-sex-cache)
:sunrise_over_mountains: 一个关联本地文件和远程文件的库

# 安装
```gradle
implementation 'org.ithot.android.cache:rl:0.0.6'
```

# 使用
- ### 权限 (请注意Android6.0及以上的运行时权限)
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.INTERNET" />
```
- ### 例子
```java
// 全局初始化一次
Rl.init(context);
// 开启日志
Rl.debug(true);
// 添加一个映射 (第三个参数 true false 该远程标所对应的文件是不是已经上传成功了,可以和下面的自定义上传器配合)
Rl.put("remote path", "local path", true);
// 根据远程标识获取本地路径(失效不重新下载)
Rl.get("remote path");
// 根据远程标识获取本地路径(失效的时候重新下载)
Rl.get("remote path", new IRlStrict() {
    @Override
    public void get(String path) {

    }
});
// 和Glide配合使用
Glide.with(context).load(Rl.get("picture path")).into(imageView);
```

- ### 自定义下载器
```java
public class YourDownloader extends RlDownloader {

  private IRlStrict strict;

    @Override
    public void start(String remote, IRlStrict strict) {
        this.strict = strict;
        // 您去异步加载文件，下载成功后调用下这个函数
        strict.get(finished_path);
    }

    @Override
    public void cancel() {
        // 取消下载的代码您写在这里
    }
}
// 设置一下您自己编写的下载器 当本地文件不存在时，可以下载远程文件并和远程标识关联
Rl.downloader(YourDownloader.class);
```

- ### 自定义上传器 (用户批量添加文件的场景，为了用户体验，后台上传文件...)
```java
public class YourUploader extends RlUploader {
    @Override
    public void upload(String remote, String local, RlUploaderStater stater) {
        // 这里应该写成同步的上传代码 (因为内部使用了IntentService去搞的)
        // 上传成功您调用下这个函数
        stater.done();
        // 上传失败您调用下这个函数
        stater.undone();
    }
}
// 设置一下您自己编写的上传器
Rl.uploader(YourUploader.class);
// 选一个时机，开始您文件的批量上传
Rl.uploading();
```
