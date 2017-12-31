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

package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.io.Serializable;
import java.lang.reflect.Modifier;

public abstract class TypeInfo implements Type, Serializable {
    /**
     * the name of type, variable, function, ...
     */
    protected Name name = null; //anonymous
    protected int modifier = Modifier.STATIC | Modifier.PUBLIC;
    protected LineInfo lineInfo;

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @Override
    public int getModifiers() {
        return modifier;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    @Override
    public void setLineNumber(@NonNull LineInfo lineNumber) {
        this.lineInfo = lineNumber;
    }

    @NonNull
    @Override
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }
}
