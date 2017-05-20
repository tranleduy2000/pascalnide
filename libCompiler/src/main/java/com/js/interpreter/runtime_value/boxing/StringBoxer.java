package com.js.interpreter.runtime_value.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.ConstantAccess;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBoxer extends DebuggableReturnValue {

    final RuntimeValue s;

    public StringBoxer(RuntimeValue tobox) {
        this.s = tobox;
        this.outputFormat = s.getOutputFormat();
    }

    @Override
    public LineInfo getLineNumber() {
        return s.getLineNumber();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        return new RuntimeType(BasicType.StringBuilder, false);
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return new StringBuilder(s.getValue(f, main).toString());
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object o = s.compileTimeValue(context);
        if (o != null) {
            return new StringBuilder(o.toString());
        } else {
            return null;
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, s.getLineNumber());
        } else {
            return new StringBoxer(s.compileTimeExpressionFold(context));
        }
    }

}
