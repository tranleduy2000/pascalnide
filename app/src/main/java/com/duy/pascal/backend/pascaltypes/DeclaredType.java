package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.RValue;

import java.util.List;

import serp.bytecode.Code;

public interface DeclaredType {

    public abstract Object initialize();

    public abstract Class getTransferClass();


    public abstract RValue convert(RValue returns_value,
                                   ExpressionContext f) throws ParsingException;

    public abstract boolean equals(DeclaredType other);

    public abstract void pushDefaultValue(Code constructor_code,
                                          RegisterAllocator ra);

    public abstract void cloneValueOnStack(TransformationInput t);

    public abstract RValue cloneValue(RValue r);

    public abstract RValue generateArrayAccess(RValue array,
                                               RValue index) throws NonArrayIndexed;

    public abstract Class<?> getStorageClass();

    public abstract void arrayStoreOperation(Code c);

    public abstract void convertStackToStorageType(Code c);

    public abstract void pushArrayOfType(Code code, RegisterAllocator ra,
                                         List<SubrangeType> ranges);

}
