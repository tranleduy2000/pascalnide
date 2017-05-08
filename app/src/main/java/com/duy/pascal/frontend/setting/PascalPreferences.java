/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.utils.FontManager;

/**
 * Setting for application
 * <p>
 * Created by Duy on 3/7/2016
 */
public class PascalPreferences {
    public static final String FILE_PATH = "last_file";
    public static final String LAST_FIND = "LAST_FIND";
    public static final String LAST_REPLACE = "LAST_REPLACE";
    public static final String TAB_POSITION_FILE = "TAB_POSITION_FILE";
    @NonNull
    protected SharedPreferences.Editor editor;
    @NonNull
    protected Context context;
    @NonNull
    private SharedPreferences sharedPreferences;

    @SuppressLint("CommitPrefEdits")
    public PascalPreferences(@NonNull Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
    }

    @SuppressLint("CommitPrefEdits")
    public PascalPreferences(@NonNull SharedPreferences mPreferences, @NonNull Context context) {
        this.context = context;
        this.sharedPreferences = mPreferences;
        this.editor = sharedPreferences.edit();
    }

    /**
     * reset default setting
     *
     * @param context
     */
    public static void setFirstOpen(Context context) {
        String key = "first_open";

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
        return getInt(key, -1);
    }

    public int getInt(String key, int def) {
        try {
            return sharedPreferences.getInt(key, def);
        } catch (Exception e) {
            try {
                return Integer.parseInt(getString(key));
            } catch (Exception ignored) {
                return def;
            }
        }
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
        String s = "";
        try {
            s = sharedPreferences.getString(key, "");
        } catch (Exception ignored) {
        }
        return s;
    }

    public boolean getBoolean(String key) {
        try {
            return sharedPreferences.getBoolean(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean getBoolean(String key, boolean def) {
        try {
            return sharedPreferences.getBoolean(key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean useFullScreen() {
        return getBoolean(context.getString(R.string.key_full_screen));
    }

    public int getConsoleBackground() {
//        return getInt(context.getString(R.string.key_bg_console));
        return Color.BLACK;
    }

    public int getConsoleTextColor() {
//        return getInt(context.getString(R.string.key_console_text_color));
        return Color.WHITE;
    }

    public int getConsoleFontSize() {
//        int fontsize;
//        try {
//            fontsize = getInt(context.getString(R.string.key_console_font_size));
//        } catch (Exception e) {
//            fontsize = 14;
//        }
//        return (fontsize >= 1 && fontsize <= 100) ? fontsize : 14;
        return (int) getTextSize();
    }

    public int getConsoleFrameRate() {
        int i;
        try {
            i = Integer.parseInt(sharedPreferences.getString(context.getString(R.string.key_console_frame_rate), "60"));
        } catch (Exception e) {
            i = 60;
        }
        return (i > 0 && i < 1000) ? i : 60;
    }

    public int getConsoleMaxBuffer() {
        int res;
        try {
            res = Integer.parseInt(
                    sharedPreferences.getString(context.getString(R.string.key_console_max_buffer_size), "200"));
        } catch (Exception e) {
            res = 100;
        }
        return res;
    }

    public boolean isWrapText() {
        return getBoolean(context.getString(R.string.key_pref_word_wrap));
    }

    public float getTextSize() {
        try {
            return Float.parseFloat(getString(context.getString(R.string.key_pref_font_size)));
        } catch (Exception e) {
            return 14f;
        }
    }

    public Typeface getFont() {
        return FontManager.getFontFromAsset(context, getString(context.getString(R.string.key_pref_font)));
    }

    public boolean isShowLines() {
        return getBoolean(context.getString(R.string.key_show_line_number));
    }

    public void setShowLines(boolean isShow) {
        put(context.getString(R.string.key_show_line_number), isShow);
    }

    public boolean isAutoCompile() {
        return getBoolean(context.getString(R.string.key_pref_auto_compile));
    }

    public boolean isShowListSymbol() {
        return getBoolean(context.getString(R.string.key_show_symbol), true);
    }

    public boolean isShowSuggestPopup() {
        return getBoolean(context.getString(R.string.key_show_suggest_popup), true);
    }

    public void setShowSuggestPopup(boolean b) {
        put(context.getString(R.string.key_show_suggest_popup), b);
    }

    public void setShowSymbol(boolean b) {
        put(context.getString(R.string.key_show_symbol), b);
    }

    public void setWordWrap(boolean b) {
        put(context.getString(R.string.key_pref_word_wrap), b);
    }

    public int getMaxPage() {
        int count = getInt(context.getString(R.string.key_max_page));
        count = Math.min(30, count);
        count = Math.max(1, count);
        return count;
    }

    public void setMaxPage(int count) {
        put(context.getString(R.string.key_max_page), count);
    }

    public int getMaxHistoryEdit() {
        int max = getInt(context.getString(R.string.key_max_page), 100);
        max = Math.min(10, max);
        max = Math.max(1, max);
        return max;
    }
}
