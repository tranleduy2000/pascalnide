package com.duy.pascal.backend.ast.runtime_value.value;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.ast.MethodDeclaration;
import com.duy.pascal.backend.ast.function_declaretion.builtin.IMethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.data_types.ArgumentType;
import com.duy.pascal.backend.data_types.RuntimeType;
import com.duy.pascal.backend.ast.AbstractCallableFunction;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.PluginCallException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.internal.PluginReflectionException;

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
