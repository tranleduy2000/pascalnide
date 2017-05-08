package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class UnAssignableTypeException extends com.duy.pascal.backend.exceptions.ParsingException {
    public ReturnValue returnValue;

    public UnAssignableTypeException(ReturnValue value) {
        super(value.getLineNumber(), "The expression " + value
                + " cannot have a value assigned to it.");
        this.returnValue = value;
    }

}
