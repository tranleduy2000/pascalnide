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

package com.duy.pascal.interperter.exceptions.parsing.define;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public  class BadFunctionCallException extends ParsingException {
    private boolean argsMatch;
    @Nullable
    private List args;
    @NotNull
    private Name functionName;
    private boolean functionExists;
    @NotNull
    private ArrayList functions;
    @NotNull
    private ExpressionContext scope;

    public BadFunctionCallException(@NotNull LineInfo line, @NotNull Name functionName, boolean functionExists, boolean numargsMatch, @NotNull ArrayList args, @NotNull ArrayList functions, @NotNull ExpressionContext scope) {
        super(line);
        this.functionName = functionName;
        this.functionExists = functionExists;
        this.functions = functions;
        this.scope = scope;
        this.argsMatch = numargsMatch;
        this.args = (List) args;
    }

    public final boolean getArgsMatch() {
        return this.argsMatch;
    }

    public final void setArgsMatch(boolean var1) {
        this.argsMatch = var1;
    }

    @Nullable
    public final List getArgs() {
        return this.args;
    }

    public final void setArgs(@Nullable List var1) {
        this.args = var1;
    }

    @Nullable
    public String getMessage() {
        return this.functionExists ? (this.argsMatch ? "One or more arguments has an incorrect type when calling function \"" + this.functionName + "\"." : "Either too few or two many arguments are being passed to function \"" + this.functionName + "\".") : "Can not call function or procedure \"" + this.functionName + "\", which is not defined.";
    }

    @NotNull
    public final Name getFunctionName() {
        return this.functionName;
    }

    public final void setFunctionName(@NotNull Name var1) {
        this.functionName = var1;
    }

    public final boolean getFunctionExists() {
        return this.functionExists;
    }

    public final void setFunctionExists(boolean var1) {
        this.functionExists = var1;
    }

    @NotNull
    public final ArrayList getFunctions() {
        return this.functions;
    }

    public final void setFunctions(@NotNull ArrayList var1) {
        this.functions = var1;
    }

    @NotNull
    public final ExpressionContext getScope() {
        return this.scope;
    }

    public final void setScope(@NotNull ExpressionContext var1) {
        this.scope = var1;
    }
}
