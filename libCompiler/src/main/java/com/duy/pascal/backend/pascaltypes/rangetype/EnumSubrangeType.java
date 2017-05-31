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

package com.duy.pascal.backend.pascaltypes.rangetype;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.TypeIdentifierExpectException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.set.EnumElementValue;
import com.duy.pascal.backend.pascaltypes.set.EnumGroupType;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;

/**
 * Created by Duy on 25-May-17.
 */

public class EnumSubrangeType extends SubrangeType {
    private EnumGroupType<EnumElementValue> enumGroupType;

    public EnumSubrangeType(GrouperToken bound, ExpressionContext context)
            throws ParsingException {
        if (bound.hasNext()) {
            if (bound.peek() instanceof WordToken) {
                WordToken name = (WordToken) bound.take();
                DeclaredType type = context.getTypedefType(name.name);
                if (type == null) {
                    throw new TypeIdentifierExpectException(name.getLineNumber(), name.name, context);
                }

                if (type instanceof EnumGroupType) {
                    EnumGroupType enumGroupType = (EnumGroupType) type;
                    this.enumGroupType = enumGroupType;
                    this.lower = 0;
                    this.size = enumGroupType.getSize();
                } else {
                }
            } else {
                throw new ExpectedTokenException("[enum identifier]", bound.peek());
            }
        } else {
            throw new ExpectedTokenException("[enum identifier]", bound.peek());
        }

        if (bound.hasNext()) {
            throw new ExpectedTokenException("]", bound.take());
        }
    }

    public EnumGroupType getEnumGroupType() {
        return enumGroupType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DeclaredType) {
            if (this.enumGroupType.equals((DeclaredType) obj)) return true;
        }
        return false;
    }

    @Override
    public boolean contain(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object value) {
        if (!(value instanceof EnumElementValue)) {
            return false;
        }
        return this.enumGroupType.contain(f, main, value);
    }

    public int indexOf(EnumElementValue key) {
        return this.enumGroupType.indexOf(key);
    }

    @Override
    public String toString() {
        return null;
    }
}
