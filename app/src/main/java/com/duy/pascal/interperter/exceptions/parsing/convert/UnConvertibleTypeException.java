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

package com.duy.pascal.interperter.exceptions.parsing.convert;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public final class UnConvertibleTypeException extends ParsingException {
    @NotNull
    private RuntimeValue value;
    @Nullable
    private Type valueType;
    @NotNull
    private Type targetType;
    @Nullable
    private RuntimeValue identifier;
    @NotNull
    private ExpressionContext scope;

    public UnConvertibleTypeException(@NotNull RuntimeValue value, @NotNull Type targetType, @NotNull Type valueType, @NotNull ExpressionContext scope) {
        super(value.getLineNumber(), "The expression or variable \"" + value + "\" is of type \"" + valueType + "\"" + ", which cannot be converted to the type \"" + targetType + "\"");
        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
        this.scope = scope;
    }

    public UnConvertibleTypeException(@NotNull RuntimeValue value, @NotNull Type identifierType, @Nullable Type valueType, @NotNull RuntimeValue identifier, @NotNull ExpressionContext scope) {
        super(value.getLineNumber(), "The expression or variable \"" + value + "\" is of type \"" + valueType + "\"" + ", which cannot be " + "converted to the type \"" + identifierType + "\" of expression or variable " + identifier);
        this.value = value;
        this.valueType = valueType;
        this.targetType = identifierType;
        this.identifier = identifier;
        this.scope = scope;
    }

    @NotNull
    public final RuntimeValue getValue() {
        return this.value;
    }

    public final void setValue(@NotNull RuntimeValue var1) {
        this.value = var1;
    }

    @Nullable
    public final Type getValueType() {
        return this.valueType;
    }

    public final void setValueType(@Nullable Type var1) {
        this.valueType = var1;
    }

    @NotNull
    public final Type getTargetType() {
        return this.targetType;
    }

    public final void setTargetType(@NotNull Type var1) {
        this.targetType = var1;
    }

    @Nullable
    public final RuntimeValue getIdentifier() {
        return this.identifier;
    }

    public final void setIdentifier(@Nullable RuntimeValue var1) {
        this.identifier = var1;
    }

    @NotNull
    public final ExpressionContext getScope() {
        return this.scope;
    }

    public final void setScope(@NotNull ExpressionContext var1) {
        this.scope = var1;
    }

    public boolean getCanAutoFix() {
        if (!(identifier instanceof VariableAccess) && !(value instanceof VariableAccess)) {
            return false;
        }
        return true;
    }
}