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
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class DuplicateIdentifierException extends ParsingException {
    @NonNull
    private String type;
    @Nullable
    private Name name;
    @NonNull
    private String preType;
    @NonNull
    private String preLine;

    public DuplicateIdentifierException(@NonNull NamedEntity previous, @NonNull NamedEntity current) {
        super(current.getLineNumber(), "" + current.getEntityType() + ' ' + current.getName() + " conflicts with previously defined " + "" + previous.getEntityType() + " with the same name defined at " + previous.getLineNumber());
        this.type = current.getEntityType();
        this.name = current.getName();
        this.preType = previous.getEntityType();
        this.preLine = String.valueOf(previous.getLineNumber());
    }

    @NonNull
    public final String getType() {
        return this.type;
    }

    public final void setType(@NonNull String var1) {
        this.type = var1;
    }

    @Nullable
    public final Name getName() {
        return this.name;
    }

    public final void setName(@Nullable Name var1) {
        this.name = var1;
    }

    @NonNull
    public final String getPreType() {
        return this.preType;
    }

    public final void setPreType(@NonNull String var1) {
        this.preType = var1;
    }

    @NonNull
    public final String getPreLine() {
        return this.preLine;
    }

    public final void setPreLine(@NonNull String var1) {
        this.preLine = var1;
    }
}
