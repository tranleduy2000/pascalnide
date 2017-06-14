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

package com.duy.pascal.backend.types;

import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.operator.OperationNotSupportedException;
import com.duy.pascal.backend.tokens.Token.Precedence;
import com.duy.pascal.backend.types.set.ArrayType;
import com.duy.pascal.backend.types.set.SetType;

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
            return type.equals(BasicType.Boolean);
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Negation;
        }

        @Override
        public String toString() {

            return "not";
        }

    },
    MULTIPLY(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.Character)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "*";
        }
    },
    DIVIDE(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.Character)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "/";
        }
    },
    DIV(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder)
                    || GCF.equals(BasicType.Character));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
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
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
        }

        @Override
        public String toString() {
            return "mod";
        }
    },
    AND(false, false) {
        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
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
            return !(GCF.equals(BasicType.Boolean));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Additive;
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
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Additive;
        }

        @Override
        public String toString() {

            return "-";
        }
    },
    OR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF.equals(BasicType.Boolean));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Additive;
        }

        @Override
        public String toString() {
            return "or";
        }
    },
    XOR(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF.equals(BasicType.Boolean));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Additive;
        }

        @Override
        public String toString() {
            return "xor";
        }
    },
    SHIFTLEFT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF.equals(BasicType.Integer)
                    || GCF.equals(BasicType.Long));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "shl";
        }
    },
    SHIFTRIGHT(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return (GCF.equals(BasicType.Integer)
                    || GCF != BasicType.Long);
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "shr";
        }
    },
    LESSTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Relational;
        }

        @Override
        public String toString() {
            return "<";
        }
    },
    GREATERTHAN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Relational;
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
        public Precedence getPrecedence() {
            return Precedence.Relational;
        }

        @Override
        public String toString() {
            return "=";
        }
    },
    LESSEQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Relational;
        }

        @Override
        public String toString() {
            return "<=";
        }

    },
    GREATEREQ(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return !(GCF.equals(BasicType.Boolean)
                    || GCF.equals(BasicType.StringBuilder));
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Relational;
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
        public Precedence getPrecedence() {
            return Precedence.Relational;
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
        public Precedence getPrecedence() {
            return Precedence.Dereferencing;
        }

        @Override
        public String toString() {
            return "@";
        }
    },
    DEREF(true, true) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return true;
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Dereferencing;
        }

        @Override
        public String toString() {
            return "^";
        }

    },

    IN(false, false) {
        @Override
        public boolean verifyBinaryOperation(DeclaredType GCF) {
            return GCF.equals(BasicType.Boolean);
        }

        @Override
        public Precedence getPrecedence() {
            return Precedence.Relational;
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
        public Precedence getPrecedence() {
            return Precedence.Relational;
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
        if (one.equals(BasicType.StringBuilder)
                || two.equals(BasicType.StringBuilder)) {
            return BasicType.StringBuilder;
        }

        if (one == BasicType.Double
                || two == BasicType.Double) {
            if (one.equals(BasicType.Boolean)
                    || two.equals(BasicType.Boolean)) {
                return null;
            }
            return BasicType.Double;
        }
        if (one.equals(BasicType.Long) || two.equals(BasicType.Long)) {
            if (one.equals(BasicType.Boolean)
                    || two.equals(BasicType.Boolean)) {
                return null;
            }
            return BasicType.Long;
        }
        if (one.equals(BasicType.Integer)
                || two.equals(BasicType.Integer)) {
            if (one.equals(BasicType.Boolean)
                    || two.equals(BasicType.Boolean)) {
                return null;
            }
            return BasicType.Integer;
        }
        if (one.equals(BasicType.Boolean)
                && two.equals(BasicType.Boolean)) {
            return BasicType.Boolean;
        }
        if (one.equals(BasicType.Character)
                && two.equals(BasicType.Character)) {
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
                + " does not support operating on an integer type number");

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
        if (type.declType.equals(BasicType.Boolean)) {
            return operate((Boolean) o);
        }
        if (type.declType.equals(BasicType.Integer)) {
            return (int) ((long) operate(((int) o)));
        }
        if (type.declType.equals(BasicType.Long)) {
            return operate((long) o);
        }
        if (type.declType == BasicType.Double) {
            return operate((double) o);
        }
        throw new RuntimeException("unrecognized type " + o.getClass()
                + " for operation " + this);
    }

    public Object operate(String s1, String s2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on Strings");

    }

    public Precedence getPrecedence() {
        return null;
    }

}
