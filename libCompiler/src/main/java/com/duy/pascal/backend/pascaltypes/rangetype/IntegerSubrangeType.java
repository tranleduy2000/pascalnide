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

package com.duy.pascal.backend.pascaltypes.rangetype;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.index.NonIntegerIndexException;
import com.duy.pascal.backend.exceptions.index.SubRangeException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.value.NonConstantExpressionException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.ExpressionContext;

public class IntegerSubrangeType extends SubrangeType {
    public IntegerSubrangeType() {
        this.lower = 0;
        this.size = 0;
    }

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
            throw new SubRangeException(lower, (int) max, i.getLineNumber());
        }
        size = (((int) max) - lower) + 1;

    }

    /**
     * @param size if <code>size == -1</code>, the array will be ignore bound when compare
     */
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
