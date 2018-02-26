package org.ithot.cache.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.ithot.cache.IRlStrict;
import org.ithot.cache.Rl;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init lib
        Rl.init(this);
        // set debug mode
        Rl.debug(true);
        // mook
        String remote = "https://wa-static-resource.oss-cn-beijing.aliyuncs.com/music/1862602494.mp3";
        Rl.put(remote, "mission.mp3", true);
        Toast.makeText(this, "mook success", Toast.LENGTH_SHORT).show();
        // look all
        Toast.makeText(this, "" + Rl.gets(Rl.ALL).toString(), Toast.LENGTH_SHORT).show();
        // get remote without checking local file and redownload
        Toast.makeText(MainActivity.this, Rl.get(remote) + "", Toast.LENGTH_SHORT).show();
        // get remote with checking local file and redownload
        Rl.get(remote, new IRlStrict() {
            @Override
            public void get(String path) {
                Toast.makeText(MainActivity.this, path + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
