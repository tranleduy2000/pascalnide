package com.duy.pascal.backend.ast.runtime_value.variables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import static com.duy.pascal.backend.utils.NullSafety.isNullValue;

import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public interface ContainsVariables extends Cloneable {
    @NonNull
    Object getVar(String name) throws RuntimePascalException;

    void setVar(String name, Object val);

    @Nullable
    ContainsVariables clone();
}
