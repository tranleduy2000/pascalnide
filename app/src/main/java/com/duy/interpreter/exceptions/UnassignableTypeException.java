package com.duy.interpreter.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.*;

public class UnassignableTypeException extends com.duy.interpreter.exceptions.ParsingException {

    public UnassignableTypeException(ReturnsValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
    }

}
