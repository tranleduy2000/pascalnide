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

package com.duy.pascal.backend.declaration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.linenumber.LineInfo;

public class LabelDeclaration implements NamedEntity, Cloneable {

    /**
     * name of variable, always first case
     */
    public String name;
    private LineInfo line;
    private Executable command;

    public LabelDeclaration(@NonNull String name, @Nullable LineInfo line) {
        this.name = name;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 31;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "label";
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public LabelDeclaration clone() {
        return new LabelDeclaration(name, line);
    }

    public Executable getCommand() {
        return command;
    }

    public void setCommand(Executable command) {
        this.command = command;
    }
}
