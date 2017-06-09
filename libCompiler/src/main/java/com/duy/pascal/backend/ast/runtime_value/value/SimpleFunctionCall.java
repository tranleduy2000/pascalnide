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
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.MethodCallException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.internal.MethodReflectionException;
import com.duy.pascal.backend.utils.ArrayUtils;
import com.duy.pascal.frontend.debug.DebugManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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

        //convert to string object for print console or write to file
        if (getFunctionName().equals("writeln") || getFunctionName().equals("write")) {
            for (int i = 0; i < values.length; i++) {
                if (argumentTypes[i].getRuntimeClass().equals(File.class)) {
                    values[i] = arguments[i].getValue(f, main);
                } else {
                    RuntimeValue rawValue = arguments[i];
                    RuntimeValue[] outputFormat = rawValue.getOutputFormat();
                    StringBuilder object = new StringBuilder(String.valueOf(rawValue.getValue(f, main)));

                    if (outputFormat != null) {
                        if (outputFormat[1] != null) {
                            int sizeOfReal = (int) outputFormat[1].getValue(f, main);
                            StringBuilder round = new StringBuilder();
                            for (int j = 0; j < sizeOfReal; j++) round.append("0");
                            DecimalFormat decimalFormat = new DecimalFormat("#0." + round.toString());
                            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
                            Double d = Double.parseDouble(object.toString());
                            object = new StringBuilder(decimalFormat.format(d));
                        }

                        if (outputFormat[0] != null) {
                            int column = (int) outputFormat[0].getValue(f, main);
                            while (object.length() < column) {
                                object.insert(0, " ");
                            }
                        }
                    }
                    values[i] = object;
                }
            }
        } else {
            for (int i = 0; i < values.length; i++) {
                values[i] = arguments[i].getValue(f, main);
            }
        }
        if (main.isDebug()) {
            if (arguments.length > 0) {
                DebugManager.showMessage(arguments[0].getLineNumber(),
                        ArrayUtils.paramsToString(arguments, values), main);
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
