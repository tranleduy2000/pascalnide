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

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.exceptions.parsing.index.LowerGreaterUpperBoundException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

/**
 * Created by Duy on 25-May-17.
 */
public class EnumSubrangeType extends SubrangeType<EnumElementValue> implements IntegerRange {
    private EnumGroupType enumGroupType;
    private Integer size;

    public EnumSubrangeType(EnumElementValue first, EnumElementValue last) throws LowerGreaterUpperBoundException {
        super(first, last);
        this.size = last.getIndex() - first.getIndex() + 1;
        this.enumGroupType = first.getEnumGroupType();
    }

    public EnumSubrangeType(EnumGroupType pascalType) throws LowerGreaterUpperBoundException {
        super(pascalType.getFirst(), pascalType.getLast());
        this.size = last.getIndex() - first.getIndex() + 1;
        this.enumGroupType = pascalType;
    }

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return EnumElementValue.class;
    }

    @Override
    public boolean contains(SubrangeType other) {
        if (other instanceof EnumSubrangeType) {
            return ((EnumSubrangeType) other).getEnumGroupType()
                    .equals(((EnumSubrangeType) other).getEnumGroupType());
        } else {
            return false;
        }
    }

    public EnumGroupType getEnumGroupType() {
        return enumGroupType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Type) {
            if (this.enumGroupType.equals((Type) obj)) return true;
        }
        return false;
    }

    @Override
    public boolean contain(VariableContext f, RuntimeExecutableCodeUnit<?> main,
                           EnumElementValue value) throws RuntimePascalException {
        return this.enumGroupType.contain(f, main, value);
    }

    public int indexOf(EnumElementValue key) {
        return this.enumGroupType.indexOf(key);
    }

    @Override
    public String toString() {
        return first + ".." + last;
    }

    @Override
    public int getFirst() {
        return first.getIndex();
    }

    @Override
    public int getSize() {
        return size;
    }

}
