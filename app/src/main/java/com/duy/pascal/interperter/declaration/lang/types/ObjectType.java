package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;

public abstract class ObjectType extends TypeInfo {

    public abstract Type getMemberType(Name name) throws ParsingException;

    @NonNull
    public String getEntityType() {
        return "object type";
    }

}
