package com.duy.interpreter.lib.templated;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArgumentType;
import com.duy.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;


public interface TemplatedPascalPlugin {
    public String name();

    public FunctionCall generateCall(LineInfo line, ReturnsValue[] values,
                                     ExpressionContext f) throws ParsingException;

    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               ReturnsValue[] values, ExpressionContext f) throws ParsingException;

    public ArgumentType[] argumentTypes();

    public DeclaredType return_type();
}
