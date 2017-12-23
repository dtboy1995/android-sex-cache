package org.ithot.cache;

import android.util.Log;

/**
 */

public final class RlLog {

    public static final String TAG = "[android-sex-cache]";

    public static void debug(String msg) {
        if (Rl.$debug) Log.d(TAG, msg + "");
    }

    public static void error(String msg) {
        if (Rl.$debug) Log.e(TAG, msg + "");
    }
}
