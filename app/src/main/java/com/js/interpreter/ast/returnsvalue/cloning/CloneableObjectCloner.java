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
import com.js.interpreter.runtime.variables.ContainsVariables;

public class CloneableObjectCloner implements ReturnsValue {
    protected ReturnsValue[] outputFormat;
    private ReturnsValue value;

    public CloneableObjectCloner(ReturnsValue value) {
        this.value = value;
        this.outputFormat = value.getOutputFormat();
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return value.getType(f);
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
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        ContainsVariables c = (ContainsVariables) value.getValue(f, main);
        return c.clone();
    }

    @Override
    public LineInfo getLine() {
        return value.getLine();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        ContainsVariables c = (ContainsVariables) value
                .compileTimeValue(context);
        return c.clone();
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(this);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new CloneableObjectCloner(value.compileTimeExpressionFold(context));
    }
}