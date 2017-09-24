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

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.tokens.Token;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;


public final class ExpectedTokenException extends ParsingException {
    @NotNull
    private String[] expected;
    @NotNull
    private String current;

    public ExpectedTokenException(@NotNull String expected, @NotNull Token current) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[]{expected};
    }

    public ExpectedTokenException(@NotNull Token expected, @NotNull Token current) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[]{expected.toString()};
    }

    public ExpectedTokenException(@NotNull Token current, @NotNull String... expectToken) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[expectToken.length];
        System.arraycopy(expectToken, 0, this.expected, 0, expectToken.length);
    }

    @NotNull
    public final String[] getExpected() {
        return this.expected;
    }

    public final void setExpected(@NotNull String[] var1) {
        this.expected = var1;
    }

    @NotNull
    public final String getCurrent() {
        return this.current;
    }

    public final void setCurrent(@NotNull String var1) {
        this.current = var1;
    }


    @Override
    @Nullable
    public String getMessage() {
        return "Syntax error, \"" + Arrays.toString(this.expected) + "\" expected but \"" + this.current + "\" found";
    }

    public boolean getCanAutoFix() {
        return true;
    }
}
