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

package com.duy.pascal.frontend.debug.utils;

import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.duy.pascal.backend.ast.runtime_value.variables.ContainsVariables;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.set.ArrayType;
import com.duy.pascal.frontend.theme.util.CodeTheme;

import java.util.List;

/**
 * Created by Duy on 09-Jun-17.
 */

public class SpanUtils {

    private CodeTheme codeTheme;

    public SpanUtils(CodeTheme codeTheme) {

        this.codeTheme = codeTheme;
    }

    public Spannable generateNameSpan(String name) {
        SpannableString text = new SpannableString(name);
        text.setSpan(new ForegroundColorSpan(codeTheme.getKeywordColor()), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }

    public Spannable generateTypeSpan(DeclaredType declaredType) {
        SpannableString spannableString;
        if (declaredType instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) declaredType;
            if (arrayType.getBounds() == null) {
                spannableString = new SpannableString("{" + arrayType.getElementType().toString() + "[]" + "}");
            } else {
                spannableString = new SpannableString("{" + arrayType.getElementType().toString()
                        + "[" + arrayType.getBounds().toString() + "]" + "}");
            }
        } else {
            spannableString = new SpannableString("{" + declaredType.toString() + "}");
        }
        spannableString.setSpan(new ForegroundColorSpan(codeTheme.getCommentColor()), 0,
                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public Spannable generateValueSpan(@Nullable Object value, @IntRange(from = 0) int maxSize) {
        if (value != null) {
            Spannable spannableString = null;
            if (value instanceof Number) { //number
                spannableString = new SpannableString(value.toString());
                spannableString.setSpan(new ForegroundColorSpan(codeTheme.getNumberColor()), 0,
                        spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (value instanceof String || value instanceof Character ||
                    value instanceof StringBuilder) { //string or char
                spannableString = new SpannableString("'" + value.toString() + "'");
                spannableString.setSpan(new ForegroundColorSpan(codeTheme.getStringColor()), 0,
                        spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (value instanceof Object[]) { //array
                spannableString = getSpanArray((Object[]) value, maxSize);
            } else if (value instanceof List) { //set, enum
                spannableString = new SpannableString(listToString((List) value, 10));
            } else if (value instanceof ContainsVariables) { //record
                spannableString = new SpannableString(value.toString());
            }
            return spannableString;
        }
        return new SpannableString("");
    }

    private SpannableString getSpanArray(Object[] value, int maxLength) {
        if (value == null || value.length == 0) {
            return new SpannableString("[]");
        }
        if (value[0] instanceof Object[]) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            if (value.length <= maxLength) {
                for (int i = 0; i < value.length; i++) {
                    stringBuilder.append(getSpanArray((Object[]) value[i], maxLength));
                    if (i == value.length - 1) {
                        return new SpannableString(stringBuilder.append("]"));
                    }
                    stringBuilder.append(", ").append("\n");
                }
            } else {
                for (int i = 0; i < maxLength; i++) {
                    stringBuilder.append(getSpanArray((Object[]) value[i], maxLength));
                    if (i == maxLength - 1) {
                        return new SpannableString(stringBuilder.append("...]"));
                    }
                    stringBuilder.append(", ").append("\n");
                }
                stringBuilder.append("...");
            }
            return new SpannableString(stringBuilder);
        } else {
            return new SpannableString(arrayToString(value, maxLength));
        }
    }

    public String listToString(List list, int maxSize) {
        if (list == null) return "";
        if (list.size() <= maxSize) {
            return list.toString();
        } else {
            StringBuilder b = new StringBuilder();
            b.append('[');
            for (int i = 0; i < maxSize; i++) {
                b.append(String.valueOf(list.get(i).toString()));
                if (i == maxSize - 1)
                    return b.append("...]").toString();
                b.append(", ");
            }
            return b.toString();
        }
    }

    public Spannable arrayToString(@Nullable Object[] array,
                                   @IntRange(from = 0) int maxSize) {
        if (array == null) return new SpannableString("[]");
        if (array.length <= maxSize) {
            SpannableStringBuilder b = new SpannableStringBuilder();
            b.append('[');
            for (int i = 0; i < array.length; i++) {
                b.append(generateValueSpan(array[i], maxSize));
                if (i == array.length - 1)
                    return b.append("]");
                b.append(", ");
            }
            return b;
        } else {
            SpannableStringBuilder b = new SpannableStringBuilder();
            b.append('[');
            for (int i = 0; i < maxSize; i++) {
                b.append(generateValueSpan(array[i], maxSize));
                if (i == maxSize - 1)
                    return b.append("...]");
                b.append(", ");
            }
            return b;
        }
    }
}
