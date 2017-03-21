package com.duy.pascal.frontend.utils;

import android.content.Context;

import com.duy.pascal.frontend.R;

/**
 * Created by Duy on 12-Mar-17.
 */

public class CodeThemeUtils {
    public static int getCodeTheme(Context context, String name) {
        if (name.equals(context.getString(R.string.default_theme))) {
            return R.style.CodeTheme;
        } else if (name.equals(context.getString(R.string.BrightYellow))) {
            return R.style.CodeTheme_BrightYellow;
        } else if (name.equals(context.getString(R.string.DarkGray))) {
            return R.style.CodeTheme_DarkGray;
        } else if (name.equals(context.getString(R.string.EspressoLibre))) {
            return R.style.CodeTheme_EspressoLibre;
        } else if (name.equals(context.getString(R.string.Idel))) {
            return R.style.CodeTheme_Idel;
        } else if (name.equals(context.getString(R.string.KFT2))) {
            return R.style.CodeTheme_KFT2;
        } else if (name.equals(context.getString(R.string.Modnokai_Coffee))) {
            return R.style.CodeTheme_ModnokaiCoffee;
        } else {
            return R.style.CodeTheme;
        }
    }
}
