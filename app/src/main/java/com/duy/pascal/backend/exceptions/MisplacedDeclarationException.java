package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;

public class MisplacedDeclarationException extends com.duy.pascal.backend.exceptions.ParsingException {

    public MisplacedDeclarationException(LineInfo line, String declarationType,
                                         ExpressionContext loc) {
        super(line, "Definition of " + declarationType
                + " is not appropriate here: " + loc);
    }

}
