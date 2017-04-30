package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.returnsvalue.RValue;

public class BadOperationTypeException extends ParsingException {
    public DeclaredType declaredType, declaredType1;
    public RValue value1, value2;
    public OperatorTypes operatorTypes;

    public BadOperationTypeException() {
        super(new LineInfo(-1, "Unknown"));
    }

    public BadOperationTypeException(LineInfo line, DeclaredType t1,
                                     DeclaredType t2, RValue v1, RValue v2,
                                     OperatorTypes operation) {
        super(line, "Operator " + operation
                + " cannot be applied to arguments '" + v1 + "' and '" + v2
                + "'.  One has operator " + t1 + " and the other has operator " + t2
                + ".");
        this.value1 = v1;
        this.value2 = v2;
        this.operatorTypes = operation;
        declaredType = t1;
        declaredType1 = t2;
    }

    public BadOperationTypeException(LineInfo line, DeclaredType t1,
                                     RValue v1,
                                     OperatorTypes operation) {
        super(line, "Operator " + operation
                + " cannot be applied to argument '" + v1
                + "' of type " + t1
                + ".");
    }

    public BadOperationTypeException(LineInfo line, OperatorTypes operator) {
        super(line, "Operator " + operator + " is not a unary operator.");
    }
}
