package org.ithot.cache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 */
public class RlHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "rl.db";
    private final static int VERSION = 1;

    public static final String TABLE_NAME = "TB_RL";
    public static final String LOCAL_FIELD = "local";
    public static final String REMOTE_FIELD = "remote";
    public static final String IS_UPLOAD_FIELD = "is_upload";

    private static final String TABLE_DEFINE =
                    "CREATE TABLE " + TABLE_NAME +
                    "(" +
                    " local text NOT NULL," +
                    " remote text NOT NULL," +
                    " is_upload integer NOT NULL" +
                    ");";

    private RlHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_DEFINE);
        } catch (Exception e) {
            RlLog.error(e.getMessage());
        }
    }

    // singleton
    private static class Inner {
        private static final RlHelper INSTANCE = new RlHelper(Rl.ctx());
    }

    public static final RlHelper instance() {
        return Inner.INSTANCE;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // to do
    }
}
