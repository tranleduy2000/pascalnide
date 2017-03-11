package com.duy.interpreter.exceptions;


import com.duy.interpreter.linenumber.LineInfo;
import com.js.interpreter.ast.AbstractFunction;

public class AmbiguousFunctionCallException extends ParsingException {

    public AmbiguousFunctionCallException(LineInfo line, AbstractFunction possible, AbstractFunction alternative) {
        super(line, "Ambiguous function call could be interpreted as "
                + possible + " or as " + alternative);
    }

}
