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

package com.js.interpreter.runtime_value;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.FieldReference;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.references.Reference;

public class VariableAccess extends DebuggableAssignableValue {
    private static final String TAG = "VariableAccess";
    private String name;
    private LineInfo line;

    public VariableAccess(WordToken t) {
        this.name = t.name;
        this.line = t.getLineNumber();
    }

    public VariableAccess(String name, LineInfo line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return super.getOutputFormat();
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        super.setOutputFormat(formatInfo);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return f.getVar(name);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (f instanceof FunctionOnStack) {
                ExpressionContextMixin declarations = ((FunctionOnStack) f).getPrototype().declarations;
                RuntimeType type = getType(declarations);
                return new FieldReference(f, name, type);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return new FieldReference(f, name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(f.getVariableDefinition(name).type, true);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }
}
