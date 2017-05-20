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

package com.duy.pascal.backend.exceptions.operator;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime_value.RuntimeValue;

public class InvalidAddressOperation extends ParsingException {

    public InvalidAddressOperation(LineInfo line, RuntimeValue v) {
        super(line, "The expression " + v + " cannot have its address taken.");
    }

    public InvalidAddressOperation(LineInfo line) {
        super(line);
    }
}
