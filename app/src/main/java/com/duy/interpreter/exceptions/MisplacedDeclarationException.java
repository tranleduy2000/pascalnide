package com.duy.interpreter.exceptions;


import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.duy.interpreter.exceptions.*;
import com.duy.interpreter.linenumber.LineInfo;

public class MisplacedDeclarationException extends com.duy.interpreter.exceptions.ParsingException {

    public MisplacedDeclarationException(LineInfo line, String declarationType,
                                         ExpressionContext loc) {
        super(line, "Definition of " + declarationType
                + " is not appropriate here: " + loc);
    }

}
