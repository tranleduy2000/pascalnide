package com.duy.pascal.backend.exceptions;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class IncompatableFunctionDeclaration extends com.duy.pascal.backend.exceptions.ParsingException {

    public IncompatableFunctionDeclaration(LineInfo line,
                                           DeclaredType returntype, DeclaredType previousreturntype) {
        super(line, "Function declaration declares conflicting return operator "
                + returntype + ".  It previously was defined as "
                + previousreturntype);
    }

}
