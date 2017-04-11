package com.duy.pascal.backend.lib.templated;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;


public interface TemplatePascalPlugin {
    public String name();

    public FunctionCall generateCall(LineInfo line, ReturnsValue[] values,
                                     ExpressionContext f) throws ParsingException;

    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               ReturnsValue[] values, ExpressionContext f)
            throws ParsingException;

    public ArgumentType[] argumentTypes();

    public DeclaredType return_type();
}
