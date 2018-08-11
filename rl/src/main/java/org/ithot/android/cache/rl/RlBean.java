package org.ithot.android.cache.rl;

/**
 */

public class RlBean implements IRl {

    public String remote;
    public String local;
    public boolean is_upload;

    @Override
    public String $local() {
        return local;
    }

    @Override
    public String $remote() {
        return remote;
    }

    @Override
    public boolean $upload() {
        return is_upload;
    }
}
