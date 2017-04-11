package com.duy.pascal.backend.pascaltypes.typeconversion;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class AnyToStringType implements ReturnsValue {
    ReturnsValue other;

    public AnyToStringType(ReturnsValue other) {
        this.other = other;
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        return other.getValue(f, main).toString();
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.anew(String.class), false);
    }

    @Override
    public LineInfo getLine() {
        return other.getLine();
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object o = other.compileTimeValue(context);
        if (o != null) {
            return o.toString();
        } else {
            return null;
        }
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        throw new UnAssignableTypeException(this);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new AnyToStringType(other.compileTimeExpressionFold(context));
    }
}
