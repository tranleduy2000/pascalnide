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

package com.duy.pascal.ui.code;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;

import com.duy.pascal.interperter.exceptions.IRichFormatException;
import com.duy.pascal.interperter.linenumber.ISourcePosition;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.autocomplete.autofix.Patterns;

import java.util.regex.Matcher;

/**
 * Created by Duy on 11-Mar-17.
 */
public class ExceptionManager {
    public static final String TAG = ExceptionManager.class.getSimpleName();
    private Context mContext;

    public ExceptionManager(Context context) {
        this.mContext = context;
    }

    /**
     * Highlight text between " and "
     */
    public static Spannable highlight(Context context, Spannable span) {
        Matcher matcher = Patterns.REPLACE_HIGHLIGHT_PATTERN.matcher(span);
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        while (matcher.find()) {
            span.setSpan(new ForegroundColorSpan(color), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new StyleSpan(Typeface.BOLD), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    /**
     * Highlight text between " and "
     */
    public static Spannable highlight(Context context, String spannable) {
        return highlight(context, new SpannableString(spannable));
    }

    public static Spanned formatMessageFromResource(Throwable exception, Context context, int resourceID, Object... args) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String message = context.getString(resourceID, args);
        if (exception instanceof ISourcePosition) {
            String formattedLine = formatLine(context, ((ISourcePosition) exception).getLineNumber());
            builder.append(formattedLine);
            builder.append("\n\n");
            builder.append(message);
            return highlight(context, builder);
        } else {
            return new SpannableString(exception.getLocalizedMessage());
        }
    }

    @NonNull
    public static String formatLine(Context context, @Nullable LineInfo lineInfo) {
        if (lineInfo != null) {
            return context.getString(R.string.line_column, lineInfo.getLine(), lineInfo.getColumn());
        }
        return "";
    }

    public Spanned getMessage(@Nullable Throwable error) {
        try {
            if (error == null) {
                return new SpannableString("Unknown error");
            } else if (this instanceof IRichFormatException) {
                return ((IRichFormatException) error).getFormattedMessage(mContext);
            } else {
                return new SpannableString(error.getMessage());
            }
        } catch (Exception err) {
            err.printStackTrace();
            return new SpannableString(err.toString());
        }
    }

}
