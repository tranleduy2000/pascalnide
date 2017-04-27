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

package com.duy.pascal.backend.lib;

import android.os.Build;

import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.js.interpreter.runtime.VariableBoxer;

import java.util.Date;
import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */

public class DosLib implements PascalLibrary {

    public static final String NAME = "dos";

    public DosLib() {

    }

    /**
     * return system time
     */
    @PascalMethod(description = "Dos library", returns = "void")
    @SuppressWarnings("unused")
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
     */
    @PascalMethod(description = "Dos library", returns = "void")
    @SuppressWarnings("unused")
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

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return true;
    }

    @Override
    public void shutdown() {

    }

    @PascalMethod(description = "Dos library", returns = "void")
    @SuppressWarnings("unused")
    public int dosVersion() {
        return Build.VERSION.SDK_INT;
    }
}
