package org.ithot.cache.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ithot.android.cache.rl.IRl;
import org.ithot.android.cache.rl.Rl;

import java.util.ArrayList;
import java.util.List;

public class ExampleActivity extends Activity {

    ListView lv;
    List<IRl> rls = new ArrayList<>();

    class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return rls.size();
        }

        @Override
        public IRl getItem(int i) {
            return rls.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.item, viewGroup, false);
            TextView tvRemote = view.findViewById(R.id.tv_remote);
            TextView tvLocal = view.findViewById(R.id.tv_local);
            TextView tvUpload = view.findViewById(R.id.tv_upload);
            IRl irl = getItem(i);
            tvRemote.setText(irl.$remote());
            tvLocal.setText(irl.$local());
            tvUpload.setText(irl.$upload() + "");
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = findViewById(R.id.lv);
        lv.setAdapter(new Adapter());
        // init lib
        Rl.init(this);
        // set debug mode
        Rl.debug(true);
        /*
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
        */
        // test upload scheduler
        /*
        Rl.put("remote1", "local1", false);
        Rl.put("remote2", "local2", false);
        Rl.put("remote3", "local3", false);
        Rl.put("remote4", "local4", false);

        */
        // Toast.makeText(this, Rl.gets(Rl.ALL).toString(), Toast.LENGTH_SHORT).show();
        // Rl.remove("/images/test");
        Rl.put("images/test", "/storage/emulated/0/tencent/micromsg/weixin/mmexport1534168613092.jpg", false);;
        rls = Rl.gets(Rl.ALL);
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();


        Rl.uploader(RlTestUploader.class);
        Rl.uploading();

//        RlLog.error(new File().exists() + "");

    }
}
