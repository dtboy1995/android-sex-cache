package org.ithot.cache.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import org.ithot.android.cache.rl.IRlStrict;
import org.ithot.android.cache.rl.Rl;

public class ExampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init lib
        Rl.init(this);
        // set debug mode
        Rl.debug(true);
        // fake
        String remote = "https://wa-static-resource.oss-cn-beijing.aliyuncs.com/music/1862602494.mp3";
        Rl.put(remote, "mission.mp3", true);
        Toast.makeText(this, "fake success", Toast.LENGTH_SHORT).show();
        // look all
        Toast.makeText(this, "all rls: " + Rl.gets(Rl.ALL).toString(), Toast.LENGTH_SHORT).show();
        // get remote without checking local file and redownload
        Toast.makeText(ExampleActivity.this, String.valueOf("local: " + Rl.get(remote)), Toast.LENGTH_SHORT).show();
        // get remote with checking local file and redownload
        Rl.get(remote, new IRlStrict() {
            @Override
            public void get(String path) {
                Toast.makeText(ExampleActivity.this, String.valueOf("local: " + path), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
