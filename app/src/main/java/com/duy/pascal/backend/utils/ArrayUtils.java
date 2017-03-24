package com.duy.pascal.backend.utils;

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
}
