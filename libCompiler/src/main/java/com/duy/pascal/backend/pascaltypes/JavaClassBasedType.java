package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.runtime_value.RuntimeValue;
import com.js.interpreter.ast.runtime_value.boxing.CharacterBoxer;
import com.js.interpreter.ast.runtime_value.boxing.StringBuilderBoxer;
import com.js.interpreter.ast.runtime_value.cloning.CloneableObjectCloner;

public class JavaClassBasedType implements DeclaredType {

    @Nullable
    private Class clazz;

    public JavaClassBasedType(@Nullable Class c) {
        this.clazz = c;
    }

    @Override
    public Object initialize() {
        try {
            if (clazz != null) {
                return clazz.newInstance();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public String toString() {
        if (clazz != null) {
            return clazz.getSimpleName();
        } else {
            return "null";
        }
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
        if (other instanceof JavaClassBasedType && ((JavaClassBasedType) other).getStorageClass() == clazz) {
            return true;
        } else if (clazz == Object.class || other == null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return new CloneableObjectCloner(r);
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }


}
