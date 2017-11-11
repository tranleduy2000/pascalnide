package com.duy.pascal.interperter.ast.runtime.value;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineInfo;
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
    public RuntimeType getRuntimeType(ExpressionContext exprContext) throws Exception {
        return other.getRuntimeType(exprContext);
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

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
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }

    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        cache = other.getValue(f, main);
        return cache;
    }

}
