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
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.cloning.ArrayCloner;

import java.util.LinkedList;

/**
 * Created by Duy on 25-May-17.
 */

public class EnumType<T extends DeclaredType> extends InfoType implements Containable {
    private LineInfo line;
    @NonNull
    private LinkedList<ConstantDefinition> list;
    private LineInfo lineNumber;


    public EnumType(@NonNull LinkedList<ConstantDefinition> list, LineInfo lineInfo) {
        this.list = list;
        this.line = lineInfo;
    }

    public void add(ConstantDefinition element) {
        list.add(element);
    }

    /**
     * @param position - index of value in list object
     */
    @Nullable
    public ConstantDefinition get(int position) {
        if (position > list.size() - 1) {
            return null;
        }
        return list.get(position);
    }

    @Nullable
    public ConstantDefinition get(String element) {
        for (ConstantDefinition pair : list) {
            if (pair.getName().equalsIgnoreCase(element)) {
                return pair;
            }
        }
        return null;
    }

    @Override
    public Object initialize() {
        return new LinkedList<>();
    }

    @Override
    public Class getTransferClass() {
        return LinkedList.class;
    }


    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f) throws ParsingException {
        RuntimeType other = runtimeValue.getType(f);
        if (!(other.declType instanceof EnumType)) {
            return null;
        }
        return cloneValue(runtimeValue);
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public int hashCode() {
        return (31 + list.size());
    }

    @Override
    public boolean equals(DeclaredType other) {
        if (this == other) {
            return true;
        }
        if (other instanceof EnumType) {
            EnumType enumType = (EnumType) other;
            if (this.list.size() != enumType.list.size()) {
                return false;
            }

            return true;
        }
        return false;
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new ArrayCloner<>(r);
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index)
            throws NonArrayIndexed {
        throw new NonArrayIndexed(line, this);
    }

    @Override
    public Class<?> getStorageClass() {
        return LinkedList.class;
    }

    @Override
    public String getEntityType() {
        return "enum type";
    }

    @Override
    public boolean contain(Object value) {
        return false;
    }
}
