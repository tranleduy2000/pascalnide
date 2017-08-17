package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RecordValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.cloning.CloneableObjectCloner;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;

import java.util.ArrayList;

public class CustomType extends ObjectType {

    /**
     * This is a list of the defined variables in the custom type.
     */
    public ArrayList<VariableDeclaration> variableDeclarations;
    private RecordValue recordValue;

    public CustomType() {
        variableDeclarations = new ArrayList<>();
    }

    public CustomType(ArrayList<VariableDeclaration> vars) {
        this.variableDeclarations = vars;
    }


    /**
     * Adds another sub-variable to this user defined type.
     *
     * @param v The name and type of the variable to add.
     */
    public void addVariableDeclaration(VariableDeclaration v) {
        variableDeclarations.add(v);
    }

    @NonNull
    @Override
    public RecordValue initialize() {
        recordValue = new RecordValue(variableDeclarations);
        return recordValue;
    }

    @Override
    public int hashCode() {
        return variableDeclarations.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomType)) {
            if (obj instanceof JavaClassBasedType) {
                return ((JavaClassBasedType) obj).getStorageClass() == this.getStorageClass();
            }
            return false;
        }
        CustomType other = (CustomType) obj;
        return variableDeclarations.equals(other.variableDeclarations);
    }

    @Override
    public boolean equals(Type obj) {
        return equals((Object) obj);
    }

    @Override
    public Class getTransferClass() {
        if (recordValue != null) {
            return recordValue.getClass();
        }
        recordValue = new RecordValue(variableDeclarations);
        return recordValue.getClass();
    }


    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f)
            throws Exception {
        RuntimeType other_type = other.getRuntimeType(f);
        if (this.equals(other_type.declType)) {
            return cloneValue(other);
        }
        return null;
    }

    @Override
    public Type getMemberType(Name name) {
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

    @NonNull
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
