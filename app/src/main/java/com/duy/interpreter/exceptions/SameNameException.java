package com.duy.interpreter.exceptions;

import com.js.interpreter.ast.NamedEntity;
import com.duy.interpreter.exceptions.*;

public class SameNameException extends com.duy.interpreter.exceptions.ParsingException {
    public SameNameException(NamedEntity previous, NamedEntity n) {
        super(n.getLineNumber(), n.getEntityType() + " " + n.name()
                + " conflicts with previously defined "
                + previous.getEntityType() + " with the same name defined at "
                + previous.getLineNumber());
    }
}
