package com.duy.pascal.interperter.tokens.grouping;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.OutputValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;

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
            builder.append(next.toCode()).append(',');
        }
        for (Token t : this.queue) {
            builder.append(t.toCode()).append(' ');
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
     * get format output, value:column:size
     * - value is value of variable
     * - column is number column on the screen use for show variable
     * - size is number of floating point if type of variable is double or float
     */
    public List<RuntimeValue> getArgumentsForOutput(ExpressionContext context)
            throws ParsingException {
        List<RuntimeValue> result = new ArrayList<>();
        while (hasNext()) {
            RuntimeValue value = getNextExpression(context);
            Class<?> runtimeClass;
            runtimeClass = value.getRuntimeType(context).declType.getStorageClass();
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
                    value = new OutputValue(value, infoOutput);
                } else {
                    next = take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException(",", next);
                    }
                }
            }
            if (!(value instanceof OutputValue) && RuntimeType.canOutputWithFormat(runtimeClass, 1)) {
                value = new OutputValue(value, null);
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
