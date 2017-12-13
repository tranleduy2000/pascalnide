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

package com.duy.pascal.interperter.declaration.lang.types.string;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.StringIndex;
import com.duy.pascal.interperter.ast.runtime.value.boxing.CharacterBoxer;
import com.duy.pascal.interperter.ast.runtime.value.boxing.StringBoxer;
import com.duy.pascal.interperter.ast.runtime.value.boxing.StringBuilderLimitBoxer;
import com.duy.pascal.interperter.ast.runtime.value.cloning.StringBuilderCloner;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.TypeInfo;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;

/**
 * Created by Duy on 26-May-17.
 */

public class StringLimitType extends TypeInfo {

    private RuntimeValue length = null; //max size
    private Class clazz = StringBuilder.class;

    @Override
    public String toString() {
        return "String";
    }

    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext context)
            throws Exception {
        RuntimeType otherType = other.getRuntimeType(context);
        if (this.equals(otherType.declType)) {
            return new StringBuilderLimitBoxer(other, length);
        }

        if (otherType.declType instanceof BasicType) {
            if (otherType.declType.equals(BasicType.StringBuilder)) {
                return new StringBuilderLimitBoxer(other, length);
            }
            if (otherType.declType.equals(BasicType.Character)) {
                return new CharacterBoxer(other);
            }

            if (((BasicType) otherType.declType).clazz == String.class) {
                return new StringBoxer(other);
            }

            RuntimeValue converted = TypeConverter.autoConvert(BasicType.StringBuilder, other,
                    (BasicType) otherType.declType);
            if (converted != null) {
                return converted;
            }
        } else if (otherType.declType instanceof JavaClassBasedType &&
                otherType.declType.getStorageClass() == String.class) {
            return new StringBuilderLimitBoxer(other, length);
        }
        return null;
    }

    @Override
    public boolean equals(Type otherType) {
        if (this == otherType) {
            return true;
        }
        if (otherType instanceof JavaClassBasedType) {
            Class other = ((JavaClassBasedType) otherType).getStorageClass();
            return clazz == other || clazz == Object.class;
        }
        return otherType instanceof StringLimitType;
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

    @NonNull
    @Override
    public Object initialize() {
        return new StringBuilder("");
    }

    @NonNull
    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }

    @NonNull
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
