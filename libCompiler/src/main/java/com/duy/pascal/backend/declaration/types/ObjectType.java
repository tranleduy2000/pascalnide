package com.duy.pascal.backend.declaration.types;

import com.duy.pascal.backend.parse_exception.ParsingException;

public abstract class ObjectType extends InfoType {

    public abstract DeclaredType getMemberType(String name) throws ParsingException;

    public String getEntityType() {
        return "object type";
    }

}
