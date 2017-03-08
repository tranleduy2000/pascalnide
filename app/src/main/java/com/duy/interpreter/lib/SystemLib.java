package com.duy.interpreter.lib;

import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.Map;

/**
 * Created by Duy on 07-Mar-17.
 */

public class SystemLib implements PascalLibrary {

    public static void inc(VariableBoxer<Long> boxer) throws RuntimePascalException {
        boxer.set(boxer.get() + 1);
    }

    public static void dec(VariableBoxer<Long> boxer) throws RuntimePascalException {
//        if (boxer.get() instanceof Long) {
//            boxer.set((Long) boxer.get() - 1);
//        } else if (boxer.get() instanceof Integer) {
//            boxer.set((Integer) boxer.get() - 1);
//        } else {
//            throw new RuntimeException("Wrong type in function dec");
//        }
        boxer.set(boxer.get() - 1);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }
}
