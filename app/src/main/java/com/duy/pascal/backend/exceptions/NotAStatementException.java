package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.pascal.backend.exceptions.*;

public class NotAStatementException extends com.duy.pascal.backend.exceptions.ParsingException {

    public NotAStatementException(ReturnsValue r) {
        super(r.getLineNumber(), r + " is not an instruction by itself.");
    }

}
