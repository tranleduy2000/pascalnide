package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.FunctionDeclaration;

public class OverridingFunctionException extends com.duy.pascal.backend.exceptions.ParsingException {
    public AbstractFunction functionDeclaration;
    public boolean isMethod = false;

    public OverridingFunctionException(FunctionDeclaration old, LineInfo line) {
        super(line, "Redefining function body for " + old.toString()
                + ", which was previous define at " + old.getLineNumber());
        this.functionDeclaration = old;
    }

    public OverridingFunctionException(AbstractFunction old,
                                       FunctionDeclaration news) {
        super(news.getLineNumber(), "Attempting to override plugin definition"
                + old.toString());
        this.functionDeclaration = old;
        isMethod = true;

    }
}
