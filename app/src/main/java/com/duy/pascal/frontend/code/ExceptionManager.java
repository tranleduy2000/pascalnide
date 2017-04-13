package com.duy.pascal.frontend.code;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.duy.pascal.backend.exceptions.BadFunctionCallException;
import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.MainProgramNotFoundException;
import com.duy.pascal.backend.exceptions.MultipleDefinitionsMainException;
import com.duy.pascal.backend.exceptions.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.exceptions.grouping.EnumeratedGroupingException;
import com.duy.pascal.backend.lib.file.exceptions.DiskReadErrorException;
import com.duy.pascal.backend.lib.file.exceptions.FileException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotAssignException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.frontend.R;
import com.google.firebase.crash.FirebaseCrash;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 11-Mar-17.
 */

public class ExceptionManager {
    public static final String TAG = ExceptionManager.class
            .getSimpleName();
    private Context context;

    public ExceptionManager(Context context) {
        this.context = context;
    }
//
//    @SuppressWarnings("deprecation")
//    public static Spanned fromHtml(String html) {
//        Spanned result;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
//        } else {
//            result = Html.fromHtml(html);
//        }
//        return result;
//    }

    public Spanned getMessage(Throwable e) {
        try {
            if (e instanceof ExpectedTokenException) {
                return getExpectedTokenException((ExpectedTokenException) e);
            }
            if (e instanceof NoSuchFunctionOrVariableException) {
                return getNoSuchFunctionOrVariableException(e);
            }
            if (e instanceof BadFunctionCallException) {
                return getBadFunctionCallException(e);
            }
            if (e instanceof MultipleDefinitionsMainException) {
                return new SpannableString(context.getString(R.string.multi_define_main));
            }
            if (e instanceof EnumeratedGroupingException) {
                return getEnumeratedGroupingException((EnumeratedGroupingException) e);
            }
            if (e instanceof UnrecognizedTokenException) {
                return getUnrecognizedTokenException((UnrecognizedTokenException) e);
            }
            if (e instanceof MainProgramNotFoundException) {
                return new SpannableString(context.getString(R.string.main_program_not_define));
            }
            if (e instanceof BadOperationTypeException) {
                return getBadOperationTypeException((BadOperationTypeException) e);
            }
            if (e instanceof FileException) {
                return getFileException((FileException) e);
            }
            if (e instanceof PluginCallException) {
                return getPluginCallException(e);
            }
            if (e instanceof RuntimePascalException) {
                return new SpannableString(e.getMessage());
            }
            if (e instanceof ParsingException) {
                return new SpannableString(((ParsingException) e).line + "\n" + e.getMessage());
            }
            return new SpannableString(e.getMessage());
        } catch (Exception err) {
            FirebaseCrash.report(new Throwable("Error when get exception msg"));
            return new SpannableString(err.toString());
        }
    }

    private Spanned getPluginCallException(Throwable e) {

        PluginCallException exception = (PluginCallException) e;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        String format = String.format(
                context.getString(R.string.PluginCallException),
                exception.function.toString(),
                exception.cause.getClass().getSimpleName());
        stringBuilder.append(format);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        stringBuilder.append(getMessage(exception.cause));
        return stringBuilder;
    }

    private Spanned getFileException(FileException e) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(context.getString(R.string.file));
        stringBuilder.append(": ");
        stringBuilder.append(e.filePath);
        stringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0,
                stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        stringBuilder.append("\n");
        stringBuilder.append("\n");

        if (e instanceof DiskReadErrorException) {
            stringBuilder.append(context.getString(R.string.DiskReadErrorException));
        } else if (e instanceof FileNotAssignException) {
            stringBuilder.append(context.getString(R.string.FileNotAssignException));
        } else if (e instanceof com.duy.pascal.backend.lib.file.exceptions.FileNotFoundException) {
            stringBuilder.append(context.getString(R.string.FileNotFoundException));
        } else if (e instanceof FileNotOpenException) {
            stringBuilder.append(context.getString(R.string.FileNotOpenException));
        } else if (e instanceof FileNotOpenForInputException) {
            stringBuilder.append(context.getString(R.string.FileNotOpenForInputException));
        }
        return stringBuilder;
    }

    private Spannable getBadOperationTypeException(BadOperationTypeException e) {
        String source;
        if (e.value1 == null) {
            source = String.format(context.getString(R.string.BadOperationTypeException2),
                    e.operatorTypes);
        } else {
            source = String.format(context.getString(R.string.BadOperationTypeException),
                    e.operatorTypes, e.value1, e.value2, e.declaredType, e.declaredType1);
        }
        return new SpannableString(source);
    }

    private Spannable getUnrecognizedTokenException(UnrecognizedTokenException e) {
        String msg = context.getString(R.string.token_not_belong) + " ";
        Spannable span = new SpannableString(msg + e.token.toString());
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                msg.length(), msg.length() + e.token.toString().length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    private Spannable getEnumeratedGroupingException(EnumeratedGroupingException e) {
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

    private Spannable getBadFunctionCallException(Throwable e) {
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

    private Spannable getNoSuchFunctionOrVariableException(Throwable e) {
        String name = ((NoSuchFunctionOrVariableException) e).name;
        String msg = context.getString(R.string.not_define_msg);

        Spannable span = new SpannableString(name + msg);
        span.setSpan(new ForegroundColorSpan(Color.YELLOW),
                0, name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }

    private Spanned getExpectedTokenException(ExpectedTokenException e) {
        String msg1 = context.getString(R.string.expected_token) + " ";
        String msg2 = context.getString(R.string.expected_token_2) + " ";
        String expected = e.token + "\n";
        String current = e.instead + "\n";
//        String msg = msg1 + expected + msg2 + current;
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder
//                .append(msg1)
//                .append("<font color=\"yellow\">").append(expected).append("</font>").append("<br>")
//                .append(msg2)
//                .append("<font color=\"yellow\">").append(current).append("</font>").append("<br>")
//                .append("<font color=\"red\">").append(e.line).append("</font>");
//        return fromHtml(stringBuilder.toString());
        String msg = msg1 + expected + msg2 + current;
        Spannable span = new SpannableString(msg);
        span.setSpan(new ForegroundColorSpan(Color.YELLOW), msg1.length(), msg1.length() + expected.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span.setSpan(new StyleSpan(Typeface.BOLD), msg1.length(), msg1.length() + expected.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        span.setSpan(new ForegroundColorSpan(Color.YELLOW), msg1.length() + expected.length() + msg2.length(), msg.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(Typeface.BOLD), msg1.length() + expected.length() + msg2.length(), msg.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }
}
