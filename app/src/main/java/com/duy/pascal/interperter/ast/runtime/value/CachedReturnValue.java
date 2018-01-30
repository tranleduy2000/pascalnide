package com.duy.pascal.interperter.ast.runtime.value;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

public class CachedReturnValue implements RuntimeValue {
    private RuntimeValue other;
    private Object cache = null;

    public CachedReturnValue(RuntimeValue other) {
        this.other = other;
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return other.getRuntimeType(context);
    }

    @NonNull
    @Override
    public LineNumber getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        cache = other.compileTimeValue(context);
        return cache;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new CachedReturnValue(other);
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }

    @NonNull
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        cache = other.getValue(context, main);
        return cache;
    }

}
