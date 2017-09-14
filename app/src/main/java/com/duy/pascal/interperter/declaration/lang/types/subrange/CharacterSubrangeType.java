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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.index.LowerGreaterUpperBoundException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;

public class CharacterSubrangeType extends SubrangeType<Character> {

    public CharacterSubrangeType(Character first, Character last)
            throws LowerGreaterUpperBoundException {
        super(first, last);
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws Exception {
        return BasicType.Character.convert(other, f);
    }

    @Override
    public boolean contains(SubrangeType other) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return Character.class;
    }
}
