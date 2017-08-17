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

package com.duy.pascal.interperter.declaration.lang.types.subrange;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.index.LowerGreaterUpperBoundException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;

public class BooleanSubrangeType extends SubrangeType<Boolean> {

    public BooleanSubrangeType(Boolean first, Boolean last) throws LowerGreaterUpperBoundException {
        super(first, last);
        this.first = first;
        this.last = last;
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws Exception {
        return BasicType.Boolean.convert(other, f);
    }

    @Override
    public boolean contains(SubrangeType other) {
        return other instanceof BooleanSubrangeType
                && first.compareTo(((BooleanSubrangeType) other).first) <= 0
                && last.compareTo(((BooleanSubrangeType) other).last) >= 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + first.hashCode();
        result = prime * result + last.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return null;
    }

    @Nullable
    @Override
    public Class<?> getStorageClass() {
        return Boolean.class;
    }
}
