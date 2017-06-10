package com.duy.pascal.backend.types;

import com.duy.pascal.backend.ast.FunctionDeclaration;

public class ClassType extends CustomType {

    private FunctionDeclaration constructor, destructor;

    @Override
    public String getEntityType() {
        return "class";
    }


}
