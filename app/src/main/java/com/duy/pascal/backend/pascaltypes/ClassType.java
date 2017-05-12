package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.ast.FunctionDeclaration;

import java.util.ArrayList;
import java.util.List;

public class ClassType extends CustomType {
    private List<FunctionDeclaration> memberfunctions = new ArrayList<>();

    public void addMemberDeclaration(FunctionDeclaration f) {
        memberfunctions.add(f);
    }

}
