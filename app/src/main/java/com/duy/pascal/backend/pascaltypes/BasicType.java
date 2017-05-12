package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnsupportedFormatException;
import com.duy.pascal.backend.pascaltypes.typeconversion.StringBuilderWithRangeType;
import com.duy.pascal.backend.pascaltypes.typeconversion.TypeConverter;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.StringIndex;
import com.js.interpreter.ast.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.StringBoxer;
import com.js.interpreter.ast.returnsvalue.cloning.StringBuilderCloner;
import com.ncsa.common.util.TypeUtils;

import java.io.File;
import java.net.Socket;

public enum BasicType implements DeclaredType {
    Boolean(Character.class) {
        @Override
        Object getDefaultValue() {
            return false;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();

        }

        @Override
        public String toString() {
            return "Boolean";
        }


    },
    Character(Character.class) {
        @Override
        Object getDefaultValue() {
            return '\0';
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Char";
        }

    },
    StringBuilder(StringBuilder.class) {
        private ReturnValue length; //max size

        @Override
        Object getDefaultValue() {
            return new StringBuilder();
        }

        @Override
        public void setLength(ReturnValue length) {
            this.length = length;
        }



        @Override
        public String toString() {
            return "String";
        }

        @Override
        public ReturnValue convert(ReturnValue valueToAssign, ExpressionContext f)
                throws ParsingException {
            RuntimeType otherType = valueToAssign.getType(f);
            if (otherType.declType instanceof BasicType) {
                if (this.equals(otherType.declType)) {
                    return new StringBuilderWithRangeType(valueToAssign, length);
                }
                if (otherType.declType == BasicType.Character) {
                    return new CharacterBoxer(valueToAssign);
                }
                if (((BasicType) otherType.declType).c == String.class) {
                    return new StringBoxer(valueToAssign);
                }
                return TypeConverter.autoConvert(this, valueToAssign, (BasicType) otherType.declType);
            }
            return null;
        }

        @Override
        public ReturnValue generateArrayAccess(ReturnValue array,
                                               ReturnValue index) throws NonArrayIndexed {
            return new StringIndex(array, index);
        }

        @Override
        public ReturnValue cloneValue(ReturnValue value) {
            return new StringBuilderCloner(value);
        }

    },
    Byte(Byte.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Short";
        }

    },
    Short(Short.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }

        @Override
        public void setLength(ReturnValue length) {
        }

        @Override
        public String toString() {
            return "Short";
        }


    },
    Integer(Integer.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Integer";
        }


    },
    Long(Long.class) {
        @Override
        Object getDefaultValue() {
            return 0L;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Long";
        }
    },
    Double(Double.class) {
        @Override
        Object getDefaultValue() {
            return 0.0D;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Real";
        }


    },
    Text(File.class) {
        @Override
        Object getDefaultValue() {
            return null;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "File";
        }

    },
    Socket(Socket.class) {
        @Override
        Object getDefaultValue() {
            return null;
        }

        @Override
        public void setLength(ReturnValue length) throws UnsupportedFormatException {
            throw new UnsupportedFormatException();
        }

        @Override
        public String toString() {
            return "Socket";
        }

    };

    private Class c;

    BasicType(Class name) {
        c = name;
    }

    public static DeclaredType create(Class c) {
        if (c == Integer.class) {
            return Integer;
        }
        if (c == Double.class) {
            return Double;
        }
        if (c == StringBuilder.class) {
            return StringBuilder;
        }
        if (c == Long.class) {
            return Long;
        }
        if (c == Character.class) {
            return Character;
        }
        if (c == Boolean.class) {
            return Boolean;
        }
        if (c == File.class) {
            return Text;
        }
        return new JavaClassBasedType(c);
    }


    abstract Object getDefaultValue();

    @Override
    public boolean equals(DeclaredType obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof JavaClassBasedType) {
            Class other = ((JavaClassBasedType) obj).clazz;
            return c == other || c == Object.class || other == Object.class;
        }
        return false;
    }

    @Override
    public Object initialize() {
        Object result;
        if ((result = getDefaultValue()) != null) {
            return result;
        } else {
            try {
                return c.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * set length of string operator
     *
     * @param length
     */
    public abstract void setLength(ReturnValue length) throws UnsupportedFormatException;

    @Override
    public Class getTransferClass() {
        return c;
    }

    @Override
    public abstract String toString();

    @Override
    public ReturnValue convert(ReturnValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType otherType = value.getType(f);
        if (otherType.declType instanceof BasicType) {
            if (this.equals(otherType.declType)) {
                return cloneValue(value);
            }
            return TypeConverter.autoConvert(this, value,
                    (BasicType) otherType.declType);
        } else {
            if (otherType.declType instanceof JavaClassBasedType) {
                if (otherType.declType.getStorageClass() == getStorageClass()) {
                    return cloneValue(value);
                }
            }
        }
        return null;
    }


    @Override
    public ReturnValue cloneValue(final ReturnValue r) {
        return r;
    }


    @Override
    public ReturnValue generateArrayAccess(ReturnValue array,
                                           ReturnValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        Class c2 = TypeUtils.getTypeForClass(c);
        return c2 == null ? c : c2;
    }


}
