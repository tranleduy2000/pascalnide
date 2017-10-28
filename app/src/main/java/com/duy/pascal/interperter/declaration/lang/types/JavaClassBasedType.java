package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.boxing.StringBoxer;
import com.duy.pascal.interperter.ast.runtime_value.value.cloning.CloneableObjectCloner;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class JavaClassBasedType extends TypeInfo {

    @NonNull
    private Class clazz;

    public JavaClassBasedType(@NonNull Class clazz) {
        this.clazz = clazz;
    }

    @NonNull
    @Override
    public Object initialize() {
        try {
            return clazz.newInstance();
        } catch (Exception ignored) {
        }
        return NullValue.get();
    }

    @Override
    public String toString() {
        if (clazz.equals(Void.class) || clazz.equals(void.class)) {
            return "";
        }
        String name = clazz.getSimpleName();
        name = name.replace(".", "_");
        return name;
    }

    @NonNull
    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws Exception {
        RuntimeType otherType = other.getRuntimeType(f);
        if (otherType.declType instanceof BasicType) {
            if (this.equals(otherType.declType)) {
                return cloneValue(other);
            }
            if (this.clazz == String.class && otherType.declType.equals(BasicType.StringBuilder)) {
                return new StringBoxer(other);
            }
            if (this.clazz == String.class && otherType.declType.equals(BasicType.Character)) {
                if (this.clazz == String.class) {
                    return new StringBoxer(other);
                }
            }
        }
        if (otherType.declType instanceof JavaClassBasedType) {
            JavaClassBasedType otherClassBasedType = (JavaClassBasedType) otherType.declType;
            if (this.equals(otherClassBasedType)) {
                return other;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Type other) {
        return (other.getStorageClass() == clazz) || (clazz == Object.class);
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

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return null;
    }

    @Override
    public void setLineNumber(@NonNull LineInfo lineNumber) {
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "Java class type";
    }

    @NonNull
    @Override
    public Name getName() {
        return clazz != null ? Name.create(clazz.getSimpleName()) : null;
    }

    @Override
    public String getDescription() {
        return null;
    }

}
