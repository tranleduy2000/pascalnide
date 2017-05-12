package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

public interface DeclaredType {

    Object initialize();

    Class getTransferClass();

    ReturnValue convert(ReturnValue returns_value,
                        ExpressionContext f) throws ParsingException;

    boolean equals(DeclaredType other);


    ReturnValue cloneValue(ReturnValue r);

    ReturnValue generateArrayAccess(ReturnValue array,
                                    ReturnValue index) throws NonArrayIndexed;

    Class<?> getStorageClass();
}
