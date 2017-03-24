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
