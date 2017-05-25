package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.RuntimeValue;

import java.util.ArrayList;
import java.util.List;

public class ParenthesizedToken extends GrouperToken {

    public ParenthesizedToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
        return "(";
    }

    @Override
    public String toCode() {
        StringBuilder builder = new StringBuilder("(");
        if (next != null) {
            builder.append(next).append(',');
        }
        for (Token t : this.queue) {
            builder.append(t).append(' ');
        }
        builder.append(')');
        return builder.toString();
    }

    public List<RuntimeValue> getArgumentsForCall(ExpressionContext context)
            throws ParsingException {
        List<RuntimeValue> result = new ArrayList<>();
        while (hasNext()) {
            RuntimeValue runtimeValue = getNextExpression(context);
            result.add(runtimeValue);
            if (hasNext()) {
                Token next = take();
                if (!(next instanceof CommaToken)) {
                    throw new ExpectedTokenException(",", next);
                }
            }
        }
        return result;
    }

    /**
     * indexOf format output, value:column:size
     * - value is value of variable
     * - column is number column on the screen use for show variable
     * - size is number of floating point if operator of variable is double or float
     */
    public List<RuntimeValue> getArgumentsForOutput(ExpressionContext context)
            throws ParsingException {
        List<RuntimeValue> result = new ArrayList<>();
        while (hasNext()) {
            RuntimeValue value = getNextExpression(context);
            Class<?> runtimeClass;
            runtimeClass = value.getType(context).declType.getStorageClass();
            if (hasNext()) {
                Token next = peek();
                if (next instanceof ColonToken) {
                    next = take();
                    if (!RuntimeType.canOutputWithFormat(runtimeClass, 1)) {
                        throw new ExpectedTokenException(",", next);
                    }
                    RuntimeValue[] infoOutput = new RuntimeValue[2];
                    RuntimeValue column = getNextExpression(context);
                    RuntimeValue lengthFloatingPoint = null;
                    if (hasNext()) {
                        next = take();
                        if (next instanceof ColonToken) {
                            lengthFloatingPoint = getNextExpression(context);
                            if (hasNext()) {
                                next = take();
                                if (!(next instanceof CommaToken)) {
                                    throw new ExpectedTokenException(",", next);
                                }
                            }
                        } else if (!(next instanceof CommaToken)) {
                            throw new ExpectedTokenException(",", next);
                        }
                    }
                    infoOutput[0] = column;
                    infoOutput[1] = lengthFloatingPoint;
                    value.setOutputFormat(infoOutput);
                } else {
                    next = take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException(",", next);
                    }
                }
            }
            result.add(value);
        }
        return result;
    }

    @Override
    protected String getClosingText() {
        return ")";
    }
}
