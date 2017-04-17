package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.pascaltypes.demo.CustomType;
import com.js.interpreter.ast.VariableDeclaration;

public class RecordType extends CustomType {
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\n");
        for (VariableDeclaration variableType : variableTypes) {
            out.append(variableType.toString()).append("\n");
        }
        return out.toString();
    }
}
