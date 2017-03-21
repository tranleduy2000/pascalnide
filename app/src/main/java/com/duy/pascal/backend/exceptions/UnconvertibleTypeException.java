package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class UnconvertibleTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public UnconvertibleTypeException(ReturnsValue obj,
                                      DeclaredType out, DeclaredType in, boolean implicit) {
        super(obj.getLineNumber(), "The expression " + obj + " is of type " + in
                + ", which cannot be " + (implicit ? "implicitly " : "")
                + "converted to to the type " + in + ".");

    }
}
