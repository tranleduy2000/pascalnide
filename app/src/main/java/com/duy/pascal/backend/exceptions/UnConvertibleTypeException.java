package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.returnsvalue.RValue;

public class UnConvertibleTypeException extends com.duy.pascal.backend.exceptions.ParsingException {

    public final RValue obj;
    public final DeclaredType out;
    public final DeclaredType in;
    public final boolean implicit;

    public UnConvertibleTypeException(RValue obj,
                                      DeclaredType out, DeclaredType in, boolean implicit) {
        super(obj.getLineNumber(),
                "The expression or variable \"" + obj + "\" is of operator \"" + out + "\""
                        + ", which cannot be " + (implicit ? "implicitly " : "")
                        + "converted to to the operator \"" + in + "\"");

        this.obj = obj;
        this.out = out;
        this.in = in;
        this.implicit = implicit;
    }
}
