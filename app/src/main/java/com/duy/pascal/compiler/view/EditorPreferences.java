package com.duy.pascal.compiler.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import com.duy.pascal.compiler.R;
import com.duy.pascal.compiler.data.Preferences;

/**
 * Created by Duy on 02-Mar-17.
 */

public class EditorPreferences extends Preferences {
    public EditorPreferences(Context context) {
        super(context);
    }

    public EditorPreferences(SharedPreferences mPreferences, Context context) {
        super(mPreferences, context);
    }

    public boolean isWrapText() {
        return getBoolean(context.getString(R.string.key_pref_word_wrap));
    }

    public float getTextSize() {
//        return TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                Float.parseFloat(getString(context.getString(R.string.key_pref_font_size))),
//                context.getResources().getDisplayMetrics());
        return Float.parseFloat(getString(context.getString(R.string.key_pref_font_size)));
    }

    public Typeface getFont() {
        return null;
    }

    public boolean isShowLineNumbers() {
        return getBoolean(context.getString(R.string.key_pref_show_line_number));
    }
}
