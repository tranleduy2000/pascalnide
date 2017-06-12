package com.duy.pascal.backend.ast.runtime_value.value;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.AbstractCallableFunction;
import com.duy.pascal.backend.ast.MethodDeclaration;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.function_declaretion.builtin.IMethodDeclaration;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.types.ArgumentType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.MethodCallException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.internal.MethodReflectionException;
import com.duy.pascal.backend.utils.ArrayUtil;
import com.duy.pascal.frontend.debug.DebugManager;

import java.lang.reflect.InvocationTargetException;

public class SimpleFunctionCall extends FunctionCall {
    private AbstractCallableFunction function;

    private LineInfo line;

    public SimpleFunctionCall(AbstractCallableFunction function,
                              RuntimeValue[] arguments, LineInfo line) {
        this.function = function;
        if (function == null) {
            System.err.println("Warning: Null function call");
        }
        this.arguments = arguments;
        this.line = line;
    }

    @Override
    public Object getValueImpl(@NonNull VariableContext f, @NonNull RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (main.isDebug()) {
            main.getDebugListener().onLine((Executable) this, line);
        }
        main.incStack(line);
        //Do not enable debug in any case, because you will need to get value of list parameter,
        //In the case of empty parameters, pause once
        main.scriptControlCheck(line, false);

        //array store value of parameters
        Object[] values = new Object[arguments.length];
        //list type of parameters
        ArgumentType[] argumentTypes = function.argumentTypes();

        for (int i = 0; i < values.length; i++) {
            values[i] = arguments[i].getValue(f, main);
        }

        if (main.isDebug()) {
            if (arguments.length > 0) {
                DebugManager.showMessage(arguments[0].getLineNumber(),
                        ArrayUtil.paramsToString(arguments, values), main);
            }
            main.scriptControlCheck(line);
        }
        Object result;
        try {
            result = function.call(f, main, values);

            DebugManager.onFunctionCalled(function, arguments, result, main);//debug
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new MethodReflectionException(line, e);
        } catch (InvocationTargetException e) {
            throw new MethodCallException(line, e.getTargetException(), function);
        }

        main.decStack();
        return result;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) {
        return new RuntimeType(function.returnType(), false);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

    }

    @Override
    protected String getFunctionName() {
        return function.getName();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(context), line);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) throws ParsingException {
        Object[] args = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].compileTimeValue(context);
            if (args[i] == null) return null;
        }
        if (function instanceof MethodDeclaration || function instanceof IMethodDeclaration) {
            try {
                return function.call(null, null, args);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(c), line);
    }
}
