package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.NamedEntity;
import com.duy.pascal.backend.exceptions.*;

public class SameNameException extends com.duy.pascal.backend.exceptions.ParsingException {
    public SameNameException(NamedEntity previous, NamedEntity n) {
        super(n.getLineNumber(), n.getEntityType() + " " + n.name()
                + " conflicts with previously defined "
                + previous.getEntityType() + " with the same name defined at "
                + previous.getLineNumber());
    }
}
