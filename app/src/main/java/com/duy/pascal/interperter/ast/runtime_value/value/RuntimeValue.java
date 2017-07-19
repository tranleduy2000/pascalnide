package com.duy.pascal.interperter.ast.runtime_value.value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;

public interface RuntimeValue {

    /**
     * @return value of variable or function, or constant
     */
    @NonNull
    Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    /**
     * @return type of variable, function, or constant
     */
    RuntimeType getRuntimeType(ExpressionContext f) throws ParsingException;

    /**
     * @return line of code
     */
    @NonNull
    LineInfo getLineNumber();

    void setLineNumber(LineInfo lineNumber);

    /*
     * return null if not a compile-time constant.
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
