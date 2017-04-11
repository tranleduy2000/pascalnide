package com.duy.pascal.backend.exceptions;


import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;

public class OverridingFunctionException extends com.duy.pascal.backend.exceptions.ParsingException {

    public OverridingFunctionException(FunctionDeclaration old, LineInfo line) {
        super(line, "Redefining function body for " + old.toString()
                + ", which was previous define at " + old.getLine());
    }

    public OverridingFunctionException(AbstractFunction old,
                                       FunctionDeclaration news) {
        super(news.getLine(), "Attempting to override plugin definition"
                + old.toString());
    }
}
