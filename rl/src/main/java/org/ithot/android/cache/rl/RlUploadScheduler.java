package org.ithot.android.cache.rl;

import android.content.Intent;

import java.util.List;

import static org.ithot.android.cache.rl.Rl.UN_UPLOAD;

public class RlUploadScheduler {

    static final String LOCAL = "local";

    synchronized static void dispatch() {
        List<IRl> locals = Rl.gets(UN_UPLOAD);
        RlLog.debug( "un upload count: "+locals.size());
        if (locals.size() == 0) return;
        for (int i = 0; i < locals.size(); i++) {
            IRl irl = locals.get(i);
            RlBean rl = new RlBean();
            rl.is_upload = irl.$upload();
            rl.local = irl.$local();
            rl.remote = irl.$remote();
            Intent intent = new Intent(Rl.ctx(), RlUploadService.class);
            intent.putExtra(LOCAL, rl);
            Rl.ctx().startService(intent);
        }
    }
}
