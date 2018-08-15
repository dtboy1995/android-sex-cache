package org.ithot.cache.test;


import android.content.Context;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

public final class OSSProvider {

    private OSSProvider() {

    }

    public static OSSProvider instance() {
        return Inner.provider;
    }

    private OSS oss;

    public OSS obtain(Context ctx) {
        if (oss == null) {
            OSSCredentialProvider provider = new OSSPlainTextAKSKCredentialProvider(
                    "REQUEST IN LOCAL",
                    "REQUEST IN LOCAL");
            oss = new OSSClient(ctx, "http://oss-cn-beijing.aliyuncs.com", provider);
        }
        OSSLog.enableLog();
        return oss;
    }

    static class Inner {
        static OSSProvider provider = new OSSProvider();
    }

}
