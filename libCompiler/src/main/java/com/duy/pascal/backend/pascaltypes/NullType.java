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

package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;

/**
 * Created by Duy on 21-May-17.
 */
public class NullType implements DeclaredType {
    @Override
    public Object initialize() {
        return null;
    }

    @Override
    public Class getTransferClass() {
        return null;
    }

    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        return null;
    }

    @Override
    public boolean equals(DeclaredType other) {
        return false;
    }

    @Nullable
    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return null;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed {
        return null;
    }

    @Nullable
    @Override
    public Class<?> getStorageClass() {
        return null;
    }
}
