package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class NotAStatementException extends com.duy.pascal.backend.exceptions.ParsingException {

    public NotAStatementException(ReturnsValue r) {
        super(r.getLine(), r + " is not an instruction by itself.");
    }

}
