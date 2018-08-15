package org.ithot.cache.test;


import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import org.ithot.android.cache.rl.Rl;
import org.ithot.android.cache.rl.RlLog;
import org.ithot.android.cache.rl.RlUploader;
import org.ithot.android.cache.rl.RlUploaderStater;

public class RlTestUploader extends RlUploader {

    @Override
    public void upload(String remote, String local, RlUploaderStater stater) {
        // synchronizing upload
        OSS oss = OSSProvider.instance().obtain(Rl.ctx());
        PutObjectRequest put = new PutObjectRequest(
                "ithot",
                remote,
                local);
        try {
            oss.putObject(put);
        } catch (Exception e) {
            RlLog.error(e.getMessage());
        }
    }
}
