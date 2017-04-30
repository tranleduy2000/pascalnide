package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.RValue;

public class NotAStatementException extends com.duy.pascal.backend.exceptions.ParsingException {
    public RValue rValue;

    public NotAStatementException(RValue r) {
        super(r.getLineNumber(), r + " is not an instruction by itself.");
        this.rValue = r;
    }

}
