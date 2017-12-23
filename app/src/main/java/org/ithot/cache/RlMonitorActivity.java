package org.ithot.cache;

import android.app.Activity;
import android.os.Bundle;

public class RlMonitorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Rl.init(getApplicationContext());
        Rl.put("remote path", "local path", true);
        Rl.get("remote path");
    }
}
