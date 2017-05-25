package com.duy.pascal.backend.runtime.value.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

public class StringBuilderBoxer extends DebuggableReturnValue {
    private RuntimeValue value;

    public StringBuilderBoxer(RuntimeValue value) {
        this.value = value;
        this.outputFormat = value.getOutputFormat();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(BasicType.create(String.class), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return value.getLineNumber();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object other = value.compileTimeValue(context);
        if (other != null) {
            return other.toString();
        }
        return null;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object other = value.getValue(f, main);
        return other.toString();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, value.getLineNumber());
        } else {
            return new StringBuilderBoxer(
                    value.compileTimeExpressionFold(context));
        }
    }
}
