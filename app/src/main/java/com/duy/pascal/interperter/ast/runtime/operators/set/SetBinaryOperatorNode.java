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

package com.duy.pascal.interperter.ast.runtime.operators.set;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.operators.BinaryOperatorNode;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;
import com.duy.pascal.interperter.exceptions.runtime.CompileException;
import com.duy.pascal.interperter.exceptions.runtime.arith.PascalArithmeticException;
import com.duy.pascal.interperter.linenumber.LineNumber;

import java.util.LinkedList;


public class SetBinaryOperatorNode extends BinaryOperatorNode {


    public SetBinaryOperatorNode(RuntimeValue operon1, RuntimeValue operon2,
                                 OperatorTypes operator, LineNumber line) {
        super(operon1, operon2, operator, line);
    }

    @NonNull
    @Override
    public RuntimeType getRuntimeType(ExpressionContext context) throws Exception {
        switch (operatorType) {
            case PLUS:
            case MULTIPLY:
            case MINUS:
            case DIFFERENT:
                SetType type = (SetType) leftNode.getRuntimeType(context).declType;
                return new RuntimeType(type, false);

            case EQUALS:
            case NOTEQUAL:
            case LESSTHAN:
            case LESSEQ:
            case GREATERTHAN:
            case GREATEREQ:

                return new RuntimeType(BasicType.Boolean, false);
            default:
                return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object operate(Object value1, Object value2)
            throws PascalArithmeticException, CompileException {
        LinkedList v1 = (LinkedList) value1;
        LinkedList v2 = (LinkedList) value2;

        LinkedList result = new LinkedList<>();

        switch (operatorType) {
            case PLUS:
                result.clear();
                for (Object element : v2) if (!v1.contains(element)) result.add(element);

                return result;
            case MINUS:
                result.addAll(v1);
                for (Object element : v2) if (v1.contains(element)) result.remove(element);

                return result;

            case MULTIPLY:
                result.clear();
                for (Object element : v2) if (v1.contains(element)) result.add(element);

                return result;

            case DIFFERENT:
                result.clear();
                for (Object element : v1) if (!v2.contains(element)) result.add(element);
                for (Object element : v2) if (!v1.contains(element)) result.add(element);
                return result;

            case EQUALS:
                if (v1.size() != v2.size()) return false;
                for (Object element : v1) {
                    if (!v2.contains(element)) return false;
                }
                return true;
            case NOTEQUAL:
                if (v1.size() != v2.size()) return true;

                for (Object element : v1) {
                    if (v2.contains(element)) return false;
                }
                return true;

            case LESSTHAN:
                if (v1.size() >= v2.size()) return false;
                for (Object e : v1) if (!v2.contains(e)) return false;
                return true;

            case LESSEQ:
                if (v1.size() > v2.size()) return false;
                for (Object e : v1) if (!v2.contains(e)) return false;
                return true;

            case GREATERTHAN:
                if (v1.size() <= v2.size()) return false;

                for (Object e : v2) if (!v1.contains(e)) return false;
                return true;

            case GREATEREQ:

                if (v1.size() < v2.size()) return false;
                for (Object e : v2) if (!v1.contains(e)) return false;
                return true;

            default:
                throw new CompileException();
        }
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws Exception {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess<>(val, line);
        } else {
            return new SetBinaryOperatorNode(
                    leftNode.compileTimeExpressionFold(context),
                    rightNode.compileTimeExpressionFold(context), operatorType,
                    line);
        }
    }

    @Override
    public void setLineNumber(LineNumber lineNumber) {

    }

    @Override
    public boolean canDebug() {
        return true;
    }
}
