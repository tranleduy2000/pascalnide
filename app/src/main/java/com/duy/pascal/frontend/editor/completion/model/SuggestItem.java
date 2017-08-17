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

package com.duy.pascal.frontend.editor.completion.model;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.editor.view.CodeSuggestsEditText;
import com.duy.pascal.interperter.declaration.Name;

/**
 * item for suggest adapter of {@link CodeSuggestsEditText}
 */
public class SuggestItem implements Comparable<Name> {
    public static final int KIND_FUNCTION = 0;
    public static final int KIND_VARIABLE = KIND_FUNCTION + 1;
    public static final int KIND_CONST = KIND_VARIABLE + 1;
    public static final int KIND_TYPE = KIND_CONST + 1;
    public static final int KIND_PROCEDURE = KIND_TYPE + 1;

    @ItemKind
    private int type;
    @NonNull
    private Name name;
    @Nullable
    private String description = null;
    @Nullable
    private CharSequence show = null;

    public SuggestItem(int type, @NonNull Name name, @Nullable String description, @Nullable String show) {
        this(type, name, description);
        this.show = show;
    }

    public SuggestItem(int type, @NonNull Name name, @Nullable String description) {
        this(type, name);
        this.description = description;
    }

    public SuggestItem(int type, @NonNull Name name) {
        this.name = name;
        this.type = type;
    }

    public SuggestItem(int type, @NonNull String name) {
        this(type, Name.create(name));
    }

    @ItemKind
    public Integer getType() {
        return type;
    }

    public void setType(@ItemKind int type) {
        this.type = type;
    }

    @NonNull
    public Name getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public CharSequence snipet() {
        return show;
    }

    public void setShow(@Nullable CharSequence show) {
        this.show = show;
    }

    @Override
    public int compareTo(@NonNull Name o) {
        return name.compareTo(o);
    }

    @Override
    public String toString() {
        return name + " - " + description + " - " + show;
    }

    @IntDef({KIND_CONST, KIND_FUNCTION, KIND_PROCEDURE, KIND_TYPE, KIND_VARIABLE})
    public @interface ItemKind {
    }

}
