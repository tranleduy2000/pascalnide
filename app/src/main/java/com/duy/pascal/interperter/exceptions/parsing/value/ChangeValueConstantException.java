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

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import org.jetbrains.annotations.Nullable;


public final class ChangeValueConstantException extends ParsingException {
    @NonNull
    private String name;
    @NonNull
    private ConstantAccess constant;
    @NonNull
    private ExpressionContext scope;

    public ChangeValueConstantException(@NonNull ConstantAccess var1, @NonNull ExpressionContext scope) {
        super(var1.getLineNumber());
        this.constant = var1;
        this.scope = scope;
        this.name = "";
    }

    @NonNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NonNull String var1) {
        this.name = var1;
    }

    @Nullable
    public String getMessage() {
        return "can not change value of constant " + this.constant;
    }

    public boolean getCanAutoFix() {
        return true;
    }

    @NonNull
    public final ConstantAccess getConst() {
        return this.constant;
    }

    public final void setConst(@NonNull ConstantAccess var1) {
        this.constant = var1;
    }

    @NonNull
    public final ExpressionContext getScope() {
        return this.scope;
    }

    public final void setScope(@NonNull ExpressionContext var1) {
        this.scope = var1;
    }
}
