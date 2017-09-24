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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;


public class ExpectedTokenException extends ParsingException {
    @NonNull
    private String[] expected;
    @NonNull
    private String current;

    public ExpectedTokenException(@NonNull String expected, @NonNull Token current) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[]{expected};
    }

    public ExpectedTokenException(@NonNull Token expected, @NonNull Token current) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[]{expected.toString()};
    }

    public ExpectedTokenException(@NonNull Token current, @NonNull String... expectToken) {
        super(current.getLineNumber());
        this.current = current.toString();
        this.expected = new String[expectToken.length];
        System.arraycopy(expectToken, 0, this.expected, 0, expectToken.length);
    }

    @NonNull
    public String[] getExpected() {
        return this.expected;
    }

    public void setExpected(@NonNull String[] var1) {
        this.expected = var1;
    }

    @NonNull
    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(@NonNull String var1) {
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
