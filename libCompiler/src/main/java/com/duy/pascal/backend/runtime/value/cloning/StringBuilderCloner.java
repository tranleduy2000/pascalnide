package com.duy.pascal.backend.runtime.value.cloning;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.value.AssignableValue;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;

public class StringBuilderCloner implements RuntimeValue {
    private RuntimeValue[] outputFormat;
    private RuntimeValue value;

    public StringBuilderCloner(RuntimeValue value) {
        this.value = value;
        this.outputFormat = value.getOutputFormat();
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return value.getType(f);
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
        Object result = value.getValue(f, main);
        return new StringBuilder(result.toString());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object val = value.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder((StringBuilder) val);
        }
        return null;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new StringBuilderCloner(value);
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

}