package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class BadOperationTypeException extends ParsingException {
    public BadOperationTypeException() {
        super(new LineInfo(-1, "Unknown"));
    }

    public BadOperationTypeException(LineInfo line, DeclaredType t1,
                                     DeclaredType t2, ReturnsValue v1, ReturnsValue v2,
                                     OperatorTypes operation) {
        super(line, "Operator " + operation
                + " cannot be applied to arguments '" + v1 + "' and '" + v2
                + "'.  One has type " + t1 + " and the other has type " + t2
                + ".");
    }

    public BadOperationTypeException(LineInfo line, OperatorTypes operator) {
        super(line, "Operator " + operator + " is not a unary operator.");
    }
}
