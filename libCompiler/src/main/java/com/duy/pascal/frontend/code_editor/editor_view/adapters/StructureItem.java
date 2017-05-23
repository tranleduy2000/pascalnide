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

package com.duy.pascal.frontend.code_editor.editor_view.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.code_editor.editor_view.CodeSuggestsEditText;

/**
 * item for suggest adapter of {@link CodeSuggestsEditText}
 */
public class StructureItem implements  Comparable {
    private int type;
    @NonNull
    private String name = "";
    private String description = null;
    private CharSequence show;
    private String compare = "";

    public StructureItem(int type, @NonNull String name, String description, CharSequence show) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.show = show;
    }

    public StructureItem(int type, @NonNull String name, String description) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.compare = name.toLowerCase();
    }

    public StructureItem(int type, @NonNull String name) {
        this.name = name;
        this.type = type;
        this.compare = name.toLowerCase();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public CharSequence getShow() {
        return show;
    }

    public void setShow(@Nullable CharSequence show) {
        this.show = show;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        String s = o.toString().toLowerCase();
        return compare.startsWith(s) ? 0 : -1;
    }
}
