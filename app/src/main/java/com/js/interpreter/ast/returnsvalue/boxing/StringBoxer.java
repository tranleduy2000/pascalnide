package com.js.interpreter.ast.returnsvalue.boxing;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnassignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class StringBoxer extends DebuggableReturnsValue {

    final ReturnsValue s;

    public StringBoxer(ReturnsValue tobox) {
        this.s = tobox;
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
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
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
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnassignableTypeException {
        throw new UnassignableTypeException(this);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, s.getLineNumber());
        } else {
            return new StringBoxer(s.compileTimeExpressionFold(context));
        }
    }

}
