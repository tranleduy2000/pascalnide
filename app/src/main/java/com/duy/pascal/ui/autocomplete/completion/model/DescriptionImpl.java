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

package com.duy.pascal.ui.autocomplete.completion.model;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.ui.autocomplete.completion.util.CodeTemplate;
import com.duy.pascal.ui.editor.view.CodeSuggestsEditText;

/**
 * item for suggest adapter of {@link CodeSuggestsEditText}
 */
public class DescriptionImpl implements Comparable<Name>, Description {
    public static final int KIND_VARIABLE = 1;
    public static final int KIND_CONST = KIND_VARIABLE + 1;
    public static final int KIND_FUNCTION = KIND_CONST + 1;
    public static final int KIND_TYPE = KIND_FUNCTION + 1;
    public static final int KIND_PROCEDURE = KIND_TYPE + 1;
    public static final int KIND_KEYWORD = KIND_PROCEDURE + 1;
    public static final int KIND_UNDEFINED = KIND_KEYWORD + 1;
    @NonNull
    protected Name name;
    @Nullable
    protected Type type;
    @ItemKind
    private int kind;
    @Nullable
    private String description = null;

    public DescriptionImpl(int kind, @NonNull Name name, @Nullable String description, Type type) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.description = description;
    }

    public DescriptionImpl(int kind, @NonNull String name) {
        this(kind, Name.create(name), null, null);
    }

    @Override
    public String getInsertText() {
        return name.getOriginName();
    }

    @Override
    public String getHeader() {
        return name.getOriginName().replace(CodeTemplate.CURSOR, "");
    }

    @ItemKind
    public Integer getKind() {
        return kind;
    }

    public void setKind(@ItemKind int kind) {
        this.kind = kind;
    }

    @Nullable
    @Override
    public Type getType() {
        return type;
    }

    @NonNull
    public String getName() {
        return name.getOriginName();
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }


    @Override
    public int compareTo(@NonNull Name o) {
        return name.compareTo(o);
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @IntDef({KIND_CONST, KIND_FUNCTION, KIND_PROCEDURE, KIND_TYPE, KIND_VARIABLE, KIND_KEYWORD, KIND_UNDEFINED})
    public @interface ItemKind {
    }

}
