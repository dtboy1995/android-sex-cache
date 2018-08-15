package org.ithot.android.cache.rl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */

public class RlBean implements IRl, Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remote);
        dest.writeString(this.local);
        dest.writeByte(this.is_upload ? (byte) 1 : (byte) 0);
    }

    public RlBean() {
    }

    protected RlBean(Parcel in) {
        this.remote = in.readString();
        this.local = in.readString();
        this.is_upload = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RlBean> CREATOR = new Parcelable.Creator<RlBean>() {
        @Override
        public RlBean createFromParcel(Parcel source) {
            return new RlBean(source);
        }

        @Override
        public RlBean[] newArray(int size) {
            return new RlBean[size];
        }
    };
}
