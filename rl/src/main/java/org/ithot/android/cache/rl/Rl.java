package org.ithot.android.cache.rl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 */

public class Rl {

    // remote local key-value
    private static Map<String, String> $memory = new ConcurrentHashMap<>();
    // debug model
    public static boolean $debug = false;
    // get model
    public static final int ALL = 0;
    public static final int UN_UPLOAD = 1;
    public static final int UPLOADED = 2;
    // application context
    private static Context $ctx;
    // default downloader
    private static Class<? extends RlDownloader> $downloader = RlDefaultDownloader.class;
    // uploader
    static Class<? extends RlUploader> $uploader;

    // downloader setter
    public static void downloader(Class<? extends RlDownloader> downloader) {
        $downloader = downloader;
    }

    // uploader setter
    public static void uploader(Class<? extends RlUploader> uploader) {
        $uploader = uploader;
    }

    // get context
    public static Context ctx() {
        return $ctx;
    }

    // set debug model
    public static void debug(boolean d) {
        $debug = d;
    }

    // not pass new to get instance
    private Rl() {

    }

    // get memory instance
    public static Map<String, String> memory() {
        return $memory;
    }

    // pass remote key get local if local file miss return remote ignore local file losing.
    public static String get(String remote) {
        String local = $memory.get(remote);
        if (local == null) {
            return remote;
        } else {
            if (new File(local).exists()) {
                return local;
            } else {
                // remove the ugly data
                remove(remote);
                return remote;
            }
        }
    }

    // get strict data
    public static void get(String remote, IRlStrict strict) {
        String local = $memory.get(remote);
        if (local != null && new File(local).exists()) {
            strict.get(local);
        } else {
            // remove the ugly data
            remove(remote);
            RlDownloader _downloader = null;
            try {
                _downloader = $downloader.newInstance();
                _downloader.start(remote, strict);
            } catch (InstantiationException e) {
                if (_downloader != null) {
                    _downloader.cancel();
                }
                strict.get(null);
            } catch (IllegalAccessException e) {
                if (_downloader != null) {
                    _downloader.cancel();
                }
                strict.get(null);
            }
        }
    }

    // remote with local real exist
    public static boolean exist(String remote) {
        String local = $memory.get(remote);
        if (local == null) return false;
        if (new File(local).exists()) {
            return true;
        } else {
            remove(remote);
            return false;
        }
    }

    // pass mode to return all data
    public static List<IRl> gets(int mode) {
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        String sql = "SELECT remote,local,is_upload FROM " + RlHelper.TABLE_NAME;
        switch (mode) {
            case UN_UPLOAD:
                sql += " WHERE is_upload=0";
                break;
            case UPLOADED:
                sql += " WHERE is_upload!=0";
                break;
        }
        Cursor cursor = db.rawQuery(sql, null);
        List<IRl> protocols = new ArrayList<>();
        while (cursor.moveToNext()) {
            RlBean bean = new RlBean();
            bean.remote = cursor.getString(cursor.getColumnIndex(RlHelper.REMOTE_FIELD));
            bean.local = cursor.getString(cursor.getColumnIndex(RlHelper.LOCAL_FIELD));
            bean.is_upload = cursor.getInt(cursor.getColumnIndex(RlHelper.IS_UPLOAD_FIELD)) != 0;
            protocols.add(bean);
        }
        cursor.close();
        db.close();
        return protocols;
    }

    // put list
    public static void puts(List<IRl> protocols) {
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues kvs = new ContentValues();
            for (IRl protocol : protocols) {
                if (!$memory.containsKey(protocol.$remote())) {
                    kvs.put(RlHelper.REMOTE_FIELD, protocol.$remote());
                    kvs.put(RlHelper.LOCAL_FIELD, protocol.$local());
                    kvs.put(RlHelper.IS_UPLOAD_FIELD, protocol.$upload() ? 1 : 0);
                    db.insert(RlHelper.TABLE_NAME, null, kvs);
                    kvs.clear();
                }
            }
            db.setTransactionSuccessful();
            //  sync memory
            for (IRl protocol : protocols) {
                $memory.put(protocol.$remote(), protocol.$local());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    // modify data
    public static void modify(String remote, String local, boolean is_upload) {
        // if not contains the remote key return directly
        if (!$memory.containsKey(remote)) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        ContentValues kvs = new ContentValues();
        kvs.put(RlHelper.REMOTE_FIELD, remote);
        kvs.put(RlHelper.LOCAL_FIELD, local);
        kvs.put(RlHelper.IS_UPLOAD_FIELD, is_upload ? 1 : 0);
        db.update(RlHelper.TABLE_NAME, kvs, "remote=?", new String[]{remote});
        db.close();
        $memory.put(remote, local);
    }

    // modify data
    public static void modify(IRl protocol) {
        // if not contains the remote key return directly
        if (!$memory.containsKey(protocol.$remote())) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        ContentValues kvs = new ContentValues();
        kvs.put(RlHelper.REMOTE_FIELD, protocol.$remote());
        kvs.put(RlHelper.LOCAL_FIELD, protocol.$local());
        kvs.put(RlHelper.IS_UPLOAD_FIELD, protocol.$upload() ? 1 : 0);
        db.update(RlHelper.TABLE_NAME, kvs, "remote=?", new String[]{protocol.$remote()});
        db.close();
        $memory.put(protocol.$remote(), protocol.$local());
    }

    // remove data
    public static void remove(String remote) {
        // if not contains the remote key return directly
        if (!$memory.containsKey(remote)) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        db.delete(RlHelper.TABLE_NAME, "remote=?", new String[]{remote});
        db.close();
        $memory.remove(remote);
    }

    // remove all data
    public static void clear() {
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        db.delete(RlHelper.TABLE_NAME, null, null);
        db.close();
        $memory.clear();
    }

    // put single
    public static void put(String remote, String local, boolean is_upload) {
        // if had contains the remote key return directly
        if ($memory.containsKey(remote)) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        ContentValues kvs = new ContentValues();
        kvs.put(RlHelper.REMOTE_FIELD, remote);
        kvs.put(RlHelper.LOCAL_FIELD, local);
        kvs.put(RlHelper.IS_UPLOAD_FIELD, is_upload ? 1 : 0);
        db.insert(RlHelper.TABLE_NAME, null, kvs);
        kvs.clear();
        db.close();
        $memory.put(remote, local);
    }

    // put single
    public static void put(IRl protocol) {
        // if had contains the remote key return directly
        if ($memory.containsKey(protocol.$remote())) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        ContentValues kvs = new ContentValues();
        kvs.put(RlHelper.REMOTE_FIELD, protocol.$remote());
        kvs.put(RlHelper.LOCAL_FIELD, protocol.$local());
        kvs.put(RlHelper.IS_UPLOAD_FIELD, protocol.$upload());
        db.insert(RlHelper.TABLE_NAME, null, kvs);
        kvs.clear();
        db.close();
        $memory.put(protocol.$remote(), protocol.$local());
    }

    // init the lib
    public static void init(Context ctx) {
        $ctx = ctx;
        List<IRl> protocols = gets(ALL);
        $memory.clear();
        if (protocols.size() == 0) return;
        for (IRl protocol : protocols) {
            // sync memory
            $memory.put(protocol.$remote(), protocol.$local());
        }
    }

    // new feature
    public static void uploading() {
        if ($uploader == null) {
            RlLog.error("uploader can not be null.");
            return;
        }
        if (Rl.ctx() == null) {
            RlLog.error("context can not be null.");
            return;
        }

        RlUploadScheduler.dispatch();
    }
}
