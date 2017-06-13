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

package com.duy.pascal.backend.ast.runtime_value.value.cloning;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.types.rangetype.IntegerSubrangeType;
import com.duy.pascal.backend.types.set.ArrayType;
import com.duy.pascal.backend.types.set.SetType;

import java.util.LinkedList;

/**
 * Created by Duy on 11-Jun-17.
 */

public class SetToDynamicArrayCloner implements RuntimeValue {
    protected RuntimeValue[] outputFormat;
    private RuntimeValue array;

    public SetToDynamicArrayCloner(RuntimeValue array) {
        this.array = array;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType type = array.getType(f);
        SetType setType = (SetType) type.declType;
        return new RuntimeType(new ArrayType<>(setType.getElementType(),
                new IntegerSubrangeType(0, setType.getSize())), false); //dynamic array, non writable
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        LinkedList arr = (LinkedList) array.getValue(f, main);
        return arr.toArray().clone();

    }

    @Override
    public LineInfo getLineNumber() {
        return array.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        LinkedList value = (LinkedList) array.compileTimeValue(context);
        return value.toArray().clone();
    }

    @Override
    public String toString() {
        return array.toString();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SetToDynamicArrayCloner(array.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
