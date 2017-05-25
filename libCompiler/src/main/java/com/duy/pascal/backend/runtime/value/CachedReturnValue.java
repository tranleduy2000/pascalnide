package com.duy.pascal.backend.runtime.value;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class CachedReturnValue implements RuntimeValue {
    protected RuntimeValue[] outputFormat;
    private RuntimeValue other;
    private Object cache = null;

    public CachedReturnValue(RuntimeValue other) {
        this.other = other;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return other.getType(f);
    }

    @Override
    public LineInfo getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        cache = other.compileTimeValue(context);
        return cache;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new CachedReturnValue(other);
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return outputFormat;
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        this.outputFormat = formatInfo;
    }


    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        cache = other.getValue(f, main);
        return cache;
    }

}
