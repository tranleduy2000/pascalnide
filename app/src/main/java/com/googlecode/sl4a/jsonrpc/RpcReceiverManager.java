/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.sl4a.jsonrpc;

import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.MethodDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class RpcReceiverManager {
    private static final String TAG = "RpcReceiverManager";
    private final Map<Class<? extends PascalLibrary>, PascalLibrary> mReceivers;

    private final Map<String, MethodDescriptor> mKnownRpcs = new HashMap<>();

    public RpcReceiverManager() {
        mReceivers = new HashMap<>();
    }

    public Collection<Class<? extends PascalLibrary>> getRpcReceiverClasses() {
        return mReceivers.keySet();
    }

    private PascalLibrary get(Class<? extends PascalLibrary> clazz) {
        PascalLibrary object = mReceivers.get(clazz);
        if (object != null) {
            return object;
        }
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor(getClass());
            object = (PascalLibrary) constructor.newInstance(this);
            mReceivers.put(clazz, object);
        } catch (Exception e) {
            Log.e(e);
        }

        return object;
    }

    public <T extends PascalLibrary> T getReceiver(Class<T> clazz) {
        PascalLibrary receiver = get(clazz);
        return clazz.cast(receiver);
    }

    public MethodDescriptor getMethodDescriptor(String methodName) {
        return mKnownRpcs.get(methodName);
    }

    public Object invoke(Class<? extends PascalLibrary> clazz, Method method, Object[] args)
            throws Exception {
        PascalLibrary object = get(clazz);
        return method.invoke(object, args);
    }

    public void shutdown() {
        for (PascalLibrary receiver : mReceivers.values()) {
            if (receiver != null) {
                receiver.shutdown();
            }
        }
    }
}
