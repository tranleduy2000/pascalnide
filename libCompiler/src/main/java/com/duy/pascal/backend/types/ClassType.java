package com.duy.pascal.backend.types;

import com.duy.pascal.backend.ast.FunctionDeclaration;

import java.util.ArrayList;

public class ClassType extends CustomType {

    private FunctionDeclaration constructor, destructor;
    private ArrayList<FunctionDeclaration> privateFunctions = new ArrayList<>();
    private ArrayList<FunctionDeclaration> publicFunctions = new ArrayList<>();

    public void addPrivateFunction(FunctionDeclaration functionDeclaration) {

    }

    public void addPublicFunction(FunctionDeclaration functionDeclaration) {

    }

    @Override
    public String getEntityType() {
        return "class";
    }


}
