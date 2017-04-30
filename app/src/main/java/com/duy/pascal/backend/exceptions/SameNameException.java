package com.duy.pascal.backend.exceptions;

import com.js.interpreter.ast.NamedEntity;

public class SameNameException extends com.duy.pascal.backend.exceptions.ParsingException {
    public String type, name;
    public String preType, preLine;

    public SameNameException(NamedEntity previous, NamedEntity current) {
        super(current.getLineNumber(), current.getEntityType() + " " + current.name()
                + " conflicts with previously defined "
                + previous.getEntityType() + " with the same name defined at "
                + previous.getLineNumber());
        this.type = current.getEntityType();
        this.name = current.name();
        this.preType = previous.getEntityType();
        this.preLine = previous.getLineNumber().toString();
    }
}
