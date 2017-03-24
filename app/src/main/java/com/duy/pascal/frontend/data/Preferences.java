package com.duy.pascal.frontend.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Setting for application
 * <p>
 * Created by Duy on 3/7/2016
 */
public class Preferences {
    public static final String FILE_PATH = "last_file";
    public static final String LAST_FIND = "LAST_FIND";
    public static final String LAST_REPLACE = "LAST_REPLACE";
    public static final String TAB_POSITION_FILE = "TAB_POSITION_FILE";
    protected SharedPreferences.Editor editor;
    protected Context context;
    private SharedPreferences sharedPreferences;

    public Preferences(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    public Preferences(SharedPreferences mPreferences, Context context) {
        this.context = context;
        this.sharedPreferences = mPreferences;
        this.editor = sharedPreferences.edit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void put(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public void put(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * get long value from key,
     *
     * @param key - key
     * @return -1 if not found
     */
    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }


    public boolean useFullScreen() {
        return getBoolean("uses_full_screen");
    }
}
