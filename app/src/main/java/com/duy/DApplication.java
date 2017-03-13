package com.duy;

import android.app.Application;
import android.content.res.Configuration;

import com.duy.pascal.compiler.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Duy on 12-Mar-17.
 */

public class DApplication extends Application {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
