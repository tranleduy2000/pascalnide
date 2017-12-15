package com.duy.pascal.interperter.exceptions.runtime;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.ui.R;
import com.duy.pascal.ui.code.ExceptionManager;

import static com.duy.pascal.ui.code.ExceptionManager.formatLine;

public class MethodCallException extends RuntimePascalException {
    public Throwable cause;
    public AbstractFunction function;

    public MethodCallException(LineInfo line, Throwable cause,
                               AbstractFunction function) {
        super(line);
        this.cause = cause;
        this.function = function;
    }

    @Override
    public String getMessage() {
        return "When calling Function or Procedure " + function.getName()
                + ", The following java exception: \"" + cause + "\"\n" +
                "Message: " + (cause != null ? cause.getMessage() : "");
    }

    @Override
    public Spanned getLocalizedMessage(@NonNull Context context) {
        MethodCallException exception = this;
        String format = String.format(
                context.getString(R.string.PluginCallException),
                exception.function.toString(),
                exception.cause.getClass().getSimpleName());
        String line = formatLine(context, exception.getLineNumber());

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(line).append("\n\n");
        builder.append(format);
        builder.append("\n\n");
        builder.append(new ExceptionManager(context).getMessage(exception.cause));
        return builder;
    }
}
