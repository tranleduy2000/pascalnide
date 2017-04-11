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

public class ArrayCloner<T> implements ReturnsValue {
    protected ReturnsValue[] outputFormat;
    private ReturnsValue returnsValue;

    public ArrayCloner(ReturnsValue r2) {
        this.returnsValue = r2;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
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
        Object[] value = (Object[]) returnsValue.getValue(f, main);
        return value.clone();
    }

    @Override
    public LineInfo getLine() {
        return returnsValue.getLine();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object[] value = (Object[]) returnsValue.compileTimeValue(context);
        return value.clone();
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(this);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new ArrayCloner(returnsValue.compileTimeExpressionFold(context));
    }
}