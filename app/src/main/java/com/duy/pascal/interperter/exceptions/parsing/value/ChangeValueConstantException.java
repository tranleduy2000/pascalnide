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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;


public class ChangeValueConstantException extends ParsingException {
    @NonNull
    private final ConstantAccess constant;
    @NonNull
    private final ExpressionContext scope;

    public ChangeValueConstantException(@NonNull ConstantAccess c, @NonNull ExpressionContext scope) {
        super(c.getLineNumber());
        this.constant = c;
        this.scope = scope;
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

    @NonNull
    public ExpressionContext getScope() {
        return this.scope;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        String name = getConst().getName().getOriginName();
        return formatMessageFromResource(this, context, R.string.ChangeValueConstantException2, name);
    }
}
