package com.hellom.picker.addresspicker.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Assets Database Manager
 * Use it, you can use a assets database file in you application
 * It will copy the database file to "/data/data/[your application package name]/database" when you first time you use it
 * Then you can get a SQLiteDatabase object by the assets database file
 * <p>
 * How to use:
 * 1. Initialize AssetsDatabaseManager
 * 2. Get AssetsDatabaseManager
 * 3. Get a SQLiteDatabase object through database file
 * 4. Use this database object
 * <p>
 * Using example:
 * AssetsDatabaseManager.initManager(getApplication()); // this method is only need call one time
 * AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  // get a AssetsDatabaseManager object
 * SQLiteDatabase db1 = mg.getDatabase("db1.db");  // get SQLiteDatabase object, db1.db is a file in assets folder
 * db1.??? // every operate by you want
 * Of cause, you can use AssetsDatabaseManager.getManager().getDatabase("xx") to get a database when you need use a database
 */
public class AssetsDatabaseManager {
    /**
     * for LogCat
     */
    private static String tag = "AssetsDatabase";
    private Map<String, SQLiteDatabase> databases = new HashMap<>();
    private WeakReference<Context> context;
    private static AssetsDatabaseManager mInstance = null;

    /**
     * Initialize AssetsDatabaseManager
     *
     * @param context, context of application
     */
    public static void initManager(Context context) {
        WeakReference<Context> contextWeakReference = new WeakReference<>(context);
        if (mInstance == null) {
            mInstance = new AssetsDatabaseManager(contextWeakReference);
        }
    }

    /**
     * Get a AssetsDatabaseManager object
     */
    public static AssetsDatabaseManager getManager() {
        return mInstance;
    }

    private AssetsDatabaseManager(WeakReference<Context> context) {
        this.context = context;
    }

    /**
     * Get a assets database, if this database is opened this method is only return a copy of the opened database
     */
    public SQLiteDatabase getDatabase(String dbFile) {
        if (databases.get(dbFile) != null) {
            Log.i(tag, String.format("Return a database copy of %s", dbFile));
            return databases.get(dbFile);
        }
        if (context == null) {
            return null;
        }
        Log.i(tag, String.format("Create database %s", dbFile));
        String sPath = getDatabaseFilepath();
        String sFile = getDatabaseFile(dbFile);
        File file = new File(sFile);
        SharedPreferences dbs = context.get().getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        // Get Database file flag, if true means this database file was copied and valid
        boolean flag = dbs.getBoolean(dbFile, false);
        if (!flag || !file.exists()) {
            file = new File(sPath);
            if (!file.exists() && !file.mkdirs()) {
                Log.i(tag, "Create \"" + sPath + "\" fail!");
                return null;
            }
            if (!copyAssetsToFilesystem(dbFile, sFile)) {
                Log.i(tag, String.format("Copy %s to %s fail!", dbFile, sFile));
                return null;
            }
            dbs.edit().putBoolean(dbFile, true).apply();
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(sFile, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        if (db != null) {
            databases.put(dbFile, db);
        }
        return db;
    }

    private String getDatabaseFilepath() {
        //%s is packageName
        String databasePath = context.get().getFilesDir().getPath() + "/%s/database";
        return String.format(databasePath, context.get().getApplicationInfo().packageName);
    }

    private String getDatabaseFile(String dbFile) {
        return getDatabaseFilepath() + "/" + dbFile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        Log.i(tag, "Copy " + assetsSrc + " to " + des);
        InputStream iStream = null;
        OutputStream oStream = null;
        try {
            AssetManager am = context.get().getAssets();
            iStream = am.open(assetsSrc);
            oStream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = iStream.read(buffer)) > 0) {
                oStream.write(buffer, 0, length);
            }
            iStream.close();
            oStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (iStream != null) {
                    iStream.close();
                }
                if (oStream != null) {
                    oStream.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * Close assets database
     */
    public boolean closeDatabase(String dbFile) {
        if (databases.get(dbFile) != null) {
            SQLiteDatabase db = databases.get(dbFile);
            if (db != null) {
                db.close();
            }
            databases.remove(dbFile);
            return true;
        }
        return false;
    }

    /**
     * Close all assets database
     */
    static public void closeAllDatabase() {
        Log.i(tag, "closeAllDatabase");
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); ++i) {
                if (mInstance.databases.get(String.valueOf(i)) != null) {
                    SQLiteDatabase database = mInstance.databases.get(String.valueOf(i));
                    if (database != null) {
                        database.close();
                    }
                }
            }
            mInstance.databases.clear();
        }
    }
}