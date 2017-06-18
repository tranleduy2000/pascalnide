package com.duy.pascal.backend.declaration.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.declaration.NamedEntity;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;

public interface DeclaredType extends NamedEntity {

    /**
     * init value for variable,
     * example init value for integer variable is 0, long is 0, string is ""
     */
    @NonNull
    Object initialize();

    @Nullable
    Class getTransferClass();

    /**
     * covert current type to another type
     * example
     * byte a = 6 -> integer b = a;
     */
    @Nullable
    RuntimeValue convert(RuntimeValue other,
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
    String getName();

    void setName(String name);

    @Override
    String getDescription();
}
