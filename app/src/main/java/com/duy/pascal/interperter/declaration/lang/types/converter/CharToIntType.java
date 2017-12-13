package com.duy.pascal.interperter.declaration.lang.types.converter;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;

public class CharToIntType implements RuntimeValue {
    private RuntimeValue other;

    CharToIntType(RuntimeValue other) {
        this.other = other;
    }


    @Override
    public String toString() {
        return other.toString();
    }


    @NonNull
    @Override
    public Object getValue(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Character i = (Character) other.getValue(context, main);
        return (int) i;
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context)
            throws Exception {
        return new RuntimeType(BasicType.Integer, false);
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
        Object o = other.compileTimeValue(context);
        if (o != null) {
            return (int) (Character) o;
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        return new CharToIntType(other.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext context) {
        return null;
    }
}
