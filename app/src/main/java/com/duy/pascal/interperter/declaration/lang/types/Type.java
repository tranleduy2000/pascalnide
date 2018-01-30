package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.Member;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public interface Type extends NamedEntity, Member {

    /**
     * init value for variable,
     * example init value for integer variable is 0, long is 0, string is ""
     */
    @NonNull
    Object initialize() throws RuntimePascalException;

    @NonNull
    Class getTransferClass();

    @NonNull
    Class<?> getStorageClass();

    /**
     * covert current type to another type
     * example
     * byte a = 6 -> integer b = a;
     */
    @Nullable
    RuntimeValue convert(RuntimeValue other, ExpressionContext context) throws Exception;

    boolean equals(Type otherType);


    @Nullable
    RuntimeValue cloneValue(RuntimeValue value);

    @Nullable
    RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed;

    @Override
    @NonNull
    LineNumber getLineNumber();

    void setLineNumber(@NonNull LineNumber lineNumber);

    @NonNull
    @Override
    String getEntityType();

    @Override
    @NonNull
    Name getName();

    void setName(@Nullable Name name);

    @Override
    @Nullable
    String getDescription();
}
