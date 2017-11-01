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

package com.duy.pascal.ui.autocomplete.completion;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.ArgumentType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.ui.autocomplete.completion.model.ConstantDescription;
import com.duy.pascal.ui.autocomplete.completion.model.Description;
import com.duy.pascal.ui.autocomplete.completion.model.FunctionDescription;
import com.duy.pascal.ui.autocomplete.completion.model.VariableDescription;

/**
 * Created by Duy on 11/1/2017.
 */

public class CompletionFactory {
    public static Description makeConstant(ConstantDefinition constant) {
        return new ConstantDescription(constant);
    }

    public static Description makeVariable(VariableDeclaration variable) {
        Name name = variable.getName();
        return new VariableDescription(name, variable.getDescription(), variable.getType());
    }

    public static Description makeFunction(AbstractFunction function) {
        Name name = function.getName();
        ArgumentType[] args = function.argumentTypes();
        Type type = function.returnType();
        return new FunctionDescription(name, args, type);
    }
}
