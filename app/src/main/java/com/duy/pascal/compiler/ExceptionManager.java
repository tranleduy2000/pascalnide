package com.duy.pascal.compiler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.duy.interpreter.exceptions.BadFunctionCallException;
import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.MultipleDefinitionsMainException;
import com.duy.interpreter.exceptions.NoSuchFunctionOrVariableException;
import com.duy.interpreter.exceptions.UnrecognizedTokenException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;

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
            return processExpectedTokenException(e);
        } else if (e instanceof NoSuchFunctionOrVariableException) {
            return processNoSuchFunctionOrVariableException(e);
        } else if (e instanceof BadFunctionCallException) {
            return processBadFunctionCallException(e);
        } else if (e instanceof MultipleDefinitionsMainException) {
            return new SpannableString(context.getString(R.string.multi_define_main));
        } else if (e instanceof EnumeratedGroupingException) {
            return processEnumeratedGroupingException((EnumeratedGroupingException) e);
        } else if (e instanceof UnrecognizedTokenException) {
            return processUnrecognizedTokenException((UnrecognizedTokenException) e);
        } else {
            return new SpannableString(e.getMessage());
        }
    }

    private Spannable processUnrecognizedTokenException(UnrecognizedTokenException e) {
        String msg = context.getString(R.string.token_not_belong) + " ";
        Spannable span = new SpannableString(msg + e.token.toString());
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                msg.length(), msg.length() + e.token.toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    private Spannable processEnumeratedGroupingException(EnumeratedGroupingException e) {
        EnumeratedGroupingException.GroupingExceptionTypes exceptionTypes = e.exceptionTypes;
        if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.IO_EXCEPTION) {
            return new SpannableString(context.getString(R.string.IOException_reading_input));
        } else if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.EXTRA_END) {
            return new SpannableString(context.getString(R.string.extra_end_program));
        } else if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.INCOMPLETE_CHAR) {
            return new SpannableString(context.getString(R.string.Incomplete_character_literal));
        } else if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.MISMATCHED_BEGIN_END) {
            return new SpannableString(context.getString(R.string.Mismatched_begin_end));
        } else if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.MISMATCHED_BRACKETS) {
            return new SpannableString(context.getString(R.string.Mismatched_brackets));
        } else if (exceptionTypes == EnumeratedGroupingException.GroupingExceptionTypes.MISMATCHED_PARENS) {
            return new SpannableString(context.getString(R.string.Mismatched_parentheses));
        }
        return new SpannableString(e.getMessage());
    }

    private Spannable processBadFunctionCallException(Exception e) {
        String functionName = ((BadFunctionCallException) e).functionName + " ";
        boolean functionExists = ((BadFunctionCallException) e).functionExists;
        boolean numargsMatch = ((BadFunctionCallException) e).numargsMatch;
        if (functionExists) {
            if (numargsMatch) {
                String msg = context.getString(R.string.bad_function_msg1) + " ";
                Spannable span = new SpannableString(msg + functionName);
                span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        msg.length(), msg.length() + functionName.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return span;
            } else {
                String msg = context.getString(R.string.bad_function_msg2) + " ";
                Spannable span = new SpannableString(msg + functionName);
                span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                        msg.length(), msg.length() + functionName.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return span;
            }
        } else {
            String msg1 = context.getString(R.string.can_not_call_func) + " ";
            String msg2 = context.getString(R.string.func_not_define) + " ";
            Spannable span = new SpannableString(msg1 + functionName + msg2);
            span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    msg1.length(), msg1.length() + functionName.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
    }

    private Spannable processNoSuchFunctionOrVariableException(Exception e) {
        String name = ((NoSuchFunctionOrVariableException) e).name;
        String msg = context.getString(R.string.not_define_msg);

        Spannable span = new SpannableString(name + msg);
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                0, name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    private Spannable processExpectedTokenException(Exception e) {
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
    }
}
