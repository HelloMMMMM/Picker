package com.hellom.picker.addresspicker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBUtils {
    private static SQLiteDatabase db;
    /**
     * 版本号
     */
    private static final int VERSION = 1;
    /**
     * 数据库名
     */
    private static final String DB_NAME = "address.db";

    private DBUtils(Context context) {
        DBOpenHelper dbHelper = new DBOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getReadableDatabase();

    }

    public static SQLiteDatabase getDBInstance(Context context) {
        if (db == null) {
            new DBUtils(context);
        }
        return db;
    }
}
