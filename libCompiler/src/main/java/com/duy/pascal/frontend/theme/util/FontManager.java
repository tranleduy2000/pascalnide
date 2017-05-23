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
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    private static Typeface get(Context c, String assetPath) throws IOException {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    throw new IOException("Could not get typeface '" +
                            assetPath + "' because " + e.getMessage());
                }
            }
            return cache.get(assetPath);
        }
    }

    public synchronized static Typeface getFontFromAsset(Context context, String name) {
        try {
            if (name.equalsIgnoreCase(context.getString(R.string.font_consolas))) {
                return get(context, PATH_TO_FONT + "consolas.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_courier_new))) {
                return get(context, PATH_TO_FONT + "courier_new.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_lucida_sans_typewriter))) {
                return get(context, PATH_TO_FONT + "lucida_sans_typewriter_regular.ttf");
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_monospace))) {
                return Typeface.MONOSPACE;
            } else if (name.equalsIgnoreCase(context.getString(R.string.font_source_code_pro))) {
                return get(context, PATH_TO_FONT + "source_code_pro.ttf");
            } else if (name.equalsIgnoreCase("triple_bold.ttf")) {
                return get(context, PATH_TO_FONT + "triple_bold.ttf");
            } else if (name.equalsIgnoreCase("triplex.ttf")) {
                return get(context, PATH_TO_FONT + "triplex.ttf");
            } else if (name.equalsIgnoreCase("graph_script.ttf")) {
                return get(context, PATH_TO_FONT + "graph_script.ttf");
            } else if (name.equalsIgnoreCase("graph_euro.ttf")) {
                return get(context, PATH_TO_FONT + "graph_euro.ttf");
            } else if (name.equalsIgnoreCase("triplex.ttf")) {
                return get(context, PATH_TO_FONT + "triplex.ttf");
            } else if (name.equalsIgnoreCase("gothic.ttf")) {
                return get(context, PATH_TO_FONT + "gothic.ttf");
            } else if (name.equalsIgnoreCase("lcd_solid.ttf")) {
                return get(context, PATH_TO_FONT + "lcd_solid.ttf");
            } else {
                return get(context, PATH_TO_FONT + name);
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
        return Typeface.MONOSPACE;
    }
}
