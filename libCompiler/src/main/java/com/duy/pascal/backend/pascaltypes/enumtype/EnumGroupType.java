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

package com.duy.pascal.backend.pascaltypes.enumtype;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.InfoType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;

import java.util.LinkedList;

/**
 * Created by Duy on 25-May-17.
 */

public class EnumGroupType<T extends EnumElementValue> extends InfoType implements Containable {
    private LinkedList<EnumElementValue> list;

    public EnumGroupType(@NonNull LinkedList<EnumElementValue> list) {
        this.list = list;
    }

    public void add(EnumElementValue element) {
        list.add(element);
    }

    /**
     * @param position - index of value in list object
     */
    @Nullable
    public EnumElementValue get(int position) {
        if (position > list.size() - 1) {
            return null;
        }
        return list.get(position);
    }

    @Nullable
    public EnumElementValue get(String element) {
        for (EnumElementValue pair : list) {
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
        if (!(other.declType instanceof EnumGroupType)) {
            return null;
        }
        return cloneValue(runtimeValue);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(DeclaredType other) {
        if (this == other) {
            return true;
        }
        if (other instanceof EnumGroupType) {
            EnumGroupType otherEnum = (EnumGroupType) other;
            if (this.list.size() != otherEnum.list.size()) {
                return false;
            }
            if (this.list.equals(otherEnum.list)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return r;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index)
            throws NonArrayIndexed {
        throw new NonArrayIndexed(lineInfo, this);
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
        if (value instanceof EnumElementValue) {
            if (list.contains(value)) return true;
        }
        return false;
    }

    public int getSize() {
        return list.size();
    }

    public LinkedList<EnumElementValue> getList() {
        return list;
    }

    public int indexOf(EnumElementValue key) {
        return this.list.indexOf(key);
    }
}
