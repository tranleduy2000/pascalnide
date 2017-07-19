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

package com.duy.pascal.interperter.ast.runtime_value.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;

/**
 * Created by Duy on 25-May-17.
 */

public class EnumElementValue implements RuntimeValue, Comparable<EnumElementValue> {
    private String name;
    private EnumGroupType type;
    private Integer value;

    private Integer index;
    private LineInfo lineInfo;

    public EnumElementValue(String name, @NonNull EnumGroupType type,
                            @NonNull Integer index, @NonNull LineInfo lineInfo) {
        this.name = name;
        this.type = type;
        this.index = index;
        this.lineInfo = lineInfo;
    }

    public EnumGroupType getEnumGroupType() {
        return type;
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        return index;
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(type, false);//this is a constant
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Nullable
    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        return this;//this is a constant
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return this;
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * uses for print to console
     */
    @Override
    public String toString() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull EnumElementValue o) {
        return this.getIndex().compareTo(o.getIndex());
    }
}
