package com.duy.interpreter.pascaltypes;

public abstract class ObjectType implements DeclaredType {

    public abstract DeclaredType getMemberType(String name);

}
