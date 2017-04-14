package com.duy.pascal.backend.pascaltypes.rangetype;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.NonIntegerIndexException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.SubRangeException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

public class IntegerSubrangeType {
    public int lower;
    public int size;

    public IntegerSubrangeType() {
        this.lower = 0;
        this.size = 0;
    }

    public IntegerSubrangeType(GrouperToken i, ExpressionContext context)
            throws ParsingException {
        ReturnsValue firstValue = i.getNextExpression(context);
        ReturnsValue low = BasicType.Integer.convert(firstValue, context);
        if (low == null) {
            throw new NonIntegerIndexException(firstValue);
        }

        Object min = low.compileTimeValue(context);
        if (min == null) {
            throw new NonConstantExpressionException(low);
        }
        lower = (int) min;

        Token t = i.take();
        if (!(t instanceof DotDotToken)) {
            throw new ExpectedTokenException("..", t);
        }

        ReturnsValue secondValue = i.getNextExpression(context);
        ReturnsValue high = BasicType.Integer.convert(secondValue, context);
        if (high == null) {
            throw new NonIntegerIndexException(secondValue);
        }
        Object max = high.compileTimeValue(context);
        if (max == null) {
            throw new NonConstantExpressionException(high);
        }
        if ((int) max < lower) {
            throw new SubRangeException(lower, (int) max, i.lineInfo);
        }
        size = (((int) max) - lower) + 1;
    }

    public IntegerSubrangeType(int lower, int size) {
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
        if (!(obj instanceof IntegerSubrangeType)) {
            return false;
        }
        IntegerSubrangeType other = (IntegerSubrangeType) obj;
        return lower == other.lower && size == other.size;
    }

    public boolean contains(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof IntegerSubrangeType)) {
            return false;
        }
        IntegerSubrangeType other = (IntegerSubrangeType) obj;
        return lower <= other.lower
                && (lower + size) >= (other.lower + other.size);
    }

    @Override
    public String toString() {
        return lower + ".." + (lower + size - 1);
    }
}
