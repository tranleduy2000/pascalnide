package com.duy.pascal.backend.ast.runtime_value.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;

public interface RuntimeValue {

    /**
     * @return value of variable or function
     */
    @NonNull
    Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    /**
     * return type of variable or return type of function
     */
    RuntimeType getType(ExpressionContext f) throws ParsingException;

    /**
     * @return lineInfo of code
     */
    @NonNull
    LineInfo getLineNumber();

    void setLineNumber(LineInfo lineNumber);

    /*
     * returns null if not a compile-time constant.
     */
    @Nullable
    Object compileTimeValue(CompileTimeContext context) throws ParsingException;

    @Nullable
    RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

    /**
     * reference of variable, set and get value
     */
    @Nullable
    AssignableValue asAssignableValue(ExpressionContext f);
}
