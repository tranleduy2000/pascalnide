/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.interperter.ast.runtime.value.range;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.NullValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.utils.NullSafety;

/**
 * Created by Duy on 19-Jun-17.
 */

public class RangeRuntimeValue extends DebuggableReturnValue {
    private RuntimeValue first, last;

    public RangeRuntimeValue(RuntimeValue first, RuntimeValue last) {
        this.first = first;
        this.last = last;
    }


    @Override
    public boolean canDebug() {
        return false;
    }


    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object value = first.getValue(f, main);
        Object value1 = last.getValue(f, main);
        return new Pair<>(value, value1);
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return null;
    }

    @Nullable
    @Override
    public Object compileTimeValue(CompileTimeContext context) throws Exception {
        Object o = first.compileTimeValue(context);
        if (NullSafety.isNullValue(o)) return NullValue.get();
        Object o1 = last.compileTimeValue(context);
        if (NullSafety.isNullValue(o1)) return NullValue.get();
        return new Pair<>(o, o1);
    }

    @Nullable
    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws Exception {
        RuntimeValue o = first.compileTimeExpressionFold(context);
        RuntimeValue o1 = last.compileTimeExpressionFold(context);
        return new RangeRuntimeValue(o, o1);
    }

}
