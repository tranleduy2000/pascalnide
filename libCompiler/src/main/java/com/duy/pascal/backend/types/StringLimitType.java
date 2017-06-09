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

package com.duy.pascal.backend.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.StringIndex;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.CharacterBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.StringBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.StringBuilderCloner;
import com.duy.pascal.backend.types.converter.StringLimitBoxer;
import com.duy.pascal.backend.types.converter.TypeConverter;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;

/**
 * Created by Duy on 26-May-17.
 */

public class StringLimitType extends InfoType {

    private RuntimeValue length = null; //max size
    private Class clazz = StringBuilder.class;

    @Override
    public String toString() {
        return "String";
    }

    @Override
    public RuntimeValue convert(RuntimeValue valueToAssign, ExpressionContext f)
            throws ParsingException {
        RuntimeType otherType = valueToAssign.getType(f);
        if (this.equals(otherType.declType)) {
            return new StringLimitBoxer(valueToAssign, length);
        }

        if (otherType.declType instanceof BasicType) {
            if (otherType.declType == BasicType.StringBuilder) {
                return new StringLimitBoxer(valueToAssign, length);
            }
            if (otherType.declType == BasicType.Character) {
                return new CharacterBoxer(valueToAssign);
            }

            if (((BasicType) otherType.declType).clazz == String.class) {
                return new StringBoxer(valueToAssign);
            }

            RuntimeValue converted = TypeConverter.autoConvert(BasicType.StringBuilder, valueToAssign,
                    (BasicType) otherType.declType);
            if (converted != null) {
                return converted;
            }
        }
        return null;
    }

    @Override
    public boolean equals(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JavaClassBasedType) {
            Class other = ((JavaClassBasedType) obj).getStorageClass();
            return clazz == other || clazz == Object.class;
        }
        if (obj instanceof StringLimitType) {
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        return new StringIndex(array, index);
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue value) {
        //do not bring length to another variable
        return new StringBuilderCloner(value);
    }

    @Nullable
    @Override
    public Object initialize() {
        return new StringBuilder("");
    }

    @Nullable
    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @Nullable
    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }

    @Override
    public String getEntityType() {
        return "string limit";
    }

    public RuntimeValue getLength() {
        return length;
    }

    public void setLength(RuntimeValue length) {
        this.length = length;
    }
}
