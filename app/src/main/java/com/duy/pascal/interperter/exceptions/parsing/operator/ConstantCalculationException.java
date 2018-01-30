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

package com.duy.pascal.interperter.exceptions.parsing.operator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;

public class ConstantCalculationException extends ParsingException {
    @NonNull
    private final RuntimePascalException target;

    public ConstantCalculationException(@NonNull RuntimePascalException e) {
        super(e.getLineNumber(), "Error while computing constant value: " + e.getMessage());
        this.target = e;
    }

    @NonNull
    public RuntimePascalException getException() {
        return this.target;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        ConstantCalculationException e = this;
        String line = formatLine(context, e.getLineNumber());
        String message = String.format(context.getString(R.string.ConstantCalculationException),
                e.getException().getLocalizedMessage());

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(line);
        builder.append("\n\n");
        builder.append(message);
        return builder;

    }
}
