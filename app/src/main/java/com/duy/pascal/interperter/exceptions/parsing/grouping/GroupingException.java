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

package com.duy.pascal.interperter.exceptions.parsing.grouping;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;

import org.jetbrains.annotations.Nullable;

public class GroupingException extends ParsingException {
    private Type exceptionTypes;
    @Nullable
    private Token openToken;
    @Nullable
    private Token closeToken;
    private Exception caused;

    public GroupingException(@Nullable LineInfo line, @NonNull String message) {
        super(line, message);
    }

    public GroupingException(@Nullable LineInfo line) {
        super(line);
    }

    public GroupingException(@Nullable LineInfo line, @NonNull Type exceptionTypes, @NonNull Token openToken, @NonNull Token closeToken) {
        super(line);
        this.exceptionTypes = exceptionTypes;
        this.openToken = openToken;
        this.closeToken = closeToken;
    }

    public GroupingException(@Nullable LineInfo line, @NonNull Type exceptionTypes) {
        super(line);
        this.exceptionTypes = exceptionTypes;
    }

    @Nullable
    public final Type getExceptionTypes() {
        return this.exceptionTypes;
    }

    public final void setExceptionTypes(@Nullable Type var1) {
        this.exceptionTypes = var1;
    }

    @Nullable
    public final Token getOpenToken() {
        return this.openToken;
    }

    public final void setOpenToken(@Nullable Token var1) {
        this.openToken = var1;
    }

    @Nullable
    public final Token getCloseToken() {
        return this.closeToken;
    }

    public final void setCloseToken(@Nullable Token var1) {
        this.closeToken = var1;
    }

    @Nullable
    public final Exception getCaused() {
        return this.caused;
    }

    public final void setCaused(@Nullable Exception var1) {
        this.caused = var1;
    }

    @Nullable
    public String getMessage() {
        return exceptionTypes.message + ": " + (caused != null ? caused.getMessage() : "");
    }

    public boolean getCanAutoFix() {
        return this.exceptionTypes == Type.UNFINISHED_BEGIN_END;
    }


    public enum Type {
        MISMATCHED_PARENTHESES("Mismatched parentheses"),
        MISMATCHED_BRACKETS("Mismatched brackets"),
        MISMATCHED_BEGIN_END("Mismatched begin - end construct"),
        UNFINISHED_BEGIN_END("Unfinished begin - end construct"),
        UNFINISHED_PARENTHESES("You forgot to close your parentheses"),
        UNFINISHED_BRACKETS("You forgot to close your brackets"),
        EXTRA_END("You have an extra 'end' in your program"),
        UNFINISHED_CONSTRUCT("You forgot to complete the structure you started here"),
        IO_EXCEPTION("IOException occurred while reading the input"),
        INCOMPLETE_CHAR("Incomplete character literal"),
        MISSING_INCLUDE("Missing file to include"),
        NEWLINE_IN_QUOTES("You must close your quotes before starting a new line");

        String message;

        Type(String message) {
            this.message = message;
        }
    }

}
