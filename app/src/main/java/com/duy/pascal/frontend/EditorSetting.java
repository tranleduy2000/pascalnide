package com.duy.pascal.frontend;

import android.content.Context;
import android.content.SharedPreferences;

import com.duy.pascal.frontend.setting.PascalPreferences;

/**
 * Created by Duy on 02-Mar-17.
 */

public class EditorSetting extends PascalPreferences {
    public EditorSetting(Context context) {
        super(context);
    }

    public EditorSetting(SharedPreferences mPreferences, Context context) {
        super(mPreferences, context);
    }

}
