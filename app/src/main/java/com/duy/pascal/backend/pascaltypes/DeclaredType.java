package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

public interface DeclaredType {

    /**
     * init value for variable,
     * example init value for integer variable is 0, long is 0, string is ""
     */
    Object initialize();

    Class getTransferClass();

    /**
     * covert current type to another type
     * example
     * byte a = 6 -> integer b = a;
     */
    ReturnValue convert(ReturnValue returnValue,
                        ExpressionContext f) throws ParsingException;

    /**
     * check equal value
     */
    boolean equals(DeclaredType other);


    /**
     * clone value
     */
    @Nullable
    ReturnValue cloneValue(ReturnValue r);

    @NonNull
    ReturnValue generateArrayAccess(ReturnValue array,
                                    ReturnValue index) throws NonArrayIndexed;

    @Nullable
    Class<?> getStorageClass();
}
