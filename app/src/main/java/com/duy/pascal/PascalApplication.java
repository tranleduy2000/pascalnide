package com.duy.pascal;

import android.app.Application;

import com.duy.pascal.frontend.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Duy on 12-Mar-17.
 */

public class PascalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
