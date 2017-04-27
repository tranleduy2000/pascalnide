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

package com.duy.pascal.backend.lib.templated;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.lib.templated.abstract_class.TemplatePascalPlugin;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class SetLengthFunction implements TemplatePascalPlugin {

    private ArgumentType[] argumentTypes =
            {new RuntimeType(new ArrayType<>(BasicType.anew(Object.class), new IntegerSubrangeType(0, 0)), true),
            new RuntimeType(BasicType.Integer, false)
    };

    @Override
    public String name() {
        return "setlength";
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnsValue[] arguments,
                                     ExpressionContext f) throws ParsingException {
        ReturnsValue array = arguments[0];
        ReturnsValue size = arguments[1];
        @SuppressWarnings("rawtypes")
        DeclaredType elemtype = ((ArrayType) ((PointerType) array.getType(f).declaredType).pointedToType).elementType;
        LineInfo l = line;
        return new SetLengthCall(array, size, elemtype, l);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line, ReturnsValue[] values, ExpressionContext f) throws ParsingException {
        return generateCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return argumentTypes;
    }

    @Override
    public DeclaredType return_type() {
        return null;
    }

}
