package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.StringBuilderBoxer;
import com.js.interpreter.ast.returnsvalue.cloning.CloneableObjectCloner;

public class JavaClassBasedType implements DeclaredType {

    private Class clazz;

    public JavaClassBasedType(Class c) {
        this.clazz = c;
    }

    @Override
    public Object initialize() {
        try {
            return clazz.newInstance();
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String toString() {
        return clazz.getSimpleName();
    }

    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @Override
    public ReturnValue convert(ReturnValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType otherType = value.getType(f);
        if (otherType.declType instanceof BasicType) {
            if (this.equals(otherType.declType)) {
                return cloneValue(value);
            }
            if (this.clazz == String.class
                    && otherType.declType == BasicType.StringBuilder) {
                return new StringBuilderBoxer(value);
            }
            if (this.clazz == String.class
                    && otherType.declType == BasicType.Character) {
                return new StringBuilderBoxer(new CharacterBoxer(value));
            }
        }
        if (otherType.declType instanceof JavaClassBasedType) {
            JavaClassBasedType otherClassBasedType = (JavaClassBasedType) otherType.declType;
            if (this.equals(otherClassBasedType)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public boolean equals(DeclaredType other) {
        return clazz == Object.class
                || (other instanceof JavaClassBasedType
                && ((JavaClassBasedType) other).getStorageClass() == clazz);
    }


    @Override
    public ReturnValue cloneValue(ReturnValue r) {
        return new CloneableObjectCloner(r);
    }

    @NonNull
    @Override
    public ReturnValue generateArrayAccess(ReturnValue array,
                                           ReturnValue index) throws NonArrayIndexed {
        return null;
    }

    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }


}
