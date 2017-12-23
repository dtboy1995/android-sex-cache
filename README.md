# ![android-sex-cache](static/icon.png)

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
compile 'com.github.dtboy1995:android-sex-cache:0.0.1'
```

# usage
```java
// init once
Rl.init(getApplicationContext())
// put
Rl.put("remote path", "local path", true);
// get
Rl.get("remote path");
```
