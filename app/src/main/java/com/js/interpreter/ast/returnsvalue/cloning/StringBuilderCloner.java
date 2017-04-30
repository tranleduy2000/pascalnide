package com.js.interpreter.ast.returnsvalue.cloning;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.LValue;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBuilderCloner implements RValue {
    protected RValue[] outputFormat;
    RValue r;

    public StringBuilderCloner(RValue r) {
        this.r = r;
        this.outputFormat = r.getOutputFormat();
    }

    @Override
    public RuntimeType get_type(ExpressionContext f)
            throws ParsingException {
        return r.get_type(f);
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
        Object result = r.getValue(f, main);
        return new StringBuilder(result.toString());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public LineInfo getLineNumber() {
        return r.getLineNumber();
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object val = r.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder((StringBuilder) val);
        }
        return null;
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderCloner(r);
    }

    @Override
    public LValue asLValue(ExpressionContext f) {
        return null;
    }

}