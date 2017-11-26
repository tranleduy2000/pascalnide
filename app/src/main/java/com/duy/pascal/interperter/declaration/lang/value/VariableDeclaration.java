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

package com.duy.pascal.interperter.declaration.lang.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NameEntityImpl;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.util.Map;

public class VariableDeclaration extends NameEntityImpl implements Cloneable {
    @NonNull
    public Name name;
    @NonNull
    public Type type;
    private Object initialValue;
    @Nullable
    private LineInfo line;
    @Nullable
    private ExpressionContext context;

    public VariableDeclaration(@NonNull Name name, @NonNull Type type, @Nullable Object initialValue,
                               @Nullable LineInfo line) {
        this(name, type, initialValue, line, null);
    }

    public VariableDeclaration(@NonNull String name, @NonNull Type type, @Nullable Object initialValue,
                               @Nullable LineInfo line) {
        this(Name.create(name), type, initialValue, line, null);
    }

    public VariableDeclaration(@NonNull Name name, @NonNull Type type, @Nullable Object initialValue,
                               @Nullable LineInfo line, @Nullable ExpressionContext f) {
        this.name = name;
        this.type = type;
        this.line = line;
        this.initialValue = initialValue;
        this.context = f;
    }

    public VariableDeclaration(@NonNull Name name, @NonNull Type type, @Nullable LineInfo line) {
        this(name, type, null, line, null);
    }

    public VariableDeclaration(@NonNull Name name, @NonNull Type type) {
        this(name, type, null, null, null);
    }

    public VariableDeclaration(@NonNull String name, @NonNull Type type) {
        this(Name.create(name), type, null, null, null);
    }


    @NonNull
    public Name getName() {
        return name;
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    public Type getType() {
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


    public Object initialize(Map<Name, Object> map) throws RuntimePascalException {
        Object value = initialValue == null ? type.initialize() : initialValue;
        map.put(name, value);
        return value;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31 + type.hashCode();
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "variable";
    }

    @Override
    public String toString() {
        return name + ": " + type;
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
