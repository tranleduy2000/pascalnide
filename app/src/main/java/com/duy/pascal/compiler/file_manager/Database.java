package com.duy.pascal.compiler.file_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * SQ lite database for pascal compiler
 * include history, variable, ...
 * Created by Duy on 3/7/2016
 */
public class Database extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "db_manager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_FILE_HISTORY = "tbl_file_history";
    private static final String KEY_FILE_PATH = "path";


    public static final String CREATE_TABLE_FILE_HISTORY =
            "create table " + TABLE_FILE_HISTORY +
                    "(" +
                    KEY_FILE_PATH + " TEXT PRIMARY KEY" +
                    ")";

    private String TAG = Database.class.getName();

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FILE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILE_HISTORY);
        onCreate(db);
    }

    public ArrayList<File> getListFile() {
        ArrayList<File> files = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_FILE_HISTORY;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String result = cursor.getString(cursor.getColumnIndex(KEY_FILE_PATH));
                File file = new File(result);
                files.add(file);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return files;
    }

    public long addNewFile(File file) {
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_FILE_PATH, file.getPath());
            return sqLiteDatabase.insert(TABLE_FILE_HISTORY, null, contentValues);
        } catch (Exception e) {
            return -1;
        }
    }

    public long addNewFile(String file) {
        return addNewFile(new File(file));
    }

}
