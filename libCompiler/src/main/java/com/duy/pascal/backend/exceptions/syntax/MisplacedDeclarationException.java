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

package com.duy.pascal.backend.exceptions.syntax;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.ExpressionContext;

public class MisplacedDeclarationException extends com.duy.pascal.backend.exceptions.ParsingException {

    public MisplacedDeclarationException(LineInfo line, String declarationType,
                                         ExpressionContext loc) {
        super(line, "Definition of " + declarationType
                + " is not appropriate here: " + loc);
    }

    public MisplacedDeclarationException(LineInfo line, String declarationType,
                                         String loc) {
        super(line, "Definition of " + declarationType
                + " is not appropriate here: " + loc);
    }

}
