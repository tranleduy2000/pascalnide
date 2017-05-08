package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

import java.util.List;

import serp.bytecode.Code;

public interface DeclaredType {

    Object initialize();

    Class getTransferClass();


    ReturnValue convert(ReturnValue returns_value,
                        ExpressionContext f) throws ParsingException;

    boolean equals(DeclaredType other);

    void pushDefaultValue(Code constructor_code,
                          RegisterAllocator ra);

    void cloneValueOnStack(TransformationInput t);

    ReturnValue cloneValue(ReturnValue r);

    ReturnValue generateArrayAccess(ReturnValue array,
                                    ReturnValue index) throws NonArrayIndexed;

    Class<?> getStorageClass();

    void arrayStoreOperation(Code c);

    void convertStackToStorageType(Code c);

    void pushArrayOfType(Code code, RegisterAllocator ra,
                         List<SubrangeType> ranges);

}
