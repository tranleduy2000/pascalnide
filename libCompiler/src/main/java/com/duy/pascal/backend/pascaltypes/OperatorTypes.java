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

package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.operator.OperationNotSupportedException;
import com.duy.pascal.backend.pascaltypes.set.ArrayType;
import com.duy.pascal.backend.pascaltypes.set.SetType;
import com.duy.pascal.backend.tokens.Token.precedence;

public enum OperatorTypes {
    NOT(true, false) {
        @Override
        public Object operate(boolean b) throws OperationNotSupportedException {
            return !b;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return ~l;
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType type) {
            return type == BasicType.Boolean;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Negation;
        }

        @Override
        public String toString() {

            return "not";
        }

    },
    MULTIPLY(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "*";
        }
    },
    DIVIDE(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "/";
        }
    },
    DIV(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder
                    || GCF == BasicType.Character);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "div";
        }
    },
    MOD(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF != BasicType.Integer
                    && GCF != BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "mod";
        }
    },
    AND(false, false) {
        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "and";
        }
    },
    PLUS(true, false) {
        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return d;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return l;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return new StringBuilder(s1).append(s2);
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }

        @Override
        public String toString() {
            return "+";
        }
    },
    MINUS(true, false) {
        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return -l;
        }

        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return -d;
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }

        @Override
        public String toString() {

            return "-";
        }
    },
    OR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }

        @Override
        public String toString() {
            return "or";
        }
    },
    XOR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF == BasicType.Boolean);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Additive;
        }

        @Override
        public String toString() {
            return "xor";
        }
    },
    SHIFTLEFT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF == BasicType.Integer
                    || GCF == BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "shl";
        }
    },
    SHIFTRIGHT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF == BasicType.Integer
                    || GCF != BasicType.Long);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "shr";
        }
    },
    LESSTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "<";
        }
    },
    GREATERTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return ">";
        }
    },
    EQUALS(false, false) {
        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return s1.equals(s2);
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "=";
        }
    },
    LESSEQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "<=";
        }

    },
    GREATEREQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder);
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return ">=";
        }

    },
    NOTEQUAL(false, false) {
        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return !(s1.equals(s2));
        }

        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "<>";
        }

    },
    ADDRESS(true, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Dereferencing;
        }

        @Override
        public String toString() {
            return "^";
        }
    },
    DEREF(true, true) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Dereferencing;
        }

        @Override
        public String toString() {
            return "@";
        }

    },

    IN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return GCF == BasicType.Boolean;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "in";
        }
    },
    DIFFERENT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return GCF instanceof SetType;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "><";
        }
    };

    public boolean canBeUnary;
    public boolean postfix;

    OperatorTypes(boolean canBeUnary, boolean postfix) {
        this.canBeUnary = canBeUnary;
        this.postfix = postfix;
    }

    public static DeclaredType get_GCF(DeclaredType one, DeclaredType two) {
        if (one == BasicType.StringBuilder
                || two == BasicType.StringBuilder) {
            return BasicType.StringBuilder;
        }

        if (one == BasicType.Double
                || two == BasicType.Double) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Double;
        }
        if (one == BasicType.Long || two == BasicType.Long) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Long;
        }
        if (one == BasicType.Integer
                || two == BasicType.Integer) {
            if (one == BasicType.Boolean
                    || two == BasicType.Boolean) {
                return null;
            }
            return BasicType.Integer;
        }
        if (one == BasicType.Boolean
                && two == BasicType.Boolean) {
            return BasicType.Boolean;
        }
        if (one == BasicType.Character
                && two == BasicType.Character) {
            return BasicType.Character;
        }
        //in
        if (one instanceof BasicType
                && two instanceof ArrayType) {
            ArrayType arrayType = (ArrayType) two;
            if (arrayType.elementType.equals(one)) {
                return BasicType.Boolean;
            }
        }
        if (one instanceof SetType && two instanceof SetType) {
            if (((SetType) one).getElementType().equals(((SetType) two).getElementType())) {
                return one;
            }
        }
        return null;
    }

    public Object operate(boolean b) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on a boolean");

    }

    public Object operate(double d) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on a floating point number");

    }

    public Object operate(long l) throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on an integer operator number");

    }

    public boolean verifyBinaryOperation(DeclaredType type1, DeclaredType type2)
            throws BadOperationTypeException {
        DeclaredType GCF = get_GCF(type1, type2);
        return GCF != null && verifyBinaryOperation(GCF);
    }

    boolean verifyBinaryOperation(DeclaredType GCF) {
        return true;
    }

    public boolean verifyUnaryOperation(DeclaredType type) {
        return true;
    }

    public Object operate(Object o, RuntimeType type) throws OperationNotSupportedException {
        if (type.declType == BasicType.Boolean) {
            return operate((Boolean) o);
        }
        if (type.declType == BasicType.Integer) {
            return (int) ((long) operate(((int) o)));
        }
        if (type.declType == BasicType.Long) {
            return operate((long) o);
        }
        if (type.declType == BasicType.Double) {
            return operate((double) o);
        }
        throw new RuntimeException("unrecognized operator " + o.getClass()
                + " for operation " + this);
    }

    public Object operate(String s1, String s2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on Strings");

    }

    public precedence getPrecedence() {
        return null;
    }

}
