package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.ScopedRegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.SimpleRegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.cloning.CloneableObjectCloner;
import com.js.interpreter.runtime.variables.ContainsVariables;

import java.util.ArrayList;
import java.util.List;

import serp.bytecode.BCClass;
import serp.bytecode.BCField;
import serp.bytecode.BCMethod;
import serp.bytecode.Code;
import serp.bytecode.Instruction;
import serp.bytecode.JumpInstruction;

public class CustomType extends ObjectType {

    /**
     * This is a list of the defined variables in the custom type.
     */
    public List<VariableDeclaration> variableTypes = new ArrayList<>();

    private CustomVariable customVariable;

    public CustomType() {
    }

    /**
     * Adds another sub-variable to this user defined type.
     *
     * @param v The name and type of the variable to add.
     */
    public void addVariableDeclaration(VariableDeclaration v) {
        variableTypes.add(v);
    }

    @Override
    public Object initialize() {
        customVariable = new CustomVariable(variableTypes);
        return customVariable;
    }

    @Override
    public int hashCode() {
        return variableTypes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomType)) {
            return false;
        }
        CustomType other = (CustomType) obj;
        return variableTypes.equals(other.variableTypes);
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
        customVariable = new CustomVariable(variableTypes);
        return customVariable.getClass();
    }

    @Override
    public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other_type = value.getType(f);
        if (this.equals(other_type.declaredType)) {
            return cloneValue(value);
        }
        return null;
    }

    @Override
    public DeclaredType getMemberType(String name) {
        for (VariableDeclaration v : variableTypes) {
            if (v.name.equals(name)) {
                return v.type;
            }
        }
        System.err.println("Could not find member " + name);
        return null;
    }

    @Override
    public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
        constructor_code.anew().setType(this.getTransferClass());
        try {
            constructor_code.invokespecial().setMethod(this.getTransferClass().getConstructor());
        } catch (SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void cloneValueOnStack(TransformationInput t) {
        t.pushInputOnStack();
        try {
            t.getCode().invokeinterface().setMethod("clone", ContainsVariables.class, new Class[0]);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ReturnsValue generateArrayAccess(ReturnsValue array,
                                            ReturnsValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLine(), this);
    }

    @Override
    public ReturnsValue cloneValue(ReturnsValue r) {
        return new CloneableObjectCloner(r);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }

    @Override
    public void arrayStoreOperation(Code c) {
        c.aastore();
    }

    @Override
    public void convertStackToStorageType(Code c) {
        // do nothing.
    }

    @Override
    public void pushArrayOfType(Code code, RegisterAllocator ra,
                                List<IntegerSubrangeType> ranges) {
        //Because I cannot mix this method into DeclaredType (no multiple inheritance) I have to duplicate it.
        ArrayType.pushArrayOfNonArrayType(this, code, ra, ranges);

    }

    public static class ByteClassLoader extends ClassLoader {

        public Class<?> loadThisClass(byte[] bytes) {
            Class<?> c = defineClass(null, bytes, 0, bytes.length);
            resolveClass(c);
            return c;
        }
    }
}
