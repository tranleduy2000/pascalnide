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

package com.duy.pascal.frontend.theme.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class CodeTheme {
    private static final String TAG = "CodeTheme";
    private static ArrayList<CodeTheme> customThemes;
    private static ArrayList<CodeTheme> builtinThemes;
    private final boolean builtin;
    private String id;
    private Hashtable<String, Integer> colors = new Hashtable<>();

    public CodeTheme(String id, boolean builtin) {
        this.id = id;
        this.builtin = builtin;
    }

    public CodeTheme(boolean builtin) {
        this.builtin = builtin;
    }

    public static CodeTheme getTheme(int themeId, Context context) {
        if (builtinThemes == null) loadBuiltinThemes(context);
        if (themeId < builtinThemes.size()) return getBuiltinTheme(themeId, context);

        if (customThemes == null) loadCustomThemes(context);
        if (themeId - builtinThemes.size() < customThemes.size()) {
            return getCustomTheme(themeId - builtinThemes.size(), context);
        }
        return getBuiltinTheme(0, context);
    }

    private static CodeTheme getBuiltinTheme(int themeId, Context ctx) {
        if (builtinThemes == null) loadBuiltinThemes(ctx);
        if (builtinThemes.get(themeId) == null)
            return builtinThemes.get(0);
        return builtinThemes.get(themeId);
    }

    public static CodeTheme getCustomTheme(int themeId, Context ctx) {
        if (customThemes == null) loadCustomThemes(ctx);
        return customThemes.get(themeId);
    }

    private static void loadCustomThemes(Context ctx) {
        customThemes = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        Set<String> keyPrefs = prefs.getAll().keySet();
        for (String key : keyPrefs) {
            if (key.startsWith("theme.")) { //theme.12.background_color
                String id = key.substring(7, key.lastIndexOf(".") - 1);
                loadCustomTheme(id, prefs);
            }
        }
    }

    public static void addCustomTheme(CodeTheme codeTheme, Context context) {
        Hashtable<String, Integer> colors = codeTheme.getColors();
        Set<Map.Entry<String, Integer>> entries = colors.entrySet();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        for (Map.Entry<String, Integer> entry : entries) {
            sharedPreferences.edit().putInt(entry.getKey(), entry.getValue()).apply();
        }
    }

    private static void loadCustomTheme(String id, SharedPreferences prefs) {
        try {
            CodeTheme codeTheme = new CodeTheme(id, false);
            codeTheme.putColor("background_color", loadColor(prefs, id, "background_color"));
            codeTheme.putColor("normal_text_color", loadColor(prefs, id, "normal_text_color"));
            codeTheme.putColor("number_color", loadColor(prefs, id, "number_color"));
            codeTheme.putColor("key_word_color", loadColor(prefs, id, "key_word_color"));
            codeTheme.putColor("string_color", loadColor(prefs, id, "string_color"));
            codeTheme.putColor("comment_color", loadColor(prefs, id, "comment_color"));
            codeTheme.putColor("error_color", loadColor(prefs, id, "error_color"));
            codeTheme.putColor("opt_color", loadColor(prefs, id, "opt_color"));

            customThemes.add(codeTheme);
        } catch (Exception ignored) {
        }
    }

    private static int loadColor(SharedPreferences prefs, String id, String name) {
        return Color.parseColor(prefs.getString("theme." + id + "." + name, "#FF000000").trim());
    }

    private static void loadBuiltinThemes(Context ctx) {
        builtinThemes = new ArrayList<>();
        try {
            InputStream is = ctx.getAssets().open("themes/themes.properties");
            Properties properties = new Properties();
            properties.load(is);
            int id = 1;
            while (true) {
                try {
                    CodeTheme codeTheme = new CodeTheme(Integer.toString(id), true);
                    codeTheme.putColor("background_color", loadColor(properties, id, "background_color"));
                    codeTheme.putColor("normal_text_color", loadColor(properties, id, "normal_text_color"));
                    codeTheme.putColor("number_color", loadColor(properties, id, "number_color"));
                    codeTheme.putColor("key_word_color", loadColor(properties, id, "key_word_color"));
                    codeTheme.putColor("string_color", loadColor(properties, id, "string_color"));
                    codeTheme.putColor("comment_color", loadColor(properties, id, "comment_color"));
                    codeTheme.putColor("error_color", loadColor(properties, id, "error_color"));
                    codeTheme.putColor("opt_color", loadColor(properties, id, "opt_color"));
                    builtinThemes.add(codeTheme);
                    id++;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static Integer loadColor(Properties properties, int id, String name)
            throws Exception {
        String color = properties.getProperty("theme." + id + "." + name);
        if (color == null) throw new RuntimeException("Can not find properties " + name);
        return Color.parseColor(color.trim());
    }

    public static String[] getThemes(Context ctx) {
        int i;
        if (builtinThemes == null) {
            loadBuiltinThemes(ctx);
        }
        if (customThemes == null) {
            loadCustomThemes(ctx);
        }
        String[] ret = new String[(builtinThemes.size() + customThemes.size())];
        for (i = 0; i < builtinThemes.size(); i++) {
            ret[i] = Integer.valueOf(i).toString();
        }
        for (i = 0; i < customThemes.size(); i++) {
            ret[builtinThemes.size() + i] = Integer.valueOf(builtinThemes.size() + i).toString();
        }
        return ret;
    }

    private Hashtable<String, Integer> getColors() {
        return colors;
    }

    public int getBackground() {
        return getColor("background_color");
    }

    public int getTextColor() {
        return getColor("normal_text_color");
    }

    @Override
    public String toString() {
        return colors.toString();
    }

    public String getId() {
        return this.id;
    }

    public boolean isBuiltin() {
        return this.builtin;
    }

    private void putColor(String name, Integer color) {
        this.colors.put(name, color);
    }

    public int getColor(String name) {
        return this.colors.get(name);
    }

    public void setKeyWordColor(int integer) {
        putColor("key_word_color", integer);
    }

    public void setBooleanColor(int integer) {
        putColor("boolean_color", integer);
    }

    public int getErrorColor() {
        return getColor("error_color");
    }

    public void setErrorColor(int integer) {
        putColor("error_color", integer);
    }

    public int getNumberColor() {
        return getColor("number_color");
    }

    public void setNumberColor(int integer) {
        putColor("number_color", integer);
    }

    public int getKeywordColor() {
        return getColor("key_word_color");
    }

    public int getOptColor() {
        return getColor("opt_color");
    }

    public void setOptColor(int integer) {
        putColor("opt_color", integer);
    }

    public int getCommentColor() {
        return getColor("comment_color");
    }

    public void setCommentColor(int integer) {
        putColor("comment_color", integer);
    }

    public int getStringColor() {
        return getColor("string_color");
    }

    public void setStringColor(int integer) {
        putColor("string_color", integer);
    }
}
