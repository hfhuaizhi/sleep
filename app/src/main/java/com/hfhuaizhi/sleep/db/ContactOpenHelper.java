package com.hfhuaizhi.sleep.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fullcircle on 2017/1/3.
 */

public class ContactOpenHelper extends SQLiteOpenHelper {
    public ContactOpenHelper(Context context) {
        super(context, "sleep.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table contact_info(_id integer primary key autoincrement,username varchar(20),contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
