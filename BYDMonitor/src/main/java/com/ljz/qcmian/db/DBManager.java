package com.ljz.qcmian.db;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.os.Build;

import com.ljz.qcmian.encrypt.RSAEncrypt;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class DBManager {
    private static final int VERSION = 1;
    private static final String DB_NAME = "main_data.db";
    private static final String TABLE_NAME = "lochis";
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER, content TEXt);";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME + ";";
    private Context mContext;
    private DBHelper mDBHelper;


    private static class SingletonHolder {
        private static final DBManager INSTANCE = new DBManager();
    }

    public static DBManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;
        SQLiteDatabase.loadLibs(mContext);
        mDBHelper = new DBHelper(mContext);
    }

    private DBManager() {
    }

    public SQLiteDatabase getDB() {
        return mDBHelper.getWritableDatabase(RSAEncrypt.dbKey);
    }

    public void insertLocation(Location location) {
        if (location == null) {
            return;
        }
        SQLiteDatabase db = getDB();
        ContentValues values = new ContentValues();
        values.put("id", location.getTime());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("provider", location.getProvider());
            jsonObject.put("latitude", location.getLatitude());
            jsonObject.put("longitude", location.getLongitude());
            jsonObject.put("altitude", location.getAltitude());
            jsonObject.put("horizontalAccuracy", location.getAccuracy());
            jsonObject.put("speed", location.getSpeed());
            jsonObject.put("bearing", location.getBearing());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                jsonObject.put("ElapsedRealtime", location.getElapsedRealtimeNanos());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                jsonObject.put("mock", location.isFromMockProvider());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                jsonObject.put("verticalAccuracy", location.getVerticalAccuracyMeters());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        values.put("content", jsonObject.toString());
        db.insert(TABLE_NAME, null, values);
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_TABLE);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
            db.execSQL(DROP_TABLE);
        }
    }
}
