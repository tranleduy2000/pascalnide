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

package com.duy.pascal.interperter.exceptions.runtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;

import com.duy.pascal.interperter.exceptions.IRichFormatException;
import com.duy.pascal.interperter.linenumber.ISourcePosition;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class RuntimePascalException extends RuntimeException implements IRichFormatException, ISourcePosition {
    @Nullable
    public LineNumber line;

    /**
     * Uses for get localized message
     */
    @Nullable
    private Integer resourceId = null;
    @Nullable
    private Object[] args;
    private Exception cause;

    public RuntimePascalException(int resId, @Nullable Object... args) {
        this.resourceId = resId;
        this.args = args;
    }

    public RuntimePascalException() {
    }

    public RuntimePascalException(@Nullable LineNumber line) {
        this.line = line;
    }

    public RuntimePascalException(@Nullable LineNumber line, String message) {
        super(message);
        this.line = line;
    }

    public RuntimePascalException(String message) {
        super(message);
    }

    public RuntimePascalException(Exception e) {
        this.cause = e;
    }

    @Override
    public synchronized Throwable getCause() {
        if (cause != null) return cause;
        return super.getCause();
    }

    @Nullable
    public LineNumber getLineNumber() {
        return line;
    }

    @Override
    public Spanned getFormattedMessage(@NonNull Context context) {
        if (resourceId != null) {
            String string = context.getString(resourceId, args);
            return new SpannableString(string);
        }
        return new SpannableString(getMessage());
    }
}
