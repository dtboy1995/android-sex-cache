# ![android-sex-cache](static/icon.png)

a file cache library that associated remote files and local files

# useful if you
- your application is rich media applications

# usage
```java
// init once
Rl.init(getApplicationContext())
// put
Rl.put("remote path", "local path", true);
// get
Rl.get("remote path");
```
