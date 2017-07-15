package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.Member;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.index.NonArrayIndexed;

public interface Type extends NamedEntity, Member {

    /**
     * init value for variable,
     * example init value for integer variable is 0, long is 0, string is ""
     */
    @NonNull
    Object initialize();

    @Nullable
    Class getTransferClass();

    @Nullable
    Class<?> getStorageClass();

    /**
     * covert current type to another type
     * example
     * byte a = 6 -> integer b = a;
     */
    @Nullable
    RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws ParsingException;

    boolean equals(Type other);


    @Nullable
    RuntimeValue cloneValue(RuntimeValue r);

    @Nullable
    RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed;

    @Override
    @NonNull
    LineInfo getLineNumber();

    void setLineNumber(@NonNull LineInfo lineNumber);

    @NonNull
    @Override
    String getEntityType();

    @Override
    @NonNull
    String getName();

    void setName(@Nullable String name);

    @Override
    @Nullable
    String getDescription();
}
