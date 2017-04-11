package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class UnconvertibleTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public UnconvertibleTypeException(ReturnsValue obj,
                                      DeclaredType out, DeclaredType in, boolean implicit) {
        super(obj.getLine(),
                "The expression or variable \"" + obj + "\" is of type \"" + out + "\""
                        + ", which cannot be " + (implicit ? "implicitly " : "")
                        + "converted to to the type \"" + in + "\"");

    }
}
