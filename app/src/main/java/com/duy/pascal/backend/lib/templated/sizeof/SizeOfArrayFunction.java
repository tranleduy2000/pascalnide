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

package com.duy.pascal.backend.lib.templated.sizeof;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.lib.templated.abstract_class.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;

public class SizeOfArrayFunction implements IMethodDeclaration {

    private static final String TAG = "LengthFunction";
    private ArgumentType[] argumentTypes = {
            new RuntimeType(new ArrayType<>(BasicType.create(Object.class), new SubrangeType(0, -1)),
                    false)};

    @Override
    public String name() {
        return "sizeof";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, RValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        RValue array = arguments[0];
        return new SizeOfArrayCall(array, line);
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
