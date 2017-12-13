package com.duy.pascal.interperter.ast.runtime.value.boxing;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.debugable.DebuggableReturnValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class StringBoxer extends DebuggableReturnValue {

    private RuntimeValue value;

    public StringBoxer(RuntimeValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return value.getValue(f, main).toString();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object value = this.value.compileTimeValue(context);
        if (value != null) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, value.getLineNumber());
        } else {
            return new StringBoxer(value.compileTimeExpressionFold(context));
        }
    }

}
