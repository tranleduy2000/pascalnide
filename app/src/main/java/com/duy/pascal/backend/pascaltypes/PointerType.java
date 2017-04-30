package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.ObjectBasedPointer;
import com.js.interpreter.runtime.PascalReference;

import java.util.List;

import serp.bytecode.Code;

public class PointerType implements DeclaredType {

    public DeclaredType pointedToType;

    public PointerType(DeclaredType pointedToType) {
        this.pointedToType = pointedToType;
    }

    @Override
    public RValue convert(RValue rValue, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = rValue.get_type(f);
        if (this.equals(other)) {
            return rValue;
        }
        return null;
    }

    @Override
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

    @Override
    public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
        pointedToType.pushDefaultValue(constructor_code, ra);
        try {
            constructor_code.invokespecial().setMethod(
                    ObjectBasedPointer.class.getConstructor(Object.class));
        } catch (SecurityException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    // The pointer itself contains no mutable information.
    @Override
    public RValue cloneValue(final RValue r) {
        return r;
    }

    @Override
    public void cloneValueOnStack(TransformationInput t) {
        t.pushInputOnStack();
        return;
    }

    @Override
    public RValue generateArrayAccess(RValue array,
                                      RValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
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
                                List<SubrangeType> ranges) {
        //Because I cannot mix this method into DeclaredType (no multiple inheritance) I have to duplicate it.
        ArrayType.pushArrayOfNonArrayType(this, code, ra, ranges);

    }

    @Override
    public String toString() {
        return "^" + pointedToType.toString();
    }
}
