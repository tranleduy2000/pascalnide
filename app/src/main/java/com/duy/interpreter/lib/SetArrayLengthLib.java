package com.duy.interpreter.lib;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.lib.templated.TemplatedPascalPlugin;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArgumentType;
import com.duy.interpreter.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class SetArrayLengthLib implements TemplatedPascalPlugin {

    SetLengthLib a = new SetLengthLib();

    @Override
    public String name() {
        return a.name();
    }

    @Override
    public FunctionCall generateCall(LineInfo line, ReturnsValue[] values,
                                     ExpressionContext f) throws ParsingException {
        return a.generateCall(line, values, f);
    }

    @Override
    public FunctionCall generatePerfectFitCall(LineInfo line,
                                               ReturnsValue[] values, ExpressionContext f) throws ParsingException {
        return a.generatePerfectFitCall(line, values, f);
    }

    @Override
    public ArgumentType[] argumentTypes() {
        return a.argumentTypes();
    }

    @Override
    public DeclaredType return_type() {
        return a.return_type();
    }

}
