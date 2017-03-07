package com.duy.interpreter.exceptions;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.DeclaredType;

public class NonArrayIndexed extends ParsingException {

    DeclaredType t;

    public NonArrayIndexed(LineInfo line, DeclaredType t) {
        super(line);
        this.t = t;
    }

    @Override
    public String getMessage() {
        return "Tried to do indexed access on something which wasn't an array or a string. It was a "
                + t.toString();
    }
}
