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

package com.duy.pascal.interperter.exceptions.parsing.operator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

public class BadOperationTypeException extends ParsingException {
    @Nullable
    private Type declaredType;
    @Nullable
    private Type declaredType1;
    @Nullable
    private RuntimeValue value1;
    @Nullable
    private RuntimeValue value2;
    @Nullable
    private OperatorTypes operatorTypes;

    public BadOperationTypeException() {
        super(new LineInfo(-1, "Unknown"));
    }

    public BadOperationTypeException(@NonNull LineInfo line, @NonNull Type t1, @NonNull Type t2, @Nullable RuntimeValue v1, @Nullable RuntimeValue v2, @NonNull OperatorTypes operation) {
        super(line, "Operator " + operation + " cannot be applied to arguments \'" + v1 + "\' and \'" + v2 + "\'.  One has type " + t1 + " and the other has type " + t2 + ".");
        this.value1 = v1;
        this.value2 = v2;
        this.operatorTypes = operation;
        this.declaredType = t1;
        this.declaredType1 = t2;
    }

    public BadOperationTypeException(@NonNull LineInfo line, @NonNull Type t1, @NonNull RuntimeValue v1, @NonNull OperatorTypes operation) {
        super(line, "Operator " + operation + " cannot be applied to argument \'" + v1 + "\' of type " + t1 + ".");
    }

    public BadOperationTypeException(@NonNull LineInfo line, @NonNull OperatorTypes operator) {
        super(line, "Operator " + operator + " is not a unary operator.");
    }

    @Nullable
    public final Type getDeclaredType() {
        return this.declaredType;
    }

    public final void setDeclaredType(@Nullable Type var1) {
        this.declaredType = var1;
    }

    @Nullable
    public final Type getDeclaredType1() {
        return this.declaredType1;
    }

    public final void setDeclaredType1(@Nullable Type var1) {
        this.declaredType1 = var1;
    }

    @Nullable
    public final RuntimeValue getValue1() {
        return this.value1;
    }

    public final void setValue1(@Nullable RuntimeValue var1) {
        this.value1 = var1;
    }

    @Nullable
    public final RuntimeValue getValue2() {
        return this.value2;
    }

    public final void setValue2(@Nullable RuntimeValue var1) {
        this.value2 = var1;
    }

    @Nullable
    public final OperatorTypes getOperatorTypes() {
        return this.operatorTypes;
    }

    public final void setOperatorTypes(@Nullable OperatorTypes var1) {
        this.operatorTypes = var1;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        BadOperationTypeException e = this;
        String message;
        if (e.getValue1() == null) {
            message = String.format(context.getString(R.string.BadOperationTypeException2),
                    e.getOperatorTypes());
        } else {
            message = String.format(context.getString(R.string.BadOperationTypeException),
                    e.getOperatorTypes(), e.getValue1(), e.getValue2(), e.getDeclaredType(), e.getDeclaredType1());
        }
        String line = formatLine(context, e.getLineNumber());

        SpannableStringBuilder builder = new SpannableStringBuilder(line);
        builder.append(line);
        builder.append("\n\n");
        builder.append(message);
        return highlight(context, builder);
    }
}
