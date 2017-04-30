package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArgumentType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.AbstractCallableFunction;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.PluginCallException;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.internal.PluginReflectionException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SimpleFunctionCall extends FunctionCall {
    private AbstractCallableFunction function;

    private LineInfo line;

    public SimpleFunctionCall(AbstractCallableFunction function,
                              ReturnsValue[] arguments, LineInfo line) {
        this.function = function;
        if (function == null) {
            System.err.println("Warning: Null function call");
        }
        this.arguments = arguments;
        this.line = line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        if (main != null) {
            if (main.isDebugMode()) {
                main.getDebugListener().onLine(getLine());
                System.out.println(getClass().getSimpleName() + " " + getLine());
            }
            main.incStack(getLine());
            main.scriptControlCheck(getLine());
        }

        //array store clone value
        Object[] values = new Object[arguments.length];
        //list type of list variable
        ArgumentType[] argumentTypes = function.getArgumentTypes();

        //convert to string object for print console or write to file
        if (getFunctionName().equals("writeln") || getFunctionName().equals("write")) {
            for (int i = 0; i < values.length; i++) {
                if (argumentTypes[i].getRuntimeClass().equals(File.class)) {
                    values[i] = arguments[i].getValue(f, main);
                } else {
                    ReturnsValue rawValue = arguments[i];
                    ReturnsValue[] outputFormat = rawValue.getOutputFormat();
                    StringBuilder object = new StringBuilder(String.valueOf(rawValue.getValue(f, main)));

                    if (outputFormat != null) {
                        if (outputFormat[1] != null) {
                            int sizeOfReal = (int) outputFormat[1].getValue(f, main);
                            StringBuilder round = new StringBuilder();
                            for (int j = 0; j < sizeOfReal; j++) round.append("#");
                            DecimalFormat decimalFormat = new DecimalFormat("#." + round.toString());
                            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.ENGLISH));
                            decimalFormat.setRoundingMode(RoundingMode.UNNECESSARY);
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
    public LineInfo getLine() {
        return line;
    }

    @Override
    protected String getFunctionName() {
        return function.name();
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(context), line);
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SimpleFunctionCall(function, compileTimeExpressionFoldArguments(c), line);
    }
}
