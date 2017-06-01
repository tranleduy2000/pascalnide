package com.duy.pascal.backend.data_types;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.variables.CustomVariable;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.CloneableObjectCloner;

import java.util.ArrayList;

public class CustomType extends ObjectType {

    /**
     * This is a list of the defined variables in the custom type.
     */
    public ArrayList<VariableDeclaration> variableDeclarations;
    private CustomVariable customVariable;

    public CustomType() {
        variableDeclarations = new ArrayList<>();
    }


    /**
     * Adds another sub-variable to this user defined type.
     *
     * @param v The name and type of the variable to add.
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
    public RuntimeValue convert(RuntimeValue value, ExpressionContext f)
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
        return null;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new CloneableObjectCloner(r);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }

    @Override
    public String getEntityType() {
        return "custom type";
    }


    public ArrayList<VariableDeclaration> getVariableDeclarations() {
        return variableDeclarations;
    }

    public void setVariableDeclarations(ArrayList<VariableDeclaration> variableDeclarations) {
        this.variableDeclarations = variableDeclarations;
    }
}
