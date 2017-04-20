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

import com.duy.pascal.frontend.view.exec_screen.console.TextConsole;

/**
 * Created by Duy on 24-Mar-17.
 */

public class ArrayUtils {
    public static String arrayToString(Object[] array) {
        StringBuilder res = new StringBuilder();
        res.append("[");
        for (Object var : array) {
            if (var instanceof Object[]) {
                String tmp = arrayToString((Object[]) var);
                res.append(tmp);
            } else {
                res.append(var.toString()).append(", ");
            }
        }
        res.append("]");
        return res.toString();
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
}
