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

package com.js.interpreter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class ConstantDefinition implements NamedEntity {
    public DeclaredType type;
    private String name;
    private Object value;
    private LineInfo line;

    public ConstantDefinition(@NonNull String name, @NonNull Object value, LineInfo line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    /**
     * constructor use for system constant
     */
    public ConstantDefinition(@NonNull String name, @NonNull Object value) {
        this.name = name;
        this.value = value;
        this.line = new LineInfo(-1, name);//null lineInfo
    }

    public ConstantDefinition(@NonNull String name, @Nullable DeclaredType type, @Nullable Object init, LineInfo line) {
        this.name = name;
        this.type = type;
        this.value = init;
        this.line = line;
    }

    /**
     * constructor used for enum
     */
    public ConstantDefinition(@NonNull String name, @Nullable DeclaredType type, LineInfo line) {
        this.name = name;
        this.type = type;
        this.line = line;
    }

    public DeclaredType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public String getEntityType() {
        return "constant";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
