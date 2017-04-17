package com.duy.pascal;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Duy on 12-Mar-17.
 */

public class PascalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
