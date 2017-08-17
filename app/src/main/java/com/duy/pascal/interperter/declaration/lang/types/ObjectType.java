package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

public abstract class ObjectType extends TypeInfo {

    public abstract Type getMemberType(String name) throws ParsingException;

    @NonNull
    public String getEntityType() {
        return "object type";
    }

}
