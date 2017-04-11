package com.duy.pascal.backend.lib.io;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

/**
 * Created by Duy on 08-Apr-17.
 */

public class CanNotReadVariableException extends RuntimePascalException {
    private Object object;

    public CanNotReadVariableException(LineInfo line) {
        super(line);
    }

    public CanNotReadVariableException(LineInfo line, String mes) {
        super(line, mes);
    }

    public CanNotReadVariableException() {
        super(new LineInfo(-1, "unknow"));
    }

    public CanNotReadVariableException(Object o) {
            this.object = o;
    }
}
