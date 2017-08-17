package com.duy.pascal.interperter.ast.runtime_value.value.boxing;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.NullValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class StringBuilderBoxer extends DebuggableReturnValue {
    private RuntimeValue value;

    public StringBuilderBoxer(RuntimeValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) throws Exception {
        return new RuntimeType(BasicType.create(StringBuilder.class), false);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
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
        if (other instanceof NullValue) {
            return other;
        }
        return new StringBuilder(other.toString());
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, value.getLineNumber());
        } else {
            return new StringBuilderBoxer(
                    value.compileTimeExpressionFold(context));
        }
    }
}
