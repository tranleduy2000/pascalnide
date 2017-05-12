package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.cloning.CloneableObjectCloner;

import java.util.ArrayList;
import java.util.List;

public class CustomType extends ObjectType {

    /**
     * This is a list of the defined variables in the custom operator.
     */
    public List<VariableDeclaration> variableDeclarations;

    private CustomVariable customVariable;

    public CustomType() {
        variableDeclarations = new ArrayList<>();
    }

    /**
     * Adds another sub-variable to this user defined operator.
     *
     * @param v The name and operator of the variable to add.
     */
    public void addVariableDeclaration(VariableDeclaration v) {
        variableDeclarations.add(v);
    }

    @Override
    public Object initialize() {
        customVariable = new CustomVariable(variableDeclarations);
        return customVariable;
    }

    @Override
    public int hashCode() {
        return variableDeclarations.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomType)) {
            return false;
        }
        CustomType other = (CustomType) obj;
        return variableDeclarations.equals(other.variableDeclarations);
    }

    @Override
    public boolean equals(DeclaredType obj) {
        return equals((Object) obj);
    }

    @Override
    public Class getTransferClass() {
        if (customVariable != null) {
            return customVariable.getClass();
        }
        customVariable = new CustomVariable(variableDeclarations);
        return customVariable.getClass();
    }


    @Override
    public ReturnValue convert(ReturnValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other_type = value.getType(f);
        if (this.equals(other_type.declType)) {
            return cloneValue(value);
        }
        return null;
    }

    @Override
    public DeclaredType getMemberType(String name) {
        for (VariableDeclaration v : variableDeclarations) {
            if (v.name.equals(name)) {
                return v.type;
            }
        }
        System.err.println("Could not find member " + name);
        return null;
    }



    @Override
    public ReturnValue generateArrayAccess(ReturnValue array,
                                           ReturnValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public ReturnValue cloneValue(ReturnValue r) {
        return new CloneableObjectCloner(r);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }

}
