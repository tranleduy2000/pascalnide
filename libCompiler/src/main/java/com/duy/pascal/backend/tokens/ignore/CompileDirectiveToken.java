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

package com.duy.pascal.backend.tokens.ignore;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;

public class CompileDirectiveToken extends Token {
    public String message;

    public CompileDirectiveToken(LineInfo line, String message) {
        super(line);
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" + message + "}";
    }

    @Override
    public String toCode() {
        return toString();
    }

    public String getMessage() {
        return message;
    }
}
