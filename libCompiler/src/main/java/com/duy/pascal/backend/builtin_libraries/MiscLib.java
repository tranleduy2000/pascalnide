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


import com.duy.pascal.backend.builtin_libraries.annotations.ArrayBoundsInfo;
import com.duy.pascal.backend.builtin_libraries.annotations.MethodTypeData;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;

import java.util.Map;

public class MiscLib implements PascalLibrary {

    @MethodTypeData(info = {@ArrayBoundsInfo(starts = {0}, lengths = {0})})
    public long GetArrayLength(Object[] o) {
        return o.length;
    }

    @MethodTypeData(info = {@ArrayBoundsInfo(starts = {0}, lengths = {0})})
    public int length(Object[] o) {
        return o.length;
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
}
