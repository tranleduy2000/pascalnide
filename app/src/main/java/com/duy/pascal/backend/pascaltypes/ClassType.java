package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.ast.FunctionDeclaration;

import java.util.ArrayList;
import java.util.List;

import serp.bytecode.BCClass;
import serp.bytecode.BCMethod;

public class ClassType extends CustomType {
    List<FunctionDeclaration> memberfunctions = new ArrayList<>();

    public void addMemberDeclaration(FunctionDeclaration f) {
        memberfunctions.add(f);
    }

    @Override
    protected void declareClassElements(BCClass c) {
        super.declareClassElements(c);
        for (FunctionDeclaration f : memberfunctions) {
            Class[] argtypes = new Class[f.argumentNames.length];
            for (int i = 0; i < f.argumentNames.length; i++) {
                argtypes[i] = f.argument_types[i].getRuntimeClass();
            }
            Class returntype = null;
            if (f.resultDefinition != null) {
                returntype = f.resultDefinition.type.getTransferClass();
            }
            BCMethod method = c.declareMethod(f.name, returntype, argtypes);
        }
    }
}
