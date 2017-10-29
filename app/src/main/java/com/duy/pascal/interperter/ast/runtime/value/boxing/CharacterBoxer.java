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

public class CharacterBoxer extends DebuggableReturnValue {
    private RuntimeValue charValue;

    public CharacterBoxer(RuntimeValue charValue) {
        this.charValue = charValue;
    }


    @Override
    public String toString() {
        return "'" + charValue + "'";
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return charValue.getLineNumber();
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public RuntimeType getRuntimeType(ExpressionContext f) {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return new StringBuilder(charValue.getValue(f, main).toString());
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws Exception {
        Object val = charValue.compileTimeValue(context);
        if (val != null) {
            return new StringBuilder(val.toString());
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, charValue.getLineNumber());
        } else {
            return new CharacterBoxer(charValue.compileTimeExpressionFold(context));
        }
    }

}
