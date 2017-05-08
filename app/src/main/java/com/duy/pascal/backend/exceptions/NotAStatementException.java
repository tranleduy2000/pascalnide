package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class NotAStatementException extends com.duy.pascal.backend.exceptions.ParsingException {
    public ReturnValue returnValue;

    public NotAStatementException(ReturnValue r) {
        super(r.getLineNumber(), r + " is not an instruction by itself.");
        this.returnValue = r;
    }

}
