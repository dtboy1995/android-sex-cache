package org.ithot.android.cache.rl;

/**
 * downloader interface
 */

public abstract class RlDownloader {

    public abstract void start(String remote, IRlStrict strict);

    public abstract void cancel();
}
