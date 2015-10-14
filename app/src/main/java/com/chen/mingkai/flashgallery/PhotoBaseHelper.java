package com.chen.mingkai.flashgallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chen.mingkai.flashgallery.PhotoDbSchema.PhotoTable;

public class PhotoBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "photoBase.db";

    public PhotoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PhotoTable.NAME +
                        "( _id integer primary key autoincrement, " +
                        PhotoTable.Cols.UUID + ", " +
                        PhotoTable.Cols.Title + ", " +
                        PhotoTable.Cols.Date + ", " +
                        PhotoTable.Cols.Description +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
