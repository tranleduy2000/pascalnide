package com.duy.pascal.backend.ast.runtime_value.value.boxing;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayBoxer extends DebuggableReturnValue {
    public RuntimeValue[] values;
    public ArgumentType type;
    public LineInfo line;

    public ArrayBoxer(RuntimeValue[] array, ArgumentType elementType,
                      LineInfo line) {
        this.values = array;
        this.type = elementType;
        this.line = line;
    }

    public RuntimeValue[] getValues() {
        return values;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        throw new ParsingException(
                line,
                "Attempted to indexOf type of varargs boxer. This should not happen as" +
                        " we are only supposed to pass varargs to plugins");
    }

    @Override
    public boolean canDebug() {
        return false;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(),
                values.length);
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].getValue(f, main);
        }
        return result;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object[] result = (Object[]) Array.newInstance(type.getRuntimeClass(), values.length);
        for (int i = 0; i < values.length; i++) {
            Object val = values[i].compileTimeValue(context);
            if (val == null) {
                return null;
            } else {
                result[i] = val;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RuntimeValue[] val = new RuntimeValue[values.length];
        for (int i = 0; i < values.length; i++) {
            val[i] = values[i].compileTimeExpressionFold(context);
        }
        return new ArrayBoxer(val, type, line);
    }
}
