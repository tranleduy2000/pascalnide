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

package com.duy.pascal.backend.lib.templated.pointer;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.ObjectBasedPointer;
import com.js.interpreter.runtime.PascalPointer;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

class NewCall extends FunctionCall {

    private RValue value;
    private RuntimeType type;
    private LineInfo line;

    NewCall(RValue value, RuntimeType type, LineInfo line) {
        this.value = value;
        this.type = type;
        this.line = line;
    }

    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.Integer, false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return null;
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new NewCall(value, type, line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new NewCall(value, type, line);
    }

    @Override
    protected String getFunctionName() {
        return "new";
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        PascalPointer pointer = (PascalPointer) this.value.getValue(f, main);
        PointerType pointerType = (PointerType) ((PointerType) type.declType).pointedToType;
        DeclaredType basicType = pointerType.pointedToType;
        if (basicType instanceof ArrayType) {
            pointer.set(new ObjectBasedPointer<>(new Object[]{}));
        } else if (BasicType.Byte.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>((byte) 0));
        } else if (BasicType.Short.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>((short) 0));
        } else if (BasicType.Integer.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>(0));
        } else if (BasicType.Long.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>(0L));
        } else if (BasicType.Double.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>(0d));
        } else if (BasicType.Character.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>((char) 0));
        } else if (BasicType.StringBuilder.equals(basicType)) {
            pointer.set(new ObjectBasedPointer<>(""));
        }
        return 0;
    }
}
