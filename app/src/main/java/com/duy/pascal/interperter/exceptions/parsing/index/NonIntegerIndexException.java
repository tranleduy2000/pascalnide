package com.duy.pascal.interperter.exceptions.parsing.index;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;
import static com.duy.pascal.ui.code.ExceptionManager.highlight;

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
public class NonIntegerIndexException extends ParsingException {
    @NonNull
    private RuntimeValue value;

    public NonIntegerIndexException(@NonNull RuntimeValue value) {
        super(value.getLineNumber());
        this.value = value;
    }

    @Nullable
    public String getMessage() {
        return "Array indexes must be integers: " + this.value;
    }

    @NonNull
    public final RuntimeValue getValue() {
        return this.value;
    }

    public final void setValue(@NonNull RuntimeValue var1) {
        this.value = var1;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        NonIntegerIndexException exception = this;
        String line = formatLine(context, exception.getLineNumber());
        String message = String.format(context.getString(R.string.NonIntegerIndexException), exception.getValue().toString());

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(line).append("\n\n");
        builder.append(message);
        return highlight(context, builder);
    }
}