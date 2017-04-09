package com.duy.pascal.backend.lib;

import java.util.ArrayList;

/**
 * Created by Duy on 08-Apr-17.
 */

public class LibraryUtils {
    public static final ArrayList<String> SUPPORT_LIB;

    static {
        SUPPORT_LIB = new ArrayList<>();
        SUPPORT_LIB.add("crt");
        SUPPORT_LIB.add("dos");
        SUPPORT_LIB.add("math");
        SUPPORT_LIB.add("graph");
    }
}
