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
import com.duy.pascal.backend.lib.templated.abstract_class.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;

public class NewFunction implements IMethodDeclaration {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new PointerType(BasicType.create(Object.class)), true)};

    @Override
    public String name() {
        return "new";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RValue pointer = arguments[0];
        RuntimeType type = pointer.get_type(f);
        return new NewCall(pointer, type, line);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, RValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType returnType() {
        return BasicType.Integer;
    }

}
