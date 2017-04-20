/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.frontend.debug;

import com.duy.pascal.backend.debugable.DebugListener;

import java.lang.reflect.Method;

/**
 * Created by Duy on 24-Mar-17.
 */

public class DebugManager {

    public static void outputMethod(DebugListener debugListener, Method method) {
        if (debugListener != null) {
            debugListener.onFunctionCall(method.getName());
        }
    }

    public static void outputConditionWhile(DebugListener debugListener, boolean b) {
        if (debugListener != null) {
            debugListener.onNewMessage("Kiem tra dieu kien vong while la " + b);
        }
    }

    public static void outputConditionFor(DebugListener debugListener, boolean b) {
        if (debugListener != null) {
            debugListener.onNewMessage("Kiem tra dieu kien vong for la " + b);
        }
    }
}
