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

package com.duy.pascal.interperter.exceptions.parsing.define;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.linenumber.LineNumber;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public  class MethodNotFoundException extends ParsingException {
    @NonNull
    private final Name name;
    @NonNull
    private final String className;

    public MethodNotFoundException(@NonNull LineNumber lineNumber, @NonNull Name name, @NonNull String className) {
        super(lineNumber);
        this.name = name;
        this.className = className;
    }

    @Nullable
    public String getMessage() {
        return "Can not find method \"" + this.name + "\" in class " + this.className;
    }

    @NonNull
    public final Name getName() {
        return this.name;
    }

    @NonNull
    public final String getClassName() {
        return this.className;
    }
}