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

package com.duy.pascal.backend.builtin_libraries;

import android.os.Build;

import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;

import java.util.Date;
import java.util.Map;

/**
 * Created by Duy on 01-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class DosLib implements PascalLibrary {

    public static final String NAME = "dos";

    public DosLib() {

    }

    /**
     * return system time
     */
    @PascalMethod(description = "Dos library", returns = "void")
    @SuppressWarnings("unused")
    public static void getTime(PascalReference<Object> hour,
                               PascalReference<Object> minute,
                               PascalReference<Object> second,
                               PascalReference<Object> sec100) {
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
    public static void getDate(PascalReference<Object> year,
                               PascalReference<Object> month,
                               PascalReference<Object> mday,
                               PascalReference<Object> wday) {
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
    @PascalMethod(description = "stop")

    public void shutdown() {

    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    @PascalMethod(description = "Dos library", returns = "void")
    @SuppressWarnings("unused")
    public int dosVersion() {
        return Build.VERSION.SDK_INT;
    }
}
