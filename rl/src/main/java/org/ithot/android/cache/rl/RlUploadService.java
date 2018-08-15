package org.ithot.android.cache.rl;

import android.app.IntentService;
import android.content.Intent;

import static org.ithot.android.cache.rl.RlUploadScheduler.LOCAL;

public class RlUploadService extends IntentService {

    public RlUploadService() {
        super("RlUploadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RlBean bean = intent.getParcelableExtra(LOCAL);
        try {
            RlUploader uploader = Rl.$uploader.newInstance();
            uploader.upload(bean.$remote(), bean.$local(), new RlUploaderStater(bean) {
                @Override
                void succeed(RlBean bean) {
                    RlLog.debug(bean.$remote() + " succeed");
                    Rl.modify(bean.$remote(), bean.$local(), true);
                }

                @Override
                void failed(RlBean bean) {
                    RlLog.debug(bean.$remote() + " failed");
                    Rl.modify(bean.$remote(), bean.$local(), false);
                }
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RlLog.debug("service destroy");
    }
}
