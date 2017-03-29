package com.duy.pascal.frontend.utils;

import android.content.Context;
import android.graphics.Typeface;

import com.duy.pascal.frontend.R;
import com.google.firebase.crash.FirebaseCrash;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by Duy on 18-Mar-17.
 */

public class FontManager {
    private static final String PATH_TO_FONT = "fonts/";
    private static final String TAG = "Typefaces";
    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();


    private static Typeface get(Context c, String assetPath) throws IOException {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    throw new IOException("Could not get typeface '" + assetPath + "' because " + e.getMessage());
                }
            }
            return cache.get(assetPath);
        }
    }

    public static Typeface getFontFromAsset(Context context, String name) {
        try {
            if (name.equalsIgnoreCase(context.getString(R.string.font_consolas))) {
                return get(context, PATH_TO_FONT + "consolas.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_courier_new))) {
                return get(context, PATH_TO_FONT + "courier_new.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_lucida_sans_typewriter))) {
                return get(context, PATH_TO_FONT + "lucida_sans_typewriter_regular.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_monospace))) {
                return Typeface.MONOSPACE;
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_roboto))) {
                return get(context, PATH_TO_FONT + "roboto.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.source_code_pro))) {
                return get(context, PATH_TO_FONT + "source_code_pro.ttf");
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
        return Typeface.MONOSPACE;
    }
}
