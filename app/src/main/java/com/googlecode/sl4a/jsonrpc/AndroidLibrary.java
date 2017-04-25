// Copyright 2010 Google Inc. All Rights Reserved.

package com.googlecode.sl4a.jsonrpc;

import com.duy.pascal.backend.lib.PascalLibrary;

import java.util.Map;

public abstract class AndroidLibrary implements PascalLibrary {

    protected final RpcReceiverManager mManager;

    public AndroidLibrary(RpcReceiverManager manager) {
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
