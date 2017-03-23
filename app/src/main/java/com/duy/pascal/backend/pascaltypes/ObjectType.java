package com.duy.pascal.backend.pascaltypes;

public abstract class ObjectType implements DeclaredType {

    public abstract DeclaredType getMemberType(String name);

}
