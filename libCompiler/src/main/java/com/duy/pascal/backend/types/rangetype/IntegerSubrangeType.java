/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.types.rangetype;

import android.support.annotation.IntRange;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.LowerGreaterUpperBoundException;
import com.duy.pascal.backend.parse_exception.index.NonIntegerIndexException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.DLog;

public class IntegerSubrangeType extends SubrangeType {
    private static final String TAG = "IntegerSubrangeType";

   /* public IntegerSubrangeType() {
        DLog.d(TAG, "IntegerSubrangeType() called");
        this.lower = 0;
        this.size = 0;
    }*/

    public IntegerSubrangeType(GrouperToken i, ExpressionContext context)
            throws ParsingException {
        RuntimeValue firstValue = i.getNextExpression(context);
        this.lineInfo = firstValue.getLineNumber();

        RuntimeValue low = BasicType.Integer.convert(firstValue, context);
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

        RuntimeValue secondValue = i.getNextExpression(context);
        RuntimeValue high = BasicType.Integer.convert(secondValue, context);
        if (high == null) {
            throw new NonIntegerIndexException(secondValue);
        }
        Object max = high.compileTimeValue(context);
        if (max == null) {
            throw new NonConstantExpressionException(high);
        }
        if ((int) max < lower) {
            throw new LowerGreaterUpperBoundException(lower, (int) max, i.getLineNumber());
        }
        size = (((int) max) - lower) + 1;

    }

    public IntegerSubrangeType(int lower, @IntRange(from = 0) int size) {
        DLog.d(TAG, "IntegerSubrangeType() called with: lower = [" + lower + "], size = [" + size + "]");
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

        //ignore bound
        if (size == -1) return true;

        return lower == other.lower && size == other.size;
    }


    @Override
    public String toString() {
        return lower + ".." + (lower + size - 1);
    }

    @Override
    public boolean contain(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object obj) {
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
}
