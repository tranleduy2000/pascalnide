package com.duy.pascal.compiler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.NoSuchFunctionOrVariableException;

/**
 * Created by Duy on 11-Mar-17.
 */

public class ExceptionManager {
    private Context context;

    public ExceptionManager(Context context) {
        this.context = context;
    }

    public Spannable getMessage(Exception e) {
        if (e instanceof ExpectedTokenException) {
            String msg1 = context.getString(R.string.expected_token) + " ";
            String msg2 = context.getString(R.string.expected_token_2) + " ";
            String expected = ((ExpectedTokenException) e).token + "\n";
            String current = ((ExpectedTokenException) e).instead + "\n";
            String msg = msg1 + expected + msg2 + current;

            Spannable span = new SpannableString(msg);
            span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    msg1.length(),
                    msg1.length() + expected.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new StyleSpan(Typeface.BOLD),
                    msg1.length(),
                    msg1.length() + expected.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    msg1.length() + expected.length() + msg2.length(), msg.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            span.setSpan(new StyleSpan(Typeface.BOLD),
                    msg1.length() + expected.length() + msg2.length(), msg.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        } else if (e instanceof NoSuchFunctionOrVariableException) {
            String name = ((NoSuchFunctionOrVariableException) e).name;
            String msg = context.getString(R.string.not_define_msg);

            Spannable span = new SpannableString(name + msg);
            span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    0, name.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            return span;
        } else {
            return new SpannableString(e.getMessage());
        }
//        return new SpannableString(e.getMessage());
    }
}
