package com.duy.pascal.backend.runtime.value;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public interface RuntimeValue {
    @Nullable
    RuntimeValue[] getOutputFormat();

    /**
     * used for output to console
     *
     * @param formatInfo formatInfo[0] : number column for display in the screen
     *                   formatInfo[1] :  length of floating point if this variable is double
     */
    void setOutputFormat(@Nullable RuntimeValue[] formatInfo);

    /**
     * @return value of variable or function
     */
    Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    /**
     * return type of variable or return type of function
     */
    RuntimeType getType(ExpressionContext f) throws ParsingException;

    /**
     * @return line of code
     */
    @Nullable
    LineInfo getLineNumber();

    /*
     * returns null if not a compile-time constant.
     */
    @Nullable
    Object compileTimeValue(CompileTimeContext context) throws ParsingException;

    @Nullable
    RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

    /**
     * reference of variable, set and indexOf value
     */
    @Nullable
    AssignableValue asAssignableValue(ExpressionContext f);
}
