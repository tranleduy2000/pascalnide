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

package com.duy.pascal.frontend.themefont.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.duy.pascal.frontend.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Duy on 12-Jul-17.
 */

public class ThemeManager {

    private static final String THEME_FILE = "THEME_FILE";
    private static ArrayList<CodeTheme> customThemes;
    private static ArrayList<CodeTheme> builtinThemes;

    private static void loadThemeFromPref(String name, @NonNull CodeTheme codeTheme, Context context) {
        int style = CodeThemeUtils.getCodeTheme(context, name);
        TypedArray typedArray = context.obtainStyledAttributes(style, R.styleable.CodeTheme);
        typedArray.getInteger(R.styleable.CodeTheme_background_color, R.color.color_background_color);

        codeTheme.setTextColor(typedArray.getInteger(R.styleable.CodeTheme_normal_text_color,
                R.color.color_normal_text_color));
        codeTheme.setBackgroundColor(typedArray.getInteger(R.styleable.CodeTheme_background_color,
                R.color.color_background_color));
        codeTheme.setErrorColor(typedArray.getInteger(R.styleable.CodeTheme_error_color,
                R.color.color_error_color));
        codeTheme.setNumberColor(typedArray.getInteger(R.styleable.CodeTheme_number_color,
                R.color.color_number_color));
        codeTheme.setKeyWordColor(typedArray.getInteger(R.styleable.CodeTheme_key_word_color,
                R.color.color_key_word_color));
        codeTheme.setCommentColor(typedArray.getInteger(R.styleable.CodeTheme_comment_color,
                R.color.color_comment_color));
        codeTheme.setStringColor(typedArray.getInteger(R.styleable.CodeTheme_string_color,
                R.color.color_string_color));
        codeTheme.setBooleanColor(typedArray.getInteger(R.styleable.CodeTheme_boolean_color,
                R.color.color_boolean_color));
        codeTheme.setOptColor(typedArray.getInteger(R.styleable.CodeTheme_opt_color,
                R.color.color_opt_color));
        typedArray.recycle();
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

    public static void addCustomTheme(CodeTheme codeTheme, Context context, String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(THEME_FILE,
                Context.MODE_PRIVATE);
        HashMap<String, Integer> colors = codeTheme.getColors();
        Set<Map.Entry<String, Integer>> entries = colors.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            sharedPreferences.edit().putInt("theme." + name + "." + entry.getKey(), entry.getValue()).apply();
        }
    }

    private static int loadColor(SharedPreferences prefs, String id, String name) {
        return Color.parseColor(prefs.getString("theme." + id + "." + name, "#FF000000").trim());
    }

    private static int loadColor(SharedPreferences prefs, String id, String name, String def) {
        return Color.parseColor(prefs.getString("theme." + id + "." + name, def).trim());
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

    private static void loadCustomThemes(Context context) {
        customThemes = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(THEME_FILE, Context.MODE_PRIVATE);
        Set<String> keyPrefs = prefs.getAll().keySet();
        for (String key : keyPrefs) {
            if (key.startsWith("theme.")) { //theme.12.background_color
                String id = key.substring(7, key.lastIndexOf(".") - 1);
                loadCustomTheme(id, prefs);
            }
        }
    }

    public static CodeTheme getDefault(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(THEME_FILE, Context.MODE_PRIVATE);
        String name = preferences.getString(context.getString(R.string.key_code_theme), "");
        try {
            Integer id = Integer.parseInt(name);
            return getTheme(id, context);
        } catch (Exception e) {
            CodeTheme codeTheme = new CodeTheme(true);
            loadThemeFromPref(name, codeTheme, context);
            return codeTheme;
        }
    }

}
