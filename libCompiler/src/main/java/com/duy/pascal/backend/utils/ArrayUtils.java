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

import android.support.annotation.NonNull;

import com.duy.pascal.frontend.view.exec_screen.console.TextConsole;

import java.util.Arrays;

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
}
