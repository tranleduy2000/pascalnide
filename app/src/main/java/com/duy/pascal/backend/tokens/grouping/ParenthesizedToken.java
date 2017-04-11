package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

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

    public List<ReturnsValue> getArgumentsForCall(ExpressionContext context)
            throws ParsingException {
        List<ReturnsValue> result = new ArrayList<>();
        while (hasNext()) {
            ReturnsValue returnsValue = getNextExpression(context);
            result.add(returnsValue);
            if (hasNext()) {
                Token next = take();
                if (!(next instanceof CommaToken)) {
                    throw new ExpectedTokenException(",", next);
                }
            }
        }
        return result;
    }

    public List<ReturnsValue> getArgumentsForOutput(ExpressionContext context)
            throws ParsingException {
        List<ReturnsValue> result = new ArrayList<>();
        while (hasNext()) {
            ReturnsValue returnsValue = getNextExpression(context);
            RuntimeType runtimeType = returnsValue.getType(context);
            if (hasNext()) {
                Token next = take();
                if (next instanceof ColonToken) {
                    if (!runtimeType.canOutputWithFormat(1)) {
                        throw new ExpectedTokenException(",", next);
                    }
                    ReturnsValue[] infoOutput = new ReturnsValue[2];
                    ReturnsValue column = getNextExpression(context);
                    ReturnsValue lengthFloatingPoint = null;
                    if (hasNext()) {
                        next = take();
                        if (next instanceof ColonToken) {
                            if (runtimeType.canOutputWithFormat(2)) {
                                lengthFloatingPoint = getNextExpression(context);
                            } else {
                                throw new ExpectedTokenException(",", next);
                            }
                        } else if (!(next instanceof CommaToken)) {
                            throw new ExpectedTokenException(",", next);
                        }
                    }
                    infoOutput[0] = column;
                    infoOutput[1] = lengthFloatingPoint;
                    returnsValue.setOutputFormat(infoOutput);
                } else if (!(next instanceof CommaToken)) {
                    throw new ExpectedTokenException(",", next);
                }
            }
            result.add(returnsValue);
        }
        return result;
    }

    @Override
    protected String getClosingText() {
        return ")";
    }
}
