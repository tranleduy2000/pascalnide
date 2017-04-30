package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.debugable.DebuggableExecutableRValue;
import com.duy.pascal.backend.exceptions.AmbiguousFunctionCallException;
import com.duy.pascal.backend.exceptions.BadFunctionCallException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FunctionCall extends DebuggableExecutableRValue {
    protected static final String TAG = FunctionCall.class.getSimpleName();
    protected RValue[] outputFormat;
    RValue[] arguments;

    public static RValue generateFunctionCall(WordToken name, List<RValue> arguments,
                                              ExpressionContext expressionContext)
            throws ParsingException {
        List<List<AbstractFunction>> possibilities = new ArrayList<>();
        expressionContext.getCallableFunctions(name.name.toLowerCase(), possibilities);

        boolean matching = false;
        boolean perfectFit = false;

        AbstractFunction chosen = null;
        AbstractFunction ambiguous = null;
        RValue result;
        RValue rValue = null;

        for (List<AbstractFunction> l : possibilities) {
            for (AbstractFunction function : l) {

                result = function.generatePerfectFitCall(name.lineInfo, arguments, expressionContext);
                if (result != null) {
                    if (perfectFit) {
                        throw new AmbiguousFunctionCallException(name.lineInfo, chosen, function);
                    }
                    perfectFit = true;
                    chosen = function;
                    rValue = result;
//                    continue;
                    break;
                }
                result = function.generateCall(name.lineInfo, arguments, expressionContext);
                if (result != null && !perfectFit) {
                    if (chosen != null) {
                        ambiguous = chosen;
                    }
                    chosen = function;
                    if (rValue == null)
                        rValue = result;
                }
                if (function.argumentTypes().length == arguments.size()) {
                    matching = true;
                }
            }
        }
        if (rValue == null) {
            throw new BadFunctionCallException(name.lineInfo, name.name,
                    !possibilities.isEmpty(), matching);
        } else if (!perfectFit && ambiguous != null) {
            throw new AmbiguousFunctionCallException(name.lineInfo, chosen, ambiguous);
        } else {
            return rValue;
        }
    }

    @Override
    public String toString() {
        return getFunctionName() + "(" + Arrays.toString(arguments) + ')';
    }

    protected abstract String getFunctionName();

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
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

    RValue[] compileTimeExpressionFoldArguments(CompileTimeContext context)
            throws ParsingException {
        RValue[] args = new RValue[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            args[i] = arguments[i].compileTimeExpressionFold(context);
        }
        return args;
    }
}