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

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.RecordType;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineInfo;


public class UnknownFieldException extends ParsingException {
    @NonNull
    private RecordType container;
    @NonNull
    private Name name;
    @NonNull
    private ExpressionContext scope;

    public UnknownFieldException(@Nullable LineInfo line, @NonNull RecordType container, @NonNull Name name, @NonNull ExpressionContext scope) {
        super(line);
        this.container = container;
        this.name = name;
        this.scope = scope;
    }

    @Nullable
    public String getMessage() {
        return "Can not find field \"" + this.name + "\" of \"" + this.container + '\"';
    }

    @NonNull
    public final RecordType getContainer() {
        return this.container;
    }

    public final void setContainer(@NonNull RecordType var1) {
        this.container = var1;
    }

    @NonNull
    public final Name getName() {
        return this.name;
    }

    public final void setName(@NonNull Name var1) {
        this.name = var1;
    }

    @NonNull
    public final ExpressionContext getScope() {
        return this.scope;
    }

    public final void setScope(@NonNull ExpressionContext var1) {
        this.scope = var1;
    }
}
