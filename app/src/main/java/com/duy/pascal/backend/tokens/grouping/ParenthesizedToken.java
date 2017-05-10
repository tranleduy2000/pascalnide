package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;

import java.util.ArrayList;
import java.util.List;

public class ParenthesizedToken extends GrouperToken {

    public ParenthesizedToken(LineInfo line) {
        super(line);
    }

    @Override
    public String toString() {
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

    @Override
    public String toCode() {
        return "(";
    }

    public List<ReturnValue> getArgumentsForCall(ExpressionContext context)
            throws ParsingException {
        List<ReturnValue> result = new ArrayList<>();
        while (hasNext()) {
            ReturnValue returnValue = getNextExpression(context);
            result.add(returnValue);
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
     * - size is number of floating point if operator of variable is double or float
     */
    public List<ReturnValue> getArgumentsForOutput(ExpressionContext context)
            throws ParsingException {
        List<ReturnValue> result = new ArrayList<>();
        while (hasNext()) {
            ReturnValue value = getNextExpression(context);
            Class<?> runtimeClass;
            runtimeClass = value.getType(context).declType.getStorageClass();
            if (hasNext()) {
                Token next = peek();
                if (next instanceof ColonToken) {
                    next = take();
                    if (!RuntimeType.canOutputWithFormat(runtimeClass, 1)) {
                        throw new ExpectedTokenException(",", next);
                    }
                    ReturnValue[] infoOutput = new ReturnValue[2];
                    ReturnValue column = getNextExpression(context);
                    ReturnValue lengthFloatingPoint = null;
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
