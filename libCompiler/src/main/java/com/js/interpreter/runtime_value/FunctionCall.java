package com.js.interpreter.runtime_value;

import com.duy.pascal.backend.debugable.DebuggableExecutableReturnValue;
import com.duy.pascal.backend.exceptions.define.AmbiguousFunctionCallException;
import com.duy.pascal.backend.exceptions.define.BadFunctionCallException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FunctionCall extends DebuggableExecutableReturnValue {
    protected static final String TAG = FunctionCall.class.getSimpleName();
    public RuntimeValue[] arguments;
    protected RuntimeValue[] outputFormat;

    public static RuntimeValue generateFunctionCall(WordToken name, List<RuntimeValue> arguments,
                                                    ExpressionContext expressionContext)
            throws ParsingException {
        List<List<AbstractFunction>> possibilities = new ArrayList<>();
        expressionContext.getCallableFunctions(name.name.toLowerCase(), possibilities);

        boolean matching = false;
        boolean perfectFit = false;

        AbstractFunction chosen = null;
        AbstractFunction ambiguous = null;
        RuntimeValue result;
        RuntimeValue runtimeValue = null;

        for (List<AbstractFunction> l : possibilities) {
            for (AbstractFunction function : l) {

                result = function.generatePerfectFitCall(name.getLineInfo(), arguments, expressionContext);
                if (result != null) {
                    if (perfectFit) {
                        throw new AmbiguousFunctionCallException(name.getLineInfo(), chosen, function);
                    }
                    perfectFit = true;
                    chosen = function;
                    runtimeValue = result;
//                    continue;
                    break;
                }
                result = function.generateCall(name.getLineInfo(), arguments, expressionContext);
                if (result != null && !perfectFit) {
                    if (chosen != null) {
                        ambiguous = chosen;
                    }
                    chosen = function;
                    if (runtimeValue == null)
                        runtimeValue = result;
                }
                if (function.argumentTypes().length == arguments.size()) {
                    matching = true;
                }
            }
        }
        if (runtimeValue == null) {
            throw new BadFunctionCallException(name.getLineInfo(), name.name,
                    !possibilities.isEmpty(), matching);
        } else if (!perfectFit && ambiguous != null) {
            throw new AmbiguousFunctionCallException(name.getLineInfo(), chosen, ambiguous);
        } else {
            return runtimeValue;
        }
    }

    @Override
    public String toString() {
        return getFunctionName() + "(" + Arrays.toString(arguments) + ')';
    }

    protected abstract String getFunctionName();

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object valueImpl = getValueImpl(f, main);
        if (valueImpl == ExecutionResult.EXIT) {
            return ExecutionResult.EXIT;
        }
        return ExecutionResult.NONE;
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    RuntimeValue[] compileTimeExpressionFoldArguments(CompileTimeContext context)
            throws ParsingException {
        RuntimeValue[] args = new RuntimeValue[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].compileTimeExpressionFold(context);
        }
        return args;
    }
}