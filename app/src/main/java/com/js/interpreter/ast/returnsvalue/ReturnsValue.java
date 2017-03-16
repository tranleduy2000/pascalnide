package com.js.interpreter.ast.returnsvalue;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnassignableTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface ReturnsValue {
    Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException;

    RuntimeType get_type(ExpressionContext f)
            throws ParsingException;

    LineInfo getLineNumber();

    /*
     * returns null if not a compile-time constant.
     */
    Object compileTimeValue(CompileTimeContext context)
            throws ParsingException;

    ReturnsValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException;

    /*
     * returns null if not a writable value.
     */
    SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnassignableTypeException;

}
