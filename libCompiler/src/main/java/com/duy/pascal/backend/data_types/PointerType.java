package com.duy.pascal.backend.data_types;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.ObjectBasedPointer;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;

public class PointerType extends InfoType {

    public DeclaredType pointedToType;

    public PointerType(DeclaredType pointedToType) {
        this.pointedToType = pointedToType;
    }

    @Override
    public RuntimeValue convert(RuntimeValue runtimeValue, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = runtimeValue.getType(f);
        if (this.equals(other.declType)) {
            return runtimeValue;
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object initialize() {
        return new ObjectBasedPointer(null);
    }

    @Override
    public Class<?> getTransferClass() {
        return PascalReference.class;
    }

    @Override
    public boolean equals(DeclaredType obj) {
        if (obj instanceof PointerType) {
            return this.pointedToType.equals(((PointerType) obj).pointedToType);
        }
        return false;
    }

    // The pointer itself contains no mutable information.
    @Override
    public RuntimeValue cloneValue(final RuntimeValue r) {
        return r;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }

    @Override
    public String getEntityType() {
        return "pointer type";
    }

    @Override
    public String toString() {
        return "^" + pointedToType.toString();
    }

    public void setPointerToType(DeclaredType pointerToType) {
        this.pointedToType = pointerToType;
    }
}
