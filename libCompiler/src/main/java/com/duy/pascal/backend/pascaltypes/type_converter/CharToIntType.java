package com.duy.pascal.backend.pascaltypes.type_converter;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.AssignableValue;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class CharToIntType implements RuntimeValue {
    RuntimeValue other;

    public CharToIntType(RuntimeValue other) {
        this.other = other;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return new RuntimeValue[0];
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {

    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Character i = (Character) other.getValue(f, main);
        return (int) i;
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.Integer, false);
    }

    @Override
    public LineInfo getLineNumber() {
        return other.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object o = other.compileTimeValue(context);
        if (o != null) {
            return (int) (Character) o;
        } else {
            return null;
        }
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new CharToIntType(other.compileTimeExpressionFold(context));
    }

    @Override
    public AssignableValue asAssignableValue(ExpressionContext f) {
        return null;
    }
}
