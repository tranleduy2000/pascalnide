// Copyright 2010 Google Inc. All Rights Reserved.

package com.duy.pascal.backend.lib.android;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.android.utils.AndroidLibraryManager;

import java.util.Map;

public abstract class BaseAndroidLibrary implements PascalLibrary {

    protected final AndroidLibraryManager mManager;

    public BaseAndroidLibrary(AndroidLibraryManager manager) {
        // To make reflection easier, we ensures that all the subclasses agree on this common
        // constructor.
        mManager = manager;
    }

    /**
     * Invoked when the receiver is shut down.
     */
    public abstract void shutdown();

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
