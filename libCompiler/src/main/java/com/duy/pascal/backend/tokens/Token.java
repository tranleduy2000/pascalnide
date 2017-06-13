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

package com.duy.pascal.backend.tokens;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;

/**
 * Created by Duy on 24-May-17.
 */

public abstract class Token {
    @Nullable
    protected LineInfo mLineNumber;

    public Token(@Nullable LineInfo lineInfo) {
        this.mLineNumber = lineInfo;
    }

    @NonNull
    public WordToken getWordValue() throws ParsingException {
        throw new ExpectedTokenException("[Identifier]", this);
    }

    @Nullable
    public Precedence getOperatorPrecedence() {
        return null;
    }

    public boolean canDeclareInInterface() {
        return false;
    }

    @Nullable
    public LineInfo getLineNumber() {
        return this.mLineNumber;
    }

    public final void setLineNumber(@NonNull LineInfo var1) {
        this.mLineNumber = var1;
    }

    public String toCode() {
        return toString();
    }

    public enum Precedence {
        Dereferencing,
        Negation,
        Multiplicative,
        Additive,
        Relational,
        NoPrecedence;
    }
}
