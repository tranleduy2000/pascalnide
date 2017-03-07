package com.duy.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.*;

public class NotAStatementException extends com.duy.interpreter.exceptions.ParsingException {

    public NotAStatementException(ReturnsValue r) {
        super(r.getLineNumber(), r + " is not an instruction by itself.");
    }

}
