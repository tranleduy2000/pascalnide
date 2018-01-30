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

package com.duy.pascal.interperter.exceptions.parsing;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.ui.R;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;


public class UnrecognizedTokenException extends ParsingException {
    @NonNull
    private Token token;

    public UnrecognizedTokenException(@NonNull Token token) {
        super(token.getLineNumber(), "The following name doesn\'t belong here: " + token);
        this.token = token;
    }

    @NonNull
    public final Token getToken() {
        return this.token;
    }

    public final void setToken(@NonNull Token var1) {
        this.token = var1;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        UnrecognizedTokenException e = this;
        String message = context.getString(R.string.token_not_belong) + " " + e.getToken();

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(formatLine(context, e.getLineNumber())).append("\n\n");
        builder.append(message);

        ForegroundColorSpan span = new ForegroundColorSpan(Color.YELLOW);
        builder.setSpan(span, message.length(), message.length() + e.getToken().toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return builder;
    }
}
