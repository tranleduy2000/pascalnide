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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatMessageFromResource;


public class DuplicateIdentifierException extends ParsingException {
    @NonNull
    private final String currentType;
    @Nullable
    private final Name currentName;
    @NonNull
    private final String previousType;
    @NonNull
    private final String previousLine;

    public DuplicateIdentifierException(@NonNull NamedEntity previous, @NonNull NamedEntity current) {
        super(current.getLineNumber(),
                String.format("%s %s conflicts with previously defined %s with the same name defined at %s",
                        current.getEntityType(), current.getName(), previous.getEntityType(), previous.getLineNumber()));
        this.currentType = current.getEntityType();
        this.currentName = current.getName();
        this.previousType = previous.getEntityType();
        this.previousLine = String.valueOf(previous.getLineNumber());
    }

    @NonNull
    public String getCurrentType() {
        return this.currentType;
    }

    @Nullable
    public Name getCurrentName() {
        return this.currentName;
    }

    @NonNull
    public String getPreviousType() {
        return this.previousType;
    }

    @NonNull
    public String getPreviousLine() {
        return this.previousLine;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        return formatMessageFromResource(
                this,
                context,
                R.string.SameNameException,
                getCurrentType(),
                getCurrentName(),
                getPreviousType(),
                getPreviousLine());
    }
}
