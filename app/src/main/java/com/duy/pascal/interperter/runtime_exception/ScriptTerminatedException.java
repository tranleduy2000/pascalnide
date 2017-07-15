package com.duy.pascal.interperter.runtime_exception;

import com.duy.pascal.interperter.linenumber.LineInfo;

public class ScriptTerminatedException extends RuntimePascalException {

    public ScriptTerminatedException(LineInfo line) {
        super(line);
    }

    @Override
    public String getMessage() {
        return "Script was manually TERMINATED before it could finish executing";
    }
}
