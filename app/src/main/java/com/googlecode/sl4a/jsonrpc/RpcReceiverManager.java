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

import com.duy.pascal.backend.lib.android.BaseAndroidLibrary;
import com.googlecode.sl4a.Log;
import com.googlecode.sl4a.rpc.MethodDescriptor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class RpcReceiverManager {
    private static final String TAG = "RpcReceiverManager";
    private final Map<Class<? extends BaseAndroidLibrary>, BaseAndroidLibrary> mReceivers;

    /**
     * A map of strings to known RPCs.
     */
    private final Map<String, MethodDescriptor> mKnownRpcs = new HashMap<>();

    public RpcReceiverManager(Collection<Class<? extends BaseAndroidLibrary>> classList) {
        mReceivers = new HashMap<>();
        for (Class<? extends BaseAndroidLibrary> receiverClass : classList) {
            mReceivers.put(receiverClass, null);
            Collection<MethodDescriptor> methodList = MethodDescriptor.collectFrom(receiverClass);
            for (MethodDescriptor m : methodList) {
                android.util.Log.d(TAG, "RpcReceiverManager: " + m.toString());
                if (mKnownRpcs.containsKey(m.getName())) {
                    // We already know an RPC of the same name. We don't catch this anywhere because this is a
                    // programming error.
                    throw new RuntimeException("An RPC with the name " + m.getName() + " is already known.");
                }
                mKnownRpcs.put(m.getName(), m);
            }
        }
    }

    public Collection<Class<? extends BaseAndroidLibrary>> getRpcReceiverClasses() {
        return mReceivers.keySet();
    }

    private BaseAndroidLibrary get(Class<? extends BaseAndroidLibrary> clazz) {
        BaseAndroidLibrary object = mReceivers.get(clazz);
        if (object != null) {
            return object;
        }
        Constructor<? extends BaseAndroidLibrary> constructor;
        try {
            constructor = clazz.getConstructor(getClass());
            object = constructor.newInstance(this);
            mReceivers.put(clazz, object);
        } catch (Exception e) {
            Log.e(e);
        }

        return object;
    }

    public <T extends BaseAndroidLibrary> T getReceiver(Class<T> clazz) {
        BaseAndroidLibrary receiver = get(clazz);
        return clazz.cast(receiver);
    }

    public MethodDescriptor getMethodDescriptor(String methodName) {
        return mKnownRpcs.get(methodName);
    }

    public Object invoke(Class<? extends BaseAndroidLibrary> clazz, Method method, Object[] args)
            throws Exception {
        BaseAndroidLibrary object = get(clazz);
        return method.invoke(object, args);
    }

    public void shutdown() {
        for (BaseAndroidLibrary receiver : mReceivers.values()) {
            if (receiver != null) {
                receiver.shutdown();
            }
        }
    }
}
