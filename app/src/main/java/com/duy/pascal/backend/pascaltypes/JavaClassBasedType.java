package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.StringBuilderBoxer;
import com.js.interpreter.ast.returnsvalue.cloning.CloneableObjectCloner;

import java.util.List;

import serp.bytecode.Code;

public class JavaClassBasedType implements DeclaredType {

    Class c;

    public JavaClassBasedType(Class c) {
        this.c = c;
    }

    @Override
    public Object initialize() {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return c.getSimpleName();
    }

    @Override
    public Class getTransferClass() {
        return c;
    }

    @Override
    public ReturnValue convert(ReturnValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other_type = value.getType(f);
        if (other_type.declType instanceof BasicType) {
            if (this.equals(other_type.declType)) {
                return cloneValue(value);
            }
            if (this.c == String.class
                    && other_type.declType == BasicType.StringBuilder) {
                return new StringBuilderBoxer(value);
            }
            if (this.c == String.class
                    && other_type.declType == BasicType.Character) {
                return new StringBuilderBoxer(new CharacterBoxer(value));
            }

        }
        return null;
    }

    @Override
    public boolean equals(DeclaredType other) {
        return c == Object.class
                || (other instanceof JavaClassBasedType && ((JavaClassBasedType) other).c == c);
    }

    @Override
    public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
        constructor_code.anew().setType(StringBuilder.class);
        constructor_code.dup();
        try {
            constructor_code.invokespecial().setMethod(
                    StringBuilder.class.getConstructor());
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cloneValueOnStack(TransformationInput t) {
        t.pushInputOnStack();
        t.getCode().invokevirtual()
                .setMethod("clone", Object.class, new Class[]{});
    }

    @Override
    public ReturnValue cloneValue(ReturnValue r) {
        return new CloneableObjectCloner(r);
    }

    @Override
    public ReturnValue generateArrayAccess(ReturnValue array,
                                           ReturnValue index) throws NonArrayIndexed {
        return null;
    }

    @Override
    public Class<?> getStorageClass() {
        return c;
    }

    @Override
    public void arrayStoreOperation(Code c) {
        c.aastore();
    }

    @Override
    public void convertStackToStorageType(Code c) {

    }

    @Override
    public void pushArrayOfType(Code code, RegisterAllocator ra,
                                List<SubrangeType> ranges) {
        ArrayType.pushArrayOfNonArrayType(this, code, ra, ranges);
    }

}
