package com.duy.pascal.frontend.view.exec_screen.console;

/**
 * Created by Duy on 05-Apr-17.
 */

public class StringCompare {
    public static boolean lessThan(String s1, String s2) {
        return s1.compareTo(s2) < 0;
    }

    public static boolean greaterThan(String s1, String s2) {
        return s1.compareTo(s2) > 0;
    }

    public static boolean greaterEqual(String s1, String s2) {
        return greaterThan(s1, s2) || s1.equals(s2);
    }
}
