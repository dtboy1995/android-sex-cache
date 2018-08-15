package org.ithot.android.cache.rl;

public abstract class RlUploaderStater implements IRlUploaderStater {

    private RlBean bean;

    RlUploaderStater(RlBean bean) {
        this.bean = bean;
    }

    abstract void succeed(RlBean bean);

    abstract void failed(RlBean bean);

    @Override
    public void done() {
        succeed(bean);
    }

    @Override
    public void undone() {
        failed(bean);
    }
}
