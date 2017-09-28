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

package com.duy.pascal.interperter.exceptions.parsing.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;


public class ChangeValueConstantException extends ParsingException {
    @NonNull
    private String name;
    @NonNull
    private ConstantAccess constant;
    @NonNull
    private ExpressionContext scope;

    public ChangeValueConstantException(@NonNull ConstantAccess c, @NonNull ExpressionContext scope) {
        super(c.getLineNumber());
        this.constant = c;
        this.scope = scope;
        this.name = "";
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public void setName(@NonNull String var1) {
        this.name = var1;
    }

    @Nullable
    public String getMessage() {
        return "can not change value of constant " + this.constant;
    }

    public boolean canQuickFix() {
        return true;
    }

    @NonNull
    public ConstantAccess getConst() {
        return this.constant;
    }

    public void setConst(@NonNull ConstantAccess var1) {
        this.constant = var1;
    }

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }

    public void setScope(@NonNull ExpressionContext var1) {
        this.scope = var1;
    }
}
