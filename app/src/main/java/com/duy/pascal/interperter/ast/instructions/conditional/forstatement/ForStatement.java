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

package com.duy.pascal.interperter.ast.instructions.conditional.forstatement;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectDoTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.UnAssignableTypeException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.OperatorToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.AssignmentToken;
import com.duy.pascal.interperter.tokens.basic.BasicToken;
import com.duy.pascal.interperter.tokens.basic.DoToken;
import com.duy.pascal.interperter.tokens.basic.DowntoToken;
import com.duy.pascal.interperter.tokens.basic.ToToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

/**
 * Created by Duy on 19-Jun-17.
 */

public class ForStatement {
    /**
     * create executable for statement
     */
    public static Executable generateForStatement(GrouperToken group, ExpressionContext context,
                                                  LineInfo lineNumber) throws Exception {
        RuntimeValue identifier = group.getNextTerm(context);
        AssignableValue varAssignable = identifier.asAssignableValue(context);
        RuntimeType varType = identifier.getRuntimeType(context);

        if (varAssignable == null) {
            throw new UnAssignableTypeException(identifier);
        }
        Token next = group.take();
        if (!(next instanceof AssignmentToken
                || next instanceof OperatorToken)) {
            throw new ExpectedTokenException(next, ":=", "in");
        }
        Executable result;
        //case: for i :=
        if (next instanceof AssignmentToken) {
            RuntimeValue firstValue = group.getNextExpression(context);
            RuntimeValue convert = varType.getRawType().convert(firstValue, context);
            if (convert == null) {
                throw new UnConvertibleTypeException(firstValue, varType.getRawType(),
                        firstValue.getRuntimeType(context).declType, identifier, context);
            }
            firstValue = convert;

            next = group.take();
            boolean downto = false;
            //case: for i := ... to[downto] ..
            if (next instanceof DowntoToken) {
                downto = true;
            } else if (!(next instanceof ToToken)) {
                throw new ExpectedTokenException(next, "to", "downto");
            }
            RuntimeValue lastValue = group.getNextExpression(context);
            convert = varType.getRawType().convert(lastValue, context);
            if (convert == null) {
                throw new UnConvertibleTypeException(lastValue, varType.getRawType(),
                        lastValue.getRuntimeType(context).declType, identifier, context);
            }
            lastValue = convert;
            next = group.take();

            if (!(next instanceof DoToken)) {
                if (next instanceof BasicToken) {
                    throw new ExpectedTokenException("do", next);
                } else {
                    throw new ExpectDoTokenException(next.getLineNumber());
                }
            }

            if (varType.getRawType() instanceof EnumGroupType) {
                result = new ForEnumStatement(context, varAssignable, firstValue,
                        lastValue, group.getNextCommand(context),
                        (EnumGroupType) varType.getRawType(), lineNumber, downto);
            } else {
                result = new ForNumberStatement(context, varAssignable, firstValue,
                        lastValue, group.getNextCommand(context), lineNumber, downto);
            }
        } else {
            //case: for <var> in <range>
            if (((OperatorToken) next).type == OperatorTypes.IN) {
                //assign value
                RuntimeValue enumList = group.getNextExpression(context);
                Type enumType = enumList.getRuntimeType(context).declType; //type of var

                //accept foreach : enum, set, array
                if (!(enumType instanceof EnumGroupType
                        || enumType instanceof ArrayType
                        || enumType instanceof SetType)) {
                    throw new UnConvertibleTypeException(enumList, varType.declType, enumType, context);
                }

                if (enumType instanceof EnumGroupType) {
                    RuntimeValue converted = varType.convert(enumList, context);
                    if (converted == null) {
                        throw new UnConvertibleTypeException(enumList,
                                varType.declType, enumType, context);
                    }
                } else if (enumType instanceof ArrayType) { //array type
                    ArrayType arrayType = (ArrayType) enumType;
                    RuntimeValue convert = arrayType.getElementType().convert(identifier, context);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(identifier, arrayType.getElementType(), varType.declType, context);
                    }
                } else {
                    SetType setType = (SetType) enumType;
                    RuntimeValue convert = setType.getElementType().convert(identifier, context);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(identifier, setType.getElementType(), varType.declType, context);
                    }
                }

                //check do token
                if (!(group.peek() instanceof DoToken)) {
                    throw new ExpectedTokenException(new DoToken(null), group.peek());
                }
                group.take(); //ignore do token
                //statement
                Executable command = group.getNextCommand(context);
                return new ForInStatement(varAssignable, enumList, command, lineNumber);
            } else {
                throw new ExpectedTokenException(next, ":=", "in");
            }
        }

        return result;
    }

}
