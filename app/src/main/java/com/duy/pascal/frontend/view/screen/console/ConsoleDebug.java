package com.duy.pascal.frontend.view.screen.console;

/**
 * Created by Duy on 26-Mar-17.
 */

public class ConsoleDebug {
    public static String bytesToString(byte[] data, int base, int length) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = data[base + i];
            if (b < 32 || b > 126) {
                buf.append(String.format("\\x%02x", b));
            } else {
                buf.append((char)b);
            }
        }
        return buf.toString();
    }
}
