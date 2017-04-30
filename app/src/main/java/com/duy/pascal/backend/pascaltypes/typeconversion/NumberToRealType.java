package com.duy.pascal.backend.pascaltypes.typeconversion;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class NumberToRealType implements RValue {
    private RValue[] outputFormat;
    private RValue other;


    public NumberToRealType(RValue other) {
        this.other = other;
        this.outputFormat = other.getOutputFormat();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public RValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Number i = (Number) other.getValue(f, main);
        return i.doubleValue();
    }

    @Override
    public RuntimeType get_type(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.Double, false);
    }

    @Override
    public LineInfo getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object o = other.compileTimeValue(context);
        if (o != null) {
            return ((Number) o).doubleValue();
        } else {
            return null;
        }
    }


    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new NumberToRealType(other.compileTimeExpressionFold(context));
    }

    @Override
    public LValue asLValue(ExpressionContext f) {
        return null;
    }
}
