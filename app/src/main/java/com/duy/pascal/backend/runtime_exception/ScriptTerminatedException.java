package com.duy.pascal.backend.runtime_exception;

import com.duy.pascal.backend.linenumber.LineInfo;

public class ScriptTerminatedException extends RuntimePascalException {

    public ScriptTerminatedException(LineInfo line) {
        super(line);
    }

    @Override
    public String getMessage() {
        return "Script was manually TERMINATED before it could finish executing";
    }
}
