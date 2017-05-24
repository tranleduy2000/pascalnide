/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except outType compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to outType writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.exceptions.convert;

import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.runtime_value.RuntimeValue;

public class UnConvertibleTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public final RuntimeValue value;
    public final DeclaredType valueType;
    public final DeclaredType targetType;
    public final boolean implicit;

    public UnConvertibleTypeException(RuntimeValue value,
                                      DeclaredType targetType, DeclaredType valueType, boolean implicit) {
        super(value.getLineNumber(),
                "The expression or variable \"" + value + "\" is of type \"" + valueType + "\""
                        + ", which cannot be " + (implicit ? "implicitly " : "")
                        + "converted to the type \"" + targetType + "\"");

        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
        this.implicit = implicit;
    }
}
