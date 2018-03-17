package com.duy.pascal.interperter.declaration.lang.types.converter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;

public class AnyToStringType implements RuntimeValue {
    private RuntimeValue value;

    public AnyToStringType(RuntimeValue value) {
        this.value = value;
    }

    @Nullable
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value = this.value.getValue(context, main);
        return value.toString();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Nullable
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context)
            throws Exception {
        return new RuntimeType(BasicType.create(String.class), false);
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
        Object o = value.compileTimeValue(context);
        if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new AnyToStringType(value.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }
}
