package com.duy.pascal.backend.lib;


import com.duy.pascal.backend.lib.annotations.ArrayBoundsInfo;
import com.duy.pascal.backend.lib.annotations.MethodTypeData;

import java.util.Map;

public class MiscLib implements PascalLibrary {

    @MethodTypeData(info = {@ArrayBoundsInfo(starts = {0}, lengths = {0})})
    public static long GetArrayLength(Object[] o) {
        return o.length;
    }

    @MethodTypeData(info = {@ArrayBoundsInfo(starts = {0}, lengths = {0})})
    public static int length(Object[] o) {
        return o.length;
    }

    public static int length(StringBuilder s) {
        return s.length();
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }
}
