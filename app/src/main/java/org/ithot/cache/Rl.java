package org.ithot.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */

public class Rl {

    // remote local key-value
    private static Map<String, String> $memory = Collections.synchronizedMap(new HashMap<String, String>());

    public static boolean $debug = false;

    public static final int ALL = 0;
    public static final int UN_UPLOAD = 1;
    public static final int UPLOADED = 2;

    private static Context $ctx;

    private Rl() {

    }

    public static String get(String remote) {
        return get(remote, true);
    }

    /**
     * @param remote
     * @param fs     exist in file system
     * @return
     */
    public static String get(String remote, boolean fs) {
        String local = $memory.get(remote);
        if (local == null) {
            return remote;
        } else {
            if (fs) {
                if (new File(local).exists()) {
                    return local;
                } else {
                    return remote;
                }
            } else {
                return local;
            }
        }
    }

    public static String get(String remote, IRlFormat format) {
        String local = $memory.get(remote);
        if (format != null)
            return format.$format(remote, local);
        return remote;
    }


    public static Context ctx() {
        return $ctx;
    }

    public static void debug(boolean d) {
        $debug = d;
    }

    /**
     * @param remote
     * @param fs     exist in file system
     * @return
     */
    public static boolean exist(String remote, boolean fs) {
        boolean status = false;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT remote,local FROM " + RlHelper.TABLE_NAME + " WHERE remote=?", new String[]{remote});
        while (cursor.moveToNext()) {
            status = true;
            if (fs) {
                status = new File(cursor.getString(cursor.getColumnIndex(RlHelper.LOCAL_FIELD))).exists();
            }
        }
        cursor.close();
        db.close();
        return status;
    }

    public static boolean exist(String remote) {
        return exist(remote, false);
    }

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

    public static void remove(String remote) {
        // if not contains the remote key return directly
        if (!$memory.containsKey(remote)) return;
        SQLiteDatabase db = RlHelper.instance().getWritableDatabase();
        db.delete(RlHelper.TABLE_NAME, "remote=?", new String[]{remote});
        db.close();
        $memory.remove(remote);
    }

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

    public static void init(Context ctx) {
        $ctx = ctx.getApplicationContext();
        List<IRl> protocols = gets(ALL);
        if (protocols.size() == 0) return;
        for (IRl protocol : protocols) {
            // sync memory
            $memory.put(protocol.$remote(), protocol.$local());
        }
    }

}
