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
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.autocomplete.autofix.DefineType;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;


public class UnknownIdentifierException extends ParsingException {
    @Nullable
    private String token;
    @Nullable
    private DefineType fitType;
    @NonNull
    private Name name;
    @NonNull
    private ExpressionContext scope;

    public UnknownIdentifierException(@Nullable LineInfo line, @NonNull Name name, @NonNull ExpressionContext scope) {
        super(line, "Unknown identifier " + name);
        this.name = name;
        this.scope = scope;
        LineInfo lineInfo = this.getLineInfo();
        if (lineInfo != null) {
            lineInfo.setLength(this.name.getLength());
        }
        this.fitType = DefineType.DECLARE_VAR;
    }

    @Nullable
    public String getToken() {
        return this.token;
    }

    public void setToken(@Nullable String var1) {
        this.token = var1;
    }

    @Nullable
    public DefineType getFitType() {
        return this.fitType;
    }

    public void setFitType(@Nullable DefineType var1) {
        this.fitType = var1;
    }

    public boolean getCanAutoFix() {
        return true;
    }

    @NonNull
    public Name getName() {
        return this.name;
    }

    public void setName(@NonNull Name name) {
        this.name = name;
    }

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }

    public void setScope(@NonNull ExpressionContext var1) {
        this.scope = var1;
    }
}
