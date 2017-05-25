package com.js.interpreter.runtime_value;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.function_declaretion.AbstractCallableFunction;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.PluginReflectionException;

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
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        if (main != null) {
            if (main.isDebugMode()) {
                main.getDebugListener().onLine(getLineNumber());
            }
            main.incStack(getLineNumber());
            main.scriptControlCheck(getLineNumber());
        }

        //array store clone value
        Object[] values = new Object[arguments.length];
        //list operator of list variable
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
                            for (int j = 0; j < sizeOfReal; j++) round.append("#");
                            DecimalFormat decimalFormat = new DecimalFormat("#." + round.toString());
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

        Object result;
        try {
            result = function.call(f, main, values);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new PluginReflectionException(line, e);
        } catch (InvocationTargetException e) {
            throw new PluginCallException(line, e.getTargetException(), function);
        }
        if (main != null)
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
    protected String getFunctionName() {
        return function.getName();
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(context), line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(c), line);
    }
}
