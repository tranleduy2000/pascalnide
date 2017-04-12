package com.js.interpreter.ast.returnsvalue.cloning;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderCloner implements ReturnsValue {
    protected ReturnsValue[] outputFormat;
    ReturnsValue returnsValue;

    public StringBuilderCloner(ReturnsValue returnsValue) {
        this.returnsValue = returnsValue;
        this.outputFormat = returnsValue.getOutputFormat();
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return returnsValue.getType(f);
    }

    @Override
    public ReturnsValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(ReturnsValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }


    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object result = returnsValue.getValue(f, main);
        return new StringBuilder(result.toString());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public LineInfo getLine() {
        return returnsValue.getLine();
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(this);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object val = returnsValue.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder((StringBuilder) val);
        }
        return null;
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderCloner(returnsValue);
    }
}