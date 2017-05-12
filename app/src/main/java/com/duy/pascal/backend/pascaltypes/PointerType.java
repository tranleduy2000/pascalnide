package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.ObjectBasedPointer;
import com.js.interpreter.runtime.PascalReference;

public class PointerType implements DeclaredType {

    public DeclaredType pointedToType;

    public PointerType(DeclaredType pointedToType) {
        this.pointedToType = pointedToType;
    }

    @Override
    public ReturnValue convert(ReturnValue returnValue, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = returnValue.getType(f);
        if (this.equals(other.declType)) {
            return returnValue;
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
    public ReturnValue cloneValue(final ReturnValue r) {
        return r;
    }

    @Override
    public ReturnValue generateArrayAccess(ReturnValue array,
                                           ReturnValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        return getTransferClass();
    }


    @Override
    public String toString() {
        return "^" + pointedToType.toString();
    }
}
