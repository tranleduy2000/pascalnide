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

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class ConstantDefinition implements NamedEntity {
    public Type type;
    private Name name;
    private Object value;
    private LineInfo line;

    public ConstantDefinition(@NonNull Name name, @NonNull Object value, LineInfo line) {
        this(name, null, value, line);
    }

    public ConstantDefinition(@NonNull String name, @NonNull Object value, LineInfo line) {
        this(Name.create(name), null, value, line);
    }

    /**
     * constructor use for system constant
     */
    public ConstantDefinition(@NonNull Name name, @NonNull Object value) {
        this(name, null, value, null);
    }

    /**
     * constructor use for system constant
     */
    public ConstantDefinition(@NonNull String name, @NonNull Object value) {
        this(Name.create(name), null, value, null);
    }

    public ConstantDefinition(@NonNull Name name, @Nullable Type type,
                              @NonNull Object init, LineInfo line) {
        this.name = name;
        this.type = type;
        this.value = init;
        this.line = line;
    }

    public ConstantDefinition(@NonNull String name, @Nullable Type type, @NonNull Object init, LineInfo line) {
        this(Name.create(name), type, init, line);
    }

    /*enum*/
    public ConstantDefinition(@NonNull Name name, @Nullable Type type, LineInfo line) {
        this(name, type, null, line);
    }

    public Type getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public LineInfo getLineNumber() {
        return line;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "constant";
    }

    @NonNull
    @Override
    public Name getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
