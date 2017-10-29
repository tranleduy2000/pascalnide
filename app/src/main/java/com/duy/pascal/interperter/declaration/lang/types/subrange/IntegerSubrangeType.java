/*
 *  Copyright 2017 Tran Le Duy
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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;

public class IntegerSubrangeType extends com.duy.pascal.interperter.declaration.lang.types.subrange.SubrangeType<Integer> implements com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerRange {
    private static final String TAG = "IntegerSubrangeType";
    public Integer size = 0;

    public IntegerSubrangeType(Integer first, @IntRange(from = 0) Integer size) {
        super(first, first + size - 1);
        this.size = size;
    }

    @Override
    public boolean contains(com.duy.pascal.interperter.declaration.lang.types.subrange.SubrangeType other) {
        return other instanceof IntegerSubrangeType
                && first <= ((IntegerSubrangeType) other).first
                && last >= ((IntegerSubrangeType) other).last;
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws Exception {
        return BasicType.Integer.convert(other, f);
    }

    @Override
    public int getFirst() {
        return first;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + first;
        result = prime * result + size;
        return result;
    }

    @Override
    public String toString() {
        return first + ".." + last;
    }

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return Integer.class;
    }
}
