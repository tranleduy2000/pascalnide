package com.duy.pascal.backend.exceptions;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

public class InvalidAddressOperation extends ParsingException {

    public InvalidAddressOperation(LineInfo line, ReturnValue v) {
        super(line, "The expression " + v + " cannot have its address taken.");
    }

    public InvalidAddressOperation(LineInfo line) {
        super(line);
    }
}
