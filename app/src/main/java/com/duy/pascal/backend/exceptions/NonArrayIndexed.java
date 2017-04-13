package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class NonArrayIndexed extends ParsingException {

    public DeclaredType t;

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
