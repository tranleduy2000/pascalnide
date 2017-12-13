package com.duy.pascal.interperter.ast.runtime.value.cloning;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.ContainsVariables;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

import static com.duy.pascal.interperter.utils.NullSafety.zReturn;

public class CloneableObjectCloner implements RuntimeValue {
    private RuntimeValue r;

    public CloneableObjectCloner(RuntimeValue r) {
        this.r = r;
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        return r.getRuntimeType(context);
    }


    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @NonNull
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object value = r.getValue(context, main);
        if (value instanceof ContainsVariables) {
            ContainsVariables c = (ContainsVariables) value;
            return zReturn(c.clone());
        }
        return value;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return r.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        return r.compileTimeValue(context);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new CloneableObjectCloner(r.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }
}