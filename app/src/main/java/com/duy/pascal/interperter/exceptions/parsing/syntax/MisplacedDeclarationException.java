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

package com.duy.pascal.interperter.exceptions.parsing.syntax;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import android.support.annotation.NonNull;


public  class MisplacedDeclarationException extends ParsingException {
    public MisplacedDeclarationException(@NonNull LineInfo line, @NonNull String declarationType, @NonNull ExpressionContext loc) {
        super(line, "Definition of " + declarationType + " is not appropriate here: " + loc);
    }

    public MisplacedDeclarationException(@NonNull LineInfo line, @NonNull String declarationType, @NonNull String loc) {
        super(line, "Definition of " + declarationType + " is not appropriate here: " + loc);
    }
}
