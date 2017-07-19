/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.interperter.declaration.lang.types.subrange;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.TypeInfo;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.declaration.lang.types.util.TypeUtils;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.parse_exception.index.LowerGreaterUpperBoundException;
import com.duy.pascal.interperter.parse_exception.index.NonArrayIndexed;
import com.duy.pascal.interperter.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.DotDotToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

/**
 * Created by Duy on 25-May-17.
 */

public abstract class SubrangeType<T extends Comparable> extends TypeInfo implements Containable<T> {

    protected T first;
    protected T last;

    public SubrangeType(T first, T last) {

        this.first = first;
        this.last = last;
    }

    public static SubrangeType getRangeType(GrouperToken g, ExpressionContext context,
                                            Token n) throws ParsingException {
        RuntimeValue first = g.getNextExpression(context, n);

        if (!(g.peek() instanceof DotDotToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        g.take(); //dot dot
        RuntimeValue last = g.getNextExpression(context, Token.Precedence.Relational);
        RuntimeType firstType = first.getRuntimeType(context);
        RuntimeValue convert = firstType.convert(last, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(last, firstType.declType,
                    last.getRuntimeType(context).declType, context);
        }
        Object v1 = first.compileTimeValue(context);
        if (v1 == null) {
            throw new NonConstantExpressionException(first);
        }
        Object v2 = last.compileTimeValue(context);
        if (v2 == null) {
            throw new NonConstantExpressionException(last);
        }
        if (TypeUtils.isIntegerType(firstType.getRawType().getStorageClass())) {
            Integer i1 = Integer.valueOf(v1.toString()); //first value
            Integer size = Integer.valueOf(v2.toString()); //last value
            if (i1 > size) {
                throw new LowerGreaterUpperBoundException(i1, size, first.getLineNumber());
            }
            size = size - i1 + 1; //size of range
            return new IntegerSubrangeType(i1, size);
        } else if (TypeUtils.isRealType(firstType.getRawType().getStorageClass())) {
            Double d1 = Double.valueOf(v1.toString());
            Double d2 = Double.valueOf(v2.toString());
            if (d1 > d2) {
                throw new LowerGreaterUpperBoundException(d1, d2, first.getLineNumber());
            }
            return new DoubleSubrangeType(d1, d2);
        } else if (firstType.getRawType().getStorageClass() == Boolean.class) {
            Boolean e1 = (Boolean) v1;
            Boolean e2 = (Boolean) v2;
            if (e1.compareTo(e2) > 0) {
                throw new LowerGreaterUpperBoundException(e1, e2, first.getLineNumber());
            }
            return new BooleanSubrangeType(e1, e2);
        } else if (firstType.getRawType() instanceof EnumGroupType) {
            EnumElementValue e1 = (EnumElementValue) v1;
            EnumElementValue e2 = (EnumElementValue) v2;
            if (e1.compareTo(e2) > 0) {
                throw new LowerGreaterUpperBoundException(e1, e2, first.getLineNumber());
            }
            return new EnumSubrangeType(e1, e2);
        }
        return null;
    }

    public abstract boolean contains(SubrangeType other);

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        return lineInfo;
    }

    public void setLineNumber(@NonNull LineInfo lineInfo) {
        this.lineInfo = lineInfo;
    }

    public abstract String toString();

    @NonNull
    @Override
    public Object initialize() {
        return first;
    }

    @Nullable
    @Override
    public Class getTransferClass() {
        return getStorageClass();
    }

    @Nullable
    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext f) throws ParsingException {
        RuntimeType other_type = other.getRuntimeType(f);
        if (this.equals(other_type.declType)) {
            return cloneValue(other);
        }
        return null;
    }

    @Override
    public boolean equals(Type obj) {
        if (obj instanceof SubrangeType) {
            SubrangeType other = (SubrangeType) obj;
            return this.first.equals(other.first) && last.equals(other.last);
        } else if (obj.getStorageClass() == this.getStorageClass()) {
            return true;
        }
        return false;

    }

    @Nullable
    @Override
    public RuntimeValue cloneValue(RuntimeValue r) {
        return r;
    }

    @Nullable
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array, RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(index.getLineNumber(), this);
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "subrange";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contain(@Nullable VariableContext f, @Nullable RuntimeExecutableCodeUnit<?> main,
                           T value) throws RuntimePascalException {
        return first.compareTo(value) <= 0 && last.compareTo(value) >= 0;
    }
}
