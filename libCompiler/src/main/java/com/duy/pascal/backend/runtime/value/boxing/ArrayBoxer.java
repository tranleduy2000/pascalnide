package com.duy.pascal.backend.runtime.value.boxing;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

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
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        throw new ParsingException(
                line,
                "Attempted to indexOf operator of varargs boxer. This should not happen as" +
                        " we are only supposed to pass varargs to plugins");
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
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context) throws ParsingException {
        RuntimeValue[] val = new RuntimeValue[values.length];
        for (int i = 0; i < values.length; i++) {
            val[i] = values[i].compileTimeExpressionFold(context);
        }
        return new ArrayBoxer(val, type, line);
    }
}
