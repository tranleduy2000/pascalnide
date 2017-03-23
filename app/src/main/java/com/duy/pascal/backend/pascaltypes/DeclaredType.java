package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;

import java.util.List;

import serp.bytecode.Code;

public interface DeclaredType {

    Object initialize();

    Class getTransferClass();

    ReturnsValue convert(ReturnsValue returns_value, ExpressionContext f) throws ParsingException;

    boolean equals(DeclaredType other);

    void pushDefaultValue(Code constructor_code, RegisterAllocator ra);

    void cloneValueOnStack(TransformationInput t);

    ReturnsValue cloneValue(ReturnsValue r);

    ReturnsValue generateArrayAccess(ReturnsValue array, ReturnsValue index) throws NonArrayIndexed;

    Class<?> getStorageClass();

    void arrayStoreOperation(Code c);

    void convertStackToStorageType(Code c);

    void pushArrayOfType(Code code, RegisterAllocator ra, List<SubrangeType> ranges);

}
