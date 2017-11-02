/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.ui.themefont.themes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.duy.pascal.ui.themefont.model.CodeTheme;
import com.duy.pascal.ui.themefont.themes.database.CodeThemeContract.CodeThemeEntry;
import com.duy.pascal.ui.utils.DLog;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Duy on 12-Jul-17.
 */

public class ThemeDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "themes.db";
    private static final String SQL_CREATE_TABLE = "Create table " +
            CodeThemeEntry.TABLE_NAME + "(" +
            CodeThemeEntry.NAME + " TEXT PRIMARY KEY, " +
            CodeThemeEntry.BACKGROUND + " INTEGER, " +
            CodeThemeEntry.NORMAL + " INTEGER, " +
            CodeThemeEntry.KEY_WORD + " INTEGER, " +
            CodeThemeEntry.BOOLEAN + " INTEGER, " +
            CodeThemeEntry.ERROR + " INTEGER, " +
            CodeThemeEntry.NUMBER + " INTEGER, " +
            CodeThemeEntry.OPERATOR + " INTEGER, " +
            CodeThemeEntry.COMMENT + " INTEGER, " +
            CodeThemeEntry.STRING + " INTEGER)";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + CodeThemeEntry.TABLE_NAME;
    private static final int DB_VERSION = 1;
    private static final String TAG = "ThemeDatabase";

    private static final String[] PROJECTION = new String[]{CodeThemeEntry.NAME, CodeThemeEntry.BACKGROUND,
            CodeThemeEntry.NORMAL, CodeThemeEntry.KEY_WORD, CodeThemeEntry.BOOLEAN, CodeThemeEntry.ERROR,
            CodeThemeEntry.NUMBER, CodeThemeEntry.OPERATOR, CodeThemeEntry.COMMENT, CodeThemeEntry.STRING};

    public ThemeDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public long insert(CodeTheme themeEntry) {
        DLog.d(TAG, "insert() called with: themeEntry = [" + themeEntry + "]");

        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Integer> entry : themeEntry.getColors().entrySet()) {
            contentValues.put(entry.getKey(), entry.getValue());
        }
        contentValues.put(CodeThemeEntry.NAME, themeEntry.getName());
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(CodeThemeEntry.TABLE_NAME, null, contentValues);
    }

    public boolean hasValue(@NonNull String name) {
        DLog.d(TAG, "hasValue() called with: name = [" + name + "]");

        SQLiteDatabase db = getReadableDatabase();
        Cursor query = db.query(CodeThemeEntry.TABLE_NAME, new String[]{CodeThemeEntry.NAME}, CodeThemeEntry.NAME + "=?",
                new String[]{name}, null, null, null);
        boolean r = query.moveToFirst();
        query.close();
        return r;
    }

    public ArrayList<CodeTheme> getAll() {
        DLog.d(TAG, "getAll() called");


        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CodeTheme> codeThemes = new ArrayList<>();
        Cursor cursor = db.query(CodeThemeEntry.TABLE_NAME, PROJECTION, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CodeTheme codeTheme = new CodeTheme(false);
            codeTheme.setName(cursor.getString(cursor.getColumnIndex(CodeThemeEntry.NAME)));
            codeTheme.setBackgroundColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.BACKGROUND)));
            codeTheme.setTextColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.NORMAL)));
            codeTheme.setKeyWordColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.KEY_WORD)));
            codeTheme.setBooleanColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.BOOLEAN)));
            codeTheme.setErrorColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.ERROR)));
            codeTheme.setNumberColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.NUMBER)));
            codeTheme.setOptColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.OPERATOR)));
            codeTheme.setCommentColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.COMMENT)));
            codeTheme.setStringColor(cursor.getInt(cursor.getColumnIndex(CodeThemeEntry.STRING)));
            codeThemes.add(codeTheme);
        }
        cursor.close();
        DLog.d(TAG, "getAll() returned: " + codeThemes);
        return codeThemes;
    }

    public int delete(CodeTheme codeTheme) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CodeThemeEntry.TABLE_NAME,
                CodeThemeEntry.NAME + "=?", new String[]{codeTheme.getName()});
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }


}
