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

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class OverridingFunctionBodyException extends ParsingException {
    @NonNull
    private AbstractFunction functionDeclaration;
    private boolean isMethod;

    public OverridingFunctionBodyException(@NonNull FunctionDeclaration old, @NonNull LineInfo line) {
        super(line, "Redefining function body for " + old + ", which was previous define at " + old.getLineNumber());
        this.functionDeclaration = (AbstractFunction) old;
    }

    public OverridingFunctionBodyException(@NonNull AbstractFunction old, @NonNull FunctionDeclaration news) {
        super(news.getLineNumber(), "Attempting to override plugin definition" + old);
        this.functionDeclaration = old;
        this.isMethod = true;
    }

    @NonNull
    public AbstractFunction getFunctionDeclaration() {
        return this.functionDeclaration;
    }

    public void setFunctionDeclaration(@NonNull AbstractFunction var1) {
        this.functionDeclaration = var1;
    }

    public boolean isMethod() {
        return this.isMethod;
    }

    public void setMethod(boolean var1) {
        this.isMethod = var1;
    }
}
