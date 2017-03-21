package com.duy.pascal.backend.tokens.grouping;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

import java.util.ArrayList;
import java.util.List;

public class ParenthesizedToken extends GrouperToken {
    /**
     *
     */
    private static final long serialVersionUID = 3945938644412769985L;

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

    public List<ReturnsValue> get_arguments_for_call(ExpressionContext context)
            throws ParsingException {
        List<ReturnsValue> result = new ArrayList<ReturnsValue>();
        while (hasNext()) {
            result.add(getNextExpression(context));
            if (hasNext()) {
                Token next = take();
                if (!(next instanceof CommaToken)) {
                    throw new ExpectedTokenException(",", next);
                }
            }
        }
        return result;
    }

    @Override
    protected String getClosingText() {
        return ")";
    }
}
