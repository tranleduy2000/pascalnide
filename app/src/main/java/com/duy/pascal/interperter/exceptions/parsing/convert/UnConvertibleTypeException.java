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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime.value.access.VariableAccess;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.utils.NullSafety;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.getMessageFromResource;


public class UnConvertibleTypeException extends ParsingException {
    @NonNull
    private RuntimeValue value;
    @Nullable
    private Type valueType;
    @NonNull
    private Type targetType;
    @Nullable
    private RuntimeValue identifier;
    @NonNull
    private ExpressionContext scope;

    public UnConvertibleTypeException(@NonNull RuntimeValue value, @NonNull Type targetType,
                                      @Nullable Type valueType, @NonNull ExpressionContext scope) {
        super(value.getLineNumber(), String.format("The expression or variable \"%s\" is of type \"%s\", which cannot be converted to the type \"%s\"", value, valueType, targetType));
        this.value = value;
        this.valueType = valueType;
        this.targetType = targetType;
        this.scope = scope;
    }

    public UnConvertibleTypeException(@NonNull RuntimeValue value, @NonNull Type identifierType,
                                      @Nullable Type valueType, @NonNull RuntimeValue identifier,
                                      @NonNull ExpressionContext scope) {
        super(value.getLineNumber(), String.format("The expression or variable \"%s\" is of type \"%s\", which cannot be converted to the type \"%s\" of expression or variable %s", value, valueType, identifierType, identifier));
        this.value = value;
        this.valueType = valueType;
        this.targetType = identifierType;
        this.identifier = identifier;
        this.scope = scope;
    }

    @NonNull
    public RuntimeValue getValue() {
        return this.value;
    }

    public void setValue(@NonNull RuntimeValue var1) {
        this.value = var1;
    }

    @Nullable
    public Type getValueType() {
        return this.valueType;
    }


    @NonNull
    public Type getTargetType() {
        return this.targetType;
    }


    @Nullable
    public RuntimeValue getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(@Nullable RuntimeValue var1) {
        this.identifier = var1;
    }

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }


    public boolean canQuickFix() {
        return identifier instanceof VariableAccess
                || value instanceof VariableAccess
                || identifier instanceof ConstantAccess;
    }

    @Override
    public Spanned getLocalizedMessage(@NonNull Context context) {
        UnConvertibleTypeException e = this;
        if (NullSafety.isNullPointer(e.getIdentifier())) {
            int id = R.string.UnConvertibleTypeException;
            return getMessageFromResource(e, context, id, e.getValue(), e.getValueType(), e.getTargetType());
        } else {
            int id;
            if (e.getIdentifier() instanceof VariableAccess) {
                id = R.string.UnConvertibleTypeVariable;
            } else if (e.getIdentifier() instanceof ConstantAccess) {
                id = R.string.UnConvertibleTypeConstant;
            } else {
                id = R.string.UnConvertibleTypeException2;
            }
            return getMessageFromResource(e, context, id, e.getValue(), e.getValueType(), e.getTargetType(), e.getIdentifier());
        }
    }
}