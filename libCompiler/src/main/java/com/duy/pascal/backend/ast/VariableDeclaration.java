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

package com.duy.pascal.backend.ast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.types.DeclaredType;

import java.util.Map;

public class VariableDeclaration implements NamedEntity, Cloneable {

    /**
     * name of variable, always first case
     */
    public String name;
    public DeclaredType type;
    private Object initialValue;
    @Nullable
    private LineInfo line;

    public VariableDeclaration(@NonNull String name, @NonNull DeclaredType type,
                               @Nullable Object initialValue, @Nullable LineInfo line) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.initialValue = initialValue;
    }

    public VariableDeclaration(@NonNull String name, @NonNull DeclaredType type,
                               @Nullable LineInfo line) {
        this.name = name;
        this.type = type;
        this.line = line;
    }

    public VariableDeclaration(@NonNull String name, @NonNull DeclaredType type) {
        this.name = name;
        this.type = type;
    }

    public VariableDeclaration(@NonNull String name, @NonNull DeclaredType type, String desc) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public DeclaredType getType() {
        return type;
    }

    @Nullable
    public Object getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(Object initialValue) {
        this.initialValue = initialValue;
    }

    public LineInfo getLineNumber() {
        return line;
    }


    public void initialize(Map<String, Object> map) {
        map.put(name, initialValue == null ? type.initialize() : initialValue);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + type.hashCode();
    }

    @Override
    public String getEntityType() {
        return "variable";
    }

    @Override
    public String toString() {
        return "var " + name + " = " + initialValue;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public VariableDeclaration clone() {
        return new VariableDeclaration(name, type, initialValue, line);
    }
}
