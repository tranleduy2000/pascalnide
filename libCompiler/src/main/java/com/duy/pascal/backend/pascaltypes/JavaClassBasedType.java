package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.runtime_value.RuntimeValue;
import com.js.interpreter.ast.runtime_value.boxing.CharacterBoxer;
import com.js.interpreter.ast.runtime_value.boxing.StringBuilderBoxer;
import com.js.interpreter.ast.runtime_value.cloning.CloneableObjectCloner;

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
    public RuntimeValue convert(RuntimeValue value, ExpressionContext f)
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
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new CloneableObjectCloner(r);
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        return null;
    }

    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }


}
