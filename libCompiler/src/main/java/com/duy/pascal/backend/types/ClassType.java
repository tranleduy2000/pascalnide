package com.duy.pascal.backend.types;

import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ClassType extends CustomType {

    private FunctionDeclaration constructor, destructor;
    private List<FunctionDeclaration> privateFunctions = new ArrayList<>();
    private List<FunctionDeclaration> publicFunctions = new ArrayList<>();
    private List<FunctionDeclaration> publicVars = new ArrayList<>();
    private List<FunctionDeclaration> privateVars = new ArrayList<>();

    public void setConstructor(FunctionDeclaration constructor) {
        this.constructor = constructor;
    }

    public void setDestructor(FunctionDeclaration destructor) {
        this.destructor = destructor;
    }

    public void addPrivateFunction(FunctionDeclaration functionDeclaration) {

    }

    public void addPublicFunction(FunctionDeclaration functionDeclaration) {

    }

    @Override
    public String getEntityType() {
        return "class";
    }


    public void addPrivateField(ArrayList<VariableDeclaration> vars) {
    }

    public void addPublicFields(List<VariableDeclaration> vars) {
    }
}
