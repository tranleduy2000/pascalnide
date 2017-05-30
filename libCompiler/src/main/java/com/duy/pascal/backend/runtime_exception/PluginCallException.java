package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.linenumber.LineInfo;

public class PluginCallException extends RuntimePascalException {
    public Throwable cause;
    public AbstractFunction function;

    public PluginCallException(LineInfo line, Throwable cause,
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
}
