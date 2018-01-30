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

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;

public class OverridingFunctionBodyException extends ParsingException {
    @NonNull
    private final AbstractFunction functionDeclaration;
    private boolean isMethod;

    public OverridingFunctionBodyException(@NonNull FunctionDeclaration old, @NonNull LineNumber line) {
        super(line, String.format("Redefining function body for %s, which was previous define at %s",
                old, old.getLineNumber()));
        this.functionDeclaration = old;
    }

    public OverridingFunctionBodyException(@NonNull AbstractFunction old, @NonNull FunctionDeclaration news) {
        super(news.getLineNumber(), String.format("Attempting to override plugin definition %s", old));
        this.functionDeclaration = old;
        this.isMethod = true;
    }

    @NonNull
    public AbstractFunction getFunctionDeclaration() {
        return this.functionDeclaration;
    }

    public boolean isMethod() {
        return this.isMethod;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        if (isMethod()) {
            return formatMessageFromResource(this, context, R.string.OverridingFunctionException);
        } else {
            return formatMessageFromResource(this, context, R.string.OverridingFunctionException,
                    getFunctionDeclaration().getName(),
                    getFunctionDeclaration().getLineNumber());
        }
    }
}
