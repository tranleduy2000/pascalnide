/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.view.code_view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * item for suggest adapter of {@link AutoSuggestsEditText}
 */
public class SuggestItem implements Serializable{
    private int type;
    @NonNull
    private String name = "";
    @Nullable
    private String description = null;
    @Nullable
    private CharSequence show;
    private String compare = "";

    public SuggestItem(int type, String name, String description, CharSequence show) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.show = show;
    }

    public SuggestItem(int type, String name, String description) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.compare = name.toLowerCase();
    }

    public SuggestItem(int type, String name) {
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

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CharSequence getShow() {
        return show;
    }

    public void setShow(CharSequence show) {
        this.show = show;
    }

    public String getCompare() {
        return compare;
    }

    public void setCompare(String compare) {
        this.compare = compare;
    }
}
