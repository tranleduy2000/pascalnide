package com.duy.pascal.frontend.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.duy.pascal.frontend.R;
import com.duy.pascal.frontend.data.Preferences;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Duy on 18-Mar-17.
 */

public class FontManager {
    private static final String PATH_TO_FONT = "/fonts/";
    public static Typeface CONSOLE_FONT = null;
    private static Typeface font;

    /**
     * return lasted font, if current font is null, this method will be get name of font
     * in the {@link android.preference.PreferenceManager} and return this font
     */
    public static Typeface getInstance(Context context) {
        if (font == null) {
            Preferences preferences = new Preferences(context);
            String fontName = preferences.getString(context.getString(R.string.key_pref_font));
            font = getFontFromAsset(fontName, context);
        }
        return font;
    }

    public static Typeface getFontConsole(Context context) {
        if (CONSOLE_FONT == null) {
            CONSOLE_FONT = getFontFromAsset("Consolas.ttf", context);
        }
        return CONSOLE_FONT;
    }

    public static Typeface getFontFromAsset(String name, Context context) {
        AssetManager assetManager = context.getAssets();
        try {
            if (name.equalsIgnoreCase(context.getString(R.string.font_consolas))) {
                return Typeface.createFromAsset(assetManager, PATH_TO_FONT + "consolas.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_courier_new))) {
                return Typeface.createFromAsset(assetManager, PATH_TO_FONT + "courier_new.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_lucida_sans_typewriter))) {
                return Typeface.createFromAsset(assetManager, PATH_TO_FONT + "lucida_sans_typewriter_regular.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_monospace))) {
                return Typeface.MONOSPACE;
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_roboto))) {
                return Typeface.createFromAsset(assetManager, PATH_TO_FONT + "roboto.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.source_code_pro))) {
                return Typeface.createFromAsset(assetManager, PATH_TO_FONT + "source_code_pro.ttf");
            }
        } catch (Exception e) {
            FirebaseCrash.report(new Exception("Can not find font"));
        }
        return Typeface.MONOSPACE;
    }
}
