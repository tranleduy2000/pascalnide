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

package com.duy.pascal.interperter.ast.runtime_value.value.cloning;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerSubrangeType;

import java.util.LinkedList;

/**
 * Created by Duy on 11-Jun-17.
 */

public class SetToDynamicArrayCloner implements RuntimeValue {
    private RuntimeValue array;

    public SetToDynamicArrayCloner(RuntimeValue array) {
        this.array = array;
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        RuntimeType type = array.getRuntimeType(f);
        SetType setType = (SetType) type.declType;
        return new RuntimeType(new ArrayType<>(setType.getElementType(),
                new IntegerSubrangeType(0, setType.getSize())), false); //dynamic array, non writable
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        LinkedList arr = (LinkedList) array.getValue(f, main);
        return arr.toArray().clone();

    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return array.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        LinkedList value = (LinkedList) array.compileTimeValue(context);
        if (value == null) {
            return NullValue.get();
        }
        return value.toArray().clone();
    }

    @Override
    public String toString() {
        return array.toString();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new SetToDynamicArrayCloner(array.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
