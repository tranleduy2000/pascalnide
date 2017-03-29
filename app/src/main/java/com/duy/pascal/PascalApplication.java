package com.duy.pascal;

import android.app.Application;

/**
 * Created by Duy on 12-Mar-17.
 */

public class PascalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * set default font
         */
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/roboto.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }
}
