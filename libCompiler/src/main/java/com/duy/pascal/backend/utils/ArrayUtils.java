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

package com.duy.pascal.backend.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.duy.pascal.frontend.view.exec_screen.console.TextConsole;
import com.js.interpreter.runtime_value.RuntimeValue;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Duy on 24-Mar-17.
 */

public class ArrayUtils {
    public static String arrayToString(Object[] array) {
        return Arrays.toString(array);
    }

    public static String arrayToString(TextConsole[] array) {
        StringBuilder res = new StringBuilder();
        for (TextConsole textObject : array) {
            res.append(textObject.getSingleString());
        }
        return res.toString();
    }

    public static String arrayToString(String[] array) {
        StringBuilder res = new StringBuilder();
        for (String textObject : array) {
            res.append(textObject);
        }
        return res.toString();
    }

    public static String[] join(@NonNull String[]... objects) {
        int size = 0;
        for (String[] object : objects) {
            size += object.length;
        }
        String[] result = new String[size];
        int index = 0;
        for (String[] object : objects) {
            for (String t : object) {
                result[index] = t;
                index++;
            }
        }
        return result;
    }

    /**
     * uses for function pascal, such as textColor(integer)
     */
    public static String argToString(Object[] argumentTypes) {
        int iMax = argumentTypes.length - 1;
        StringBuilder b = new StringBuilder();
        b.append('(');
        for (int i = 0; i < argumentTypes.length; i++) {
            b.append(argumentTypes[i].toString());
            if (i == iMax) {
                b.append(')');
                break;
            }
            b.append(", ");
        }
        if (argumentTypes.length == 0)
            b.append(")");
        return b.toString();
    }

    /**
     * uses for function pascal, such as textColor(integer)
     */
    public static String argToString(List<RuntimeValue> args) {
        return argToString(args.toArray());
    }

    public static String expectToString(String[] expected, Context context) {
        if (expected.length == 0) return "";
        if (expected.length == 1) return expected[0];
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < expected.length; i++) {
            result.append(expected[i]);
            if (i == expected.length - 1) {
                break;
            }
            result.append(" | ");
        }
        return result.toString();
    }
}
