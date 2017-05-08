package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ReturnValue {
    ReturnValue[] getOutputFormat();

    /**
     * used for output to console
     *
     * @param formatInfo formatInfo[0] : number column for display in the screen
     *                   formatInfo[1] :  length of floating point if this variable is double
     */
    void setOutputFormat(ReturnValue[] formatInfo);

    Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

    RuntimeType getType(ExpressionContext f)
            throws ParsingException;

    LineInfo getLineNumber();

    /*
     * returns null if not a compile-time constant.
     */
    Object compileTimeValue(CompileTimeContext context)
            throws ParsingException;

    ReturnValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

    /*
     * returns null if not a writable value.
     */
//    SetValueExecutable createSetValueInstruction(RValue r)
//            throws UnAssignableTypeException, ChangeValueConstantException;


    LeftValue asLValue(ExpressionContext f);
}
