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

public class CharToIntType implements ReturnsValue {
    ReturnsValue other;

    public CharToIntType(ReturnsValue other) {
        this.other = other;
    }


    @Override
    public void setOutputFormat(ReturnsValue[] formatInfo) {

    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    @Override
    public ReturnsValue[] getOutputFormat() {
        return new ReturnsValue[0];
    }

    @Override
    public Object getValue(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Character i = (Character) other.getValue(f, main);
        return (int) i.charValue();
    }

    @Override
    public RuntimeType getType(ExpressionContext f)
            throws ParsingException {
        return new RuntimeType(BasicType.Integer, false);
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
            return (int) ((Character) o).charValue();
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
        return new CharToIntType(other.compileTimeExpressionFold(context));
    }
}
