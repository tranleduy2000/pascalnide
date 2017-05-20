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

package com.js.interpreter.runtime_value.operators.pointer;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.AssignableValue;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class AddressEval extends DebuggableReturnValue {
    final AssignableValue target;
    final LineInfo line;

    public AddressEval(AssignableValue target, LineInfo line) {
        this.target = target;
        this.line = line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        return target.getReference(f, main);
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(new PointerType(target.getType(f).declType), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        return null;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        return this;
    }
}
