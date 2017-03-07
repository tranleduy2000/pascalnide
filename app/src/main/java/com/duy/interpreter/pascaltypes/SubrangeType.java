package com.duy.interpreter.pascaltypes;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.NonConstantExpressionException;
import com.duy.interpreter.exceptions.NonIntegerIndexException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.basic.DotDotToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;

public class SubrangeType {

    public int lower;
    public int size;

    public SubrangeType() {
        this.lower = 0;
        this.size = 0;
    }

    public SubrangeType(GrouperToken i, ExpressionContext context)
            throws ParsingException {
        ReturnsValue l = i.getNextExpression(context);
        ReturnsValue low = BasicType.Integer.convert(l, context);
        if (low == null) {
            throw new NonIntegerIndexException(l);
        }
        Object min = low.compileTimeValue(context);
        if (min == null) {
            throw new NonConstantExpressionException(low);
        }
        lower = (Integer) min;

        Token t = i.take();
        if (!(t instanceof DotDotToken)) {
            throw new ExpectedTokenException("..", t);
        }
        ReturnsValue h = i.getNextExpression(context);
        ReturnsValue high = BasicType.Integer.convert(h, context);
        if (high == null) {
            throw new NonIntegerIndexException(h);
        }
        Object max = high.compileTimeValue(context);
        if (max == null) {
            throw new NonConstantExpressionException(high);
        }
        size = (((Integer) max) - lower) + 1;
    }
    public SubrangeType(int lower, int size) {
        this.lower = lower;
        this.size = size;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + lower;
        result = prime * result + size;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SubrangeType)) {
            return false;
        }
        SubrangeType other = (SubrangeType) obj;
        return lower == other.lower && size == other.size;
    }

    public boolean contains(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SubrangeType)) {
            return false;
        }
        SubrangeType other = (SubrangeType) obj;
        return lower <= other.lower
                && (lower + size) >= (other.lower + other.size);
    }

    @Override
    public String toString() {
        return lower + ".." + (lower + size - 1);
    }
}
