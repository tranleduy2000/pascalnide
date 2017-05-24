package com.duy.pascal.backend.pascaltypes;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonArrayIndexed;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;

public interface DeclaredType extends NamedEntity {

    /**
     * init value for variable,
     * example init value for integer variable is 0, long is 0, string is ""
     */
    @Nullable
    Object initialize();

    @Nullable
    Class getTransferClass();

    /**
     * covert current type to another type
     * example
     * byte a = 6 -> integer b = a;
     */
    @Nullable
    RuntimeValue convert(RuntimeValue runtimeValue,
                         ExpressionContext f) throws ParsingException;

    /**
     * check equal value
     */
    boolean equals(DeclaredType other);


    /**
     * clone value
     */
    @Nullable
    RuntimeValue cloneValue(RuntimeValue r);

    @Nullable
    RuntimeValue generateArrayAccess(RuntimeValue array,
                                     RuntimeValue index) throws NonArrayIndexed;

    @Nullable
    Class<?> getStorageClass();

    @Override
    LineInfo getLineNumber();

    void setLineNumber(LineInfo lineNumber);

    @Override
    String getEntityType();

    @Override
    String name();

    @Override
    String getDescription();

    void setName(String name);
}
