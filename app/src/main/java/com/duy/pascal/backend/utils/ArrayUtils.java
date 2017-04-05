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
