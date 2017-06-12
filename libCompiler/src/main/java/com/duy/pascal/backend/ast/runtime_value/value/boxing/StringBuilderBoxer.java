package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.RuntimeType;

public class StringBuilderBoxer extends DebuggableReturnValue {
    private RuntimeValue value;

    public StringBuilderBoxer(RuntimeValue value) {
        this.value = value;
        this.outputFormat = value.getOutputFormat();
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.create(StringBuilder.class), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object other = value.compileTimeValue(context);
        if (other != null) {
            return new StringBuilder(other.toString());
        }
        return null;
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object other = value.getValue(f, main);
        return new StringBuilder(other.toString());
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, value.getLineNumber());
        } else {
            return new StringBuilderBoxer(
                    value.compileTimeExpressionFold(context));
        }
    }
}
