package com.duy.pascal.interperter.ast.runtime.value.cloning;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.NullValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class StringBuilderCloner implements RuntimeValue {
    private RuntimeValue value;

    public StringBuilderCloner(RuntimeValue value) {
        this.value = value;
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return value.getRuntimeType(context);
    }

    @Nullable
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object result = value.getValue(context, main);
        if (result instanceof NullValue) {
            return result;
        }
        return new StringBuilder(result.toString());
    }

    @Override
    public String toString() {
        return "string";
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object val = value.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder((StringBuilder) val);
        }
        return null;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new StringBuilderCloner(value);
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }

}