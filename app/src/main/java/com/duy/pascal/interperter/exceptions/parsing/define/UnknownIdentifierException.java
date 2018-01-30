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
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;


public class UnknownIdentifierException extends ParsingException {
    @Nullable
    private final DefineType fitType;
    @NonNull
    private final Name name;
    @NonNull
    private final ExpressionContext scope;

    public UnknownIdentifierException(@Nullable LineNumber line, @NonNull Name name, @NonNull ExpressionContext scope) {
        super(line, "Unknown identifier " + name);
        this.name = name;
        this.scope = scope;
        LineNumber lineNumber = this.getLineNumber();
        if (lineNumber != null) {
            lineNumber.setLength(this.name.getLength());
        }
        this.fitType = DefineType.DECLARE_VAR;
    }

    @Nullable
    public DefineType getFitType() {
        return this.fitType;
    }

    public boolean canQuickFix() {
        return true;
    }

    @NonNull
    public Name getName() {
        return this.name;
    }

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        return formatMessageFromResource(
                this,
                context,
                R.string.NoSuchFunctionOrVariableException,
                getName());
    }

    /**
     * Created by Duy on 23-May-17.
     */
    public enum DefineType {
        DECLARE_TYPE, DECLARE_VAR, DECLARE_FUNCTION, DECLARE_PROCEDURE, DECLARE_CONST
    }
}
