package com.duy.pascal.backend.ast.runtime_value.value;

import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.debugable.DebuggableExecutableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.AmbiguousFunctionCallException;
import com.duy.pascal.backend.parse_exception.define.BadFunctionCallException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.utils.ArrayUtil;

import java.util.ArrayList;
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
                result = function.generatePerfectFitCall(name.getLineNumber(), arguments, expressionContext);
                if (result != null) {
                    if (perfectFit) {
                        throw new AmbiguousFunctionCallException(name.getLineNumber(), chosen, function);
                    }
                    perfectFit = true;
                    chosen = function;
                    runtimeValue = result;
                    break;
                }
                result = function.generateCall(name.getLineNumber(), arguments, expressionContext);
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
            ArrayList<String> argsType = new ArrayList<>();
            for (int i = 0; i < arguments.size(); i++) {
                argsType.add(String.valueOf(arguments.get(i).getType(expressionContext)));
            }
            ArrayList<String> listFunctions = new ArrayList<>();
            for (List<AbstractFunction> possibility : possibilities) {
                for (AbstractFunction function : possibility) {
                    listFunctions.add(function.toString());
                }
            }
            throw new BadFunctionCallException(name.getLineNumber(),
                    name.name, !possibilities.isEmpty(), matching, argsType, listFunctions, expressionContext);
        } else if (!perfectFit && ambiguous != null) {
            throw new AmbiguousFunctionCallException(name.getLineNumber(), chosen, ambiguous);
        } else {
            return runtimeValue;
        }
    }

    @Override
    public String toString() {
        return getFunctionName() + ArrayUtil.argToString(arguments);
    }

    protected abstract String getFunctionName();

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutableCodeUnit<?> main, String contextName)
            throws RuntimePascalException {
        Object valueImpl = getValueImpl(f, main);
        if (valueImpl == ExecutionResult.EXIT) {
            return ExecutionResult.EXIT;
        }
        return ExecutionResult.NOPE;
    }


    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {

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