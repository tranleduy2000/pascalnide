package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.frontend.DLog;
import com.js.interpreter.ast.AbstractCallableFunction;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.PluginReflectionException;

import java.lang.reflect.InvocationTargetException;

public class SimpleFunctionCall extends FunctionCall {
    private AbstractCallableFunction function;

    private LineInfo line;

    public SimpleFunctionCall(AbstractCallableFunction function,
                              ReturnsValue[] arguments, LineInfo line) {
        this.function = function;
        if (function == null && DLog.DEBUG_PROGRAM) {
            System.err.println("Warning: Null function call");
        }
        this.arguments = arguments;
        this.line = line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object[] values = new Object[arguments.length];
        function.argumentTypes();
        for (int i = 0; i < values.length; i++) {
            values[i] = arguments[i].getValue(f, main);
        }
        Object result;
        try {
            result = function.call(f, main, values);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new PluginReflectionException(line, e);
        } catch (InvocationTargetException e) {
            throw new PluginCallException(line,e, function);
        }
        return result;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) {
        return new RuntimeType(function.return_type(), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    protected String getFunctionName() {
        return function.name();
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SimpleFunctionCall(function,
                compileTimeExpressionFoldArguments(context), line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SimpleFunctionCall(function,
                compileTimeExpressionFoldArguments(c), line);
    }
}
