package com.duy.pascal.frontend.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class ThemeFromAssets {
    private static ArrayList<ThemeFromAssets> customThemeFromAssetses;
    private static ArrayList<ThemeFromAssets> themeFromAssetses;
    private final boolean builtin;
    private int bg_editor_color;
    private String id;
    private ArrayList<Color> colors;

    public ThemeFromAssets(String id, boolean builtin) {
        this.colors = new ArrayList<>();
        this.id = id;
        this.builtin = builtin;
    }

    public static ThemeFromAssets getTheme(int themeId, Context context) {
        if (themeFromAssetses == null) {
            loadThemes(context);
        }
        if (customThemeFromAssetses == null) {
            loadCustomThemes(context);
        }
        if (themeId < themeFromAssetses.size()) {
            return getBuiltinTheme(themeId, context);
        }
        if (themeId - themeFromAssetses.size() < customThemeFromAssetses.size()) {
            return getCustomTheme(themeId - themeFromAssetses.size(), context);
        }
        return getBuiltinTheme(0, context);
    }

    private static ThemeFromAssets getBuiltinTheme(int themeId, Context ctx) {
        if (themeFromAssetses == null) {
            loadThemes(ctx);
        }
        if (customThemeFromAssetses == null) {
            loadCustomThemes(ctx);
        }
        return themeFromAssetses.get(themeId);
    }

    static ThemeFromAssets getCustomTheme(int themeId, Context ctx) {
        if (themeFromAssetses == null) {
            loadThemes(ctx);
        }
        if (customThemeFromAssetses == null) {
            loadCustomThemes(ctx);
        }
        return (ThemeFromAssets) customThemeFromAssetses.get(themeId);
    }

    private static void loadCustomThemes(Context ctx) {
        customThemeFromAssetses = new ArrayList<>();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        Set<String> keyPrefs = prefs.getAll().keySet();
        String[] allPrefs = new String[keyPrefs.size()];
        int i = 0;
        for (String p : keyPrefs) {
            int i2 = i + 1;
            allPrefs[i] = p;
            i = i2;
        }
        Arrays.sort(allPrefs);
        for (String p2 : allPrefs) {
            if (p2.startsWith("theme.") && p2.endsWith(".bg_editor_color")) {
                String id = p2.substring(p2.indexOf(46) + 1);
                loadCustomTheme(id.substring(0, id.indexOf(46)), prefs);
            }
        }
    }

    private static void loadCustomTheme(String id, SharedPreferences prefs) {
        try {
            ThemeFromAssets themeFromAssets = new ThemeFromAssets(id, false);
            themeFromAssets.setBackground(loadCustomColor(prefs, id, "bg_editor_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "normal_text_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "number_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "key_word_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "string_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "identifier"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "preprocessor"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "comment_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "error_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "function_color"));
            themeFromAssets.addStyle(loadCustomStyle(prefs, id, "opt_color"));
            customThemeFromAssetses.add(themeFromAssets);
        } catch (Exception e) {
        }
    }

    private static Color loadCustomStyle(SharedPreferences prefs, String id, String name) {
        return new Color(android.graphics.Color.parseColor(prefs.getString("theme." + id + "." + name, "#FFFFFFFF").trim()));
    }

    private static int loadCustomColor(SharedPreferences prefs, String id, String name) {
        return android.graphics.Color.parseColor(prefs.getString("theme." + id + "." + name, "#FF000000").trim());
    }

    private static void loadThemes(Context ctx) {
        themeFromAssetses = new ArrayList();
        try {
            InputStream is = ctx.getAssets().open("themes/themes.properties");
            Properties properties = new Properties();
            properties.load(is);
            int themeid = 1;
            while (true) {
                try {
                    ThemeFromAssets themeFromAssets = new ThemeFromAssets(Integer.toString(themeid), true);
                    themeFromAssets.setBackground(loadColor(properties, themeid, "bg_editor_color"));//0
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "normal_text_color"));//1
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "number_color"));//2
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "key_word_color"));//3
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "string_color"));//4
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "identifier"));//5
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "preprocessor"));//6
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "comment_color"));//7
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "error_color"));//8
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "function_color"));//9
                    themeFromAssets.addStyle(loadStyle(properties, themeid, "opt_color"));//10
                    themeFromAssetses.add(themeFromAssets);
                    themeid++;
                } catch (Exception e) {
                    return;
                }
            }
        } catch (IOException e2) {
        }
    }

    private static Color loadStyle(Properties properties, int themeid, String name) {
        String line = properties.getProperty("theme." + themeid + "." + name);
        if (line == null && name.equals("function_color")) {
            line = properties.getProperty("theme." + themeid + ".string_color");
        }
        if (line == null && name.equals("opt_color")) {
            line = properties.getProperty("theme." + themeid + ".string_color");
        }
        return new Color(android.graphics.Color.parseColor(line.trim()));
    }

    private static int loadColor(Properties properties, int themeid, String name) {
        String line = properties.getProperty("theme." + themeid + "." + name);
        if (line == null && name.equals("function_color")) {
            line = properties.getProperty("theme." + themeid + ".string_color");
        }
        if (line == null && name.equals("opt_color")) {
            line = properties.getProperty("theme." + themeid + ".string_color");
        }
        return android.graphics.Color.parseColor(line.trim());
    }

    public static CharSequence[] getThemes(Context ctx) {
        int i;
        if (themeFromAssetses == null) {
            loadThemes(ctx);
        }
        if (customThemeFromAssetses == null) {
            loadCustomThemes(ctx);
        }
        CharSequence[] ret = new CharSequence[(themeFromAssetses.size() + customThemeFromAssetses.size())];
        for (i = 0; i < themeFromAssetses.size(); i++) {
            ret[i] = Integer.valueOf(i).toString();
        }
        for (i = 0; i < customThemeFromAssetses.size(); i++) {
            ret[themeFromAssetses.size() + i] = Integer.valueOf(themeFromAssetses.size() + i).toString();
        }
        return ret;
    }

    public static CharSequence[] getCustomThemes(Context ctx) {
        if (themeFromAssetses == null) {
            loadThemes(ctx);
        }
        if (customThemeFromAssetses == null) {
            loadCustomThemes(ctx);
        }
        CharSequence[] ret = new CharSequence[customThemeFromAssetses.size()];
        for (int i = 0; i < customThemeFromAssetses.size(); i++) {
            ret[i] = Integer.valueOf(themeFromAssetses.size() + i).toString();
        }
        return ret;
    }

    public static void reloadCustomThemes() {
        customThemeFromAssetses = null;
    }

    public static boolean reloaded() {
        return customThemeFromAssetses == null;
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

    private void addStyle(Color color) {
        this.colors.add(color);
    }

    public Color getDefaultStyle() {
        return (Color) this.colors.get(0);
    }

    public Color getStyle(int id) {
        Color color = (Color) this.colors.get(id);
        if (color.getForeground() == getDefaultStyle().getForeground()) {
            return null;
        }
        return color;
    }

    public int getColor(int id) {
        return ((Color) this.colors.get(id)).getForeground();
    }


    public ArrayList<Color> getColors() {
        return this.colors;
    }

    public int getBackground() {
        return this.bg_editor_color;
    }

    public void setBackground(int bg_editor_color) {
        this.bg_editor_color = bg_editor_color;
    }

    public void setColor(int style, int foreground) {
        this.colors.get(style).setForeground(foreground);
    }

    public String getBackgroundHex() {
        return "#FF" + Integer.toHexString(getBackground()).substring(2).toUpperCase();
    }

    public String getColorHex(int style) {
        return "#FF" + Integer.toHexString(getColor(style)).substring(2).toUpperCase();
    }
}
