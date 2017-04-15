package com.duy.pascal.backend.lib;

import android.os.Build;

import com.js.interpreter.runtime.VariableBoxer;

import java.util.Date;
import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */

public class DosLib implements PascalLibrary {

    public DosLib() {

    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    /**
     * return system time
     */@SuppressWarnings("unused")
    public static void getTime(VariableBoxer<Object> hour,
                        VariableBoxer<Object> minute,
                        VariableBoxer<Object> second,
                        VariableBoxer<Object> sec100) {
        Date date = new Date();
        hour.set(date.getHours());
        minute.set(date.getMinutes());
        second.set(date.getSeconds());
        sec100.set(0);
    }

    /**
     * return system date
     */@SuppressWarnings("unused")
    public static void getDate(VariableBoxer<Object> year,
                        VariableBoxer<Object> month,
                        VariableBoxer<Object> mday,
                        VariableBoxer<Object> wday) {
        Date date = new Date();
        year.set(date.getYear());
        month.set(date.getMonth());
        mday.set(date.getDate());
        wday.set(date.getDay());
    }
    @SuppressWarnings("unused")
    public int dosVersion() {
        return Build.VERSION.SDK_INT;
    }
}
