package com.duy.interpreter.exceptions;

import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.DeclaredType;

public class IncompatableFunctionDeclaration extends com.duy.interpreter.exceptions.ParsingException {

    public IncompatableFunctionDeclaration(LineInfo line,
                                           DeclaredType returntype, DeclaredType previousreturntype) {
        super(line, "Function declaration declares conflicting return type "
                + returntype + ".  It previously was defined as "
                + previousreturntype);
    }

}
