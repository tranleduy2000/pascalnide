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
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class SetArrayLengthFunction implements TemplatePascalPlugin {

    SetLengthFunction a = new SetLengthFunction();

    @Override
    public String name() {
        return a.name();
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnsValue[] values, ExpressionContext f) throws ParsingException {
        return a.generateCall(line, values, f);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               ReturnsValue[] values, ExpressionContext f) throws ParsingException {
        return a.generatePerfectFitCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return a.argumentTypes();
    }

    @Override
    public DeclaredType return_type() {
        return a.return_type();
    }

}
