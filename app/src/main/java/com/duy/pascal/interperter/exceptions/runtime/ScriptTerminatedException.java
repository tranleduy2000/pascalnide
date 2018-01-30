package com.duy.pascal.interperter.exceptions.runtime;

import com.duy.pascal.interperter.linenumber.LineNumber;

public class ScriptTerminatedException extends RuntimePascalException {

    public ScriptTerminatedException(LineNumber line) {
        super(line);
    }

    @Override
    public String getMessage() {
        return "Script was manually TERMINATED before it could finish executing";
    }
}
