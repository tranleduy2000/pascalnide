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

package com.duy.pascal.backend.exceptions.convert;

import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.runtime_value.RuntimeValue;

public class UnConvertibleTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public final RuntimeValue obj;
    public final DeclaredType out;
    public final DeclaredType in;
    public final boolean implicit;

    public UnConvertibleTypeException(RuntimeValue obj,
                                      DeclaredType out, DeclaredType in, boolean implicit) {
        super(obj.getLineNumber(),
                "The expression or variable \"" + obj + "\" is of type \"" + out + "\""
                        + ", which cannot be " + (implicit ? "implicitly " : "")
                        + "converted to the type \"" + in + "\"");

        this.obj = obj;
        this.out = out;
        this.in = in;
        this.implicit = implicit;
    }
}
