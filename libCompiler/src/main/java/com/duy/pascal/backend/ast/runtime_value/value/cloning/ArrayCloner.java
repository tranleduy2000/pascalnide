package com.duy.pascal.backend.ast.runtime_value.value.cloning;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.NullValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.types.RuntimeType;

import static com.duy.pascal.backend.utils.NullSafety.isNullValue;

public class ArrayCloner<T> implements RuntimeValue {
    private RuntimeValue r;

    public ArrayCloner(RuntimeValue r2) {
        this.r = r2;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return r.getType(f);
    }


    @NonNull
    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object arr = r.getValue(f, main);
        Object[] value = (Object[]) arr;
        return value.clone();
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
            throws ParsingException {
        Object[] value = (Object[]) r.compileTimeValue(context);
        if (isNullValue(value)) {
            return NullValue.get();
        }
        return value.clone();
    }

    @Override
    public String toString() {
        return r.toString();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new ArrayCloner(r.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}