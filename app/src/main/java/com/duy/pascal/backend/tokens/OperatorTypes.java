package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.OperationNotSupportedException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.Token.precedence;

public enum OperatorTypes {
    NOT(true) {
        @Override
        public Object operate(boolean b) throws OperationNotSupportedException {
            return !b;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return ~l;
        }

        @Override
        public void verifyOperation(DeclaredType type)
                throws BadOperationTypeException {
            if (type != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
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
    MULTIPLY(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 * d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 * l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
        }

        @Override
        public Token.precedence getPrecedence() {
            return Token.precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "*";
        }
    },
    DIVIDE(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            if (d2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            return d1 / d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            if (l2 == 0) {
                throw new ArithmeticException("/ by zero");
            }
            return (double) l1 / (double) l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.Character
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    DIV(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return ((long) d1) / ((long) d2);
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 / l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder
                    || GCF == BasicType.Character) {
                throw new BadOperationTypeException();
            }
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
    MOD(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 % l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
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
    AND(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 && b2;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Multiplicative;
        }

        @Override
        public String toString() {

            return "and";
        }
    },
    PLUS(true) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 + d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 + l2;
        }

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
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
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
    MINUS(true) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 - l2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 - d2;
        }

        @Override
        public Object operate(long l) throws OperationNotSupportedException {
            return -l;
        }

        @Override
        public Object operate(double d) throws OperationNotSupportedException {
            return -d;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    OR(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 || b2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
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
    XOR(false) {
        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 ^ b2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 ^ l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Boolean) {
                throw new BadOperationTypeException();
            }
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
    SHIFTLEFT(false) {
        @Override
        public Object operate(long l1, long l2) {
            return l1 << l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
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
    SHIFTRIGHT(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 >> l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF != BasicType.Integer
                    && GCF != BasicType.Long) {
                throw new BadOperationTypeException();
            }
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
    LESSTHAN(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 < d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 < l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    GREATERTHAN(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 > d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 > l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    EQUALS(false) {
        @Override
        public Object operate(char c1, char c2)
                throws OperationNotSupportedException {
            return c1 == c2;
        }

        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 == b2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 == d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 == l2;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return s1.equals(s2);
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
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
    LESSEQ(false) {
        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 <= l2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 <= d2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    GREATEREQ(false) {
        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 >= d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 >= l2;
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            if (GCF == BasicType.Boolean
                    || GCF == BasicType.StringBuilder) {
                throw new BadOperationTypeException();
            }
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
    NOTEQUAL(false) {
        @Override
        public Object operate(char c1, char c2)
                throws OperationNotSupportedException {
            return c1 != c2;
        }

        @Override
        public Object operate(boolean b1, boolean b2)
                throws OperationNotSupportedException {
            return b1 != b2;
        }

        @Override
        public Object operate(double d1, double d2)
                throws OperationNotSupportedException {
            return d1 != d2;
        }

        @Override
        public Object operate(long l1, long l2)
                throws OperationNotSupportedException {
            return l1 != l2;
        }

        @Override
        public Object operate(String s1, String s2)
                throws OperationNotSupportedException {
            return !(s1.equals(s2));
        }

        @Override
        public void verifyOperation(DeclaredType GCF)
                throws BadOperationTypeException {
            return;
        }

        @Override
        public precedence getPrecedence() {
            return precedence.Relational;
        }

        @Override
        public String toString() {
            return "<>";
        }
    };

    public boolean can_be_unary;

    OperatorTypes(boolean can_be_unary) {
        this.can_be_unary = can_be_unary;
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
        return null;
    }

    public Object operate(char c1, char c2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on character types");
    }

    public Object operate(double d1, double d2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on floating point types");
    }

    public Object operate(long l1, long l2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on integer types");
    }

    public Object operate(boolean b1, boolean b2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this
                + " does not support operating on boolean types");

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

    public void verifyOperation(DeclaredType type1, DeclaredType type2)
            throws BadOperationTypeException {
        DeclaredType GCF = get_GCF(type1, type2);
        if (GCF == null) {
            throw new BadOperationTypeException();
        }
        verifyOperation(GCF);
    }

    void verifyOperation(DeclaredType GCF) throws BadOperationTypeException {
    }

    public Object operate(Object o) throws OperationNotSupportedException {
        if (o instanceof Boolean) {
            return operate(((Boolean) o).booleanValue());
        }
        if (o instanceof Integer) {
            return ((Long) operate(((Integer) o).longValue())).intValue();
        }
        if (o instanceof Long) {
            return operate(((Long) o).longValue());
        }
        if (o instanceof Double) {
            return operate(((Double) o).doubleValue());
        }
        throw new RuntimeException("unrecognized type " + o.getClass()
                + " for operation " + this);
    }

    public Object operate(String s1, String s2)
            throws OperationNotSupportedException {
        throw new OperationNotSupportedException(this + " does not support operating on Strings");

    }

    public Token.precedence getPrecedence() {
        return null;
    }

}
