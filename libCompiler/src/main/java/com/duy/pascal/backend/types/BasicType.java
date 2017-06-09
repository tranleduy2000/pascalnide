package com.duy.pascal.backend.types;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.StringIndex;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.CharacterBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.StringBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.StringBuilderBoxer;
import com.duy.pascal.backend.ast.runtime_value.value.cloning.StringBuilderCloner;
import com.duy.pascal.backend.types.converter.StringLimitBoxer;
import com.duy.pascal.backend.types.converter.TypeConverter;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.index.NonArrayIndexed;

import java.io.File;

public enum BasicType implements DeclaredType {
    Boolean(Character.class) {
        @Override
        Object getDefaultValue() {
            return false;
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
        public String toString() {
            return "Char";
        }

    },
    StringBuilder(StringBuilder.class) {
        @Override
        Object getDefaultValue() {
            return new StringBuilder();
        }

        @Override
        public String toString() {
            return "String";
        }

        @Override
        public RuntimeValue convert(RuntimeValue valueToAssign, ExpressionContext f)
                throws ParsingException {
            RuntimeType otherType = valueToAssign.getType(f);
            if (otherType.declType instanceof BasicType) {
                if (this.equals(otherType.declType)) {
                    return new StringBuilderBoxer(valueToAssign);
                }
                if (otherType.declType == BasicType.Character) {
                    return new CharacterBoxer(valueToAssign);
                }
                if (((BasicType) otherType.declType).clazz == String.class) {
                    return new StringBoxer(valueToAssign);
                }
                return TypeConverter.autoConvert(this, valueToAssign, (BasicType) otherType.declType);
            } else if (otherType.declType instanceof StringLimitType) {
                return new StringLimitBoxer(valueToAssign, ((StringLimitType) otherType.declType).getLength());
            }
            return null;
        }

        @Override
        public boolean equals(DeclaredType obj) {
            return super.equals(obj);
        }

        @NonNull
        @Override
        public RuntimeValue generateArrayAccess(RuntimeValue array,
                                                RuntimeValue index) throws NonArrayIndexed {
            return new StringIndex(array, index);
        }

        @Override
        public RuntimeValue cloneValue(RuntimeValue value) {
            //do not bring length to another variable
            return new StringBuilderCloner(value);
        }

    },
    Byte(Byte.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }


        @Override
        public String toString() {
            return "Byte";
        }

    },
    Short(Short.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }


        @Override
        public String toString() {
            return "Shortint";
        }


    },
    Integer(Integer.class) {
        @Override
        Object getDefaultValue() {
            return 0;
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
        public String toString() {
            return "Longint";
        }
    },
    Float(Float.class) {
        @Override
        Object getDefaultValue() {
            return 0d;
        }


        @Override
        public String toString() {
            return "Real";
        }


    },
    Double(Double.class) {
        @Override
        Object getDefaultValue() {
            return 0d;
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
        public String toString() {
            return "Text";
        }

    },;

    public Class clazz;
    private LineInfo lineInfo;
    private String name;

    BasicType(Class name) {
        clazz = name;
    }

    public static DeclaredType create(Class c) {
        if (c == Integer.class || c == int.class) {
            return Integer;
        }
        if (c == Double.class || c == double.class) {
            return Double;
        }
        if (c == StringBuilder.class) {
            return StringBuilder;
        }
        if (c == Long.class || c == long.class) {
            return Long;
        }
        if (c == Character.class || c == char.class) {
            return Character;
        }
        if (c == Boolean.class || c == boolean.class) {
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
            Class other = ((JavaClassBasedType) obj).getStorageClass();
            return clazz == other || clazz == Object.class;
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
                return clazz.newInstance();
            } catch (InstantiationException e) {
//                e.printStackTrace();
            } catch (IllegalAccessException e) {
//                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @Override
    public abstract String toString();

    @Override
    public RuntimeValue convert(RuntimeValue value, ExpressionContext f)
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
    public RuntimeValue cloneValue(final RuntimeValue r) {
        return r;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }

    @Override
    public LineInfo getLineNumber() {
        if (lineInfo == null) {
            return new LineInfo(-1, "");
        }
        return lineInfo;
    }

    @Override
    public void setLineNumber(LineInfo lineNumber) {
        this.lineInfo = lineNumber;
    }

    @Override
    public String getEntityType() {
        return "basic type";
    }

    @Override
    public String getDescription() {
        return "basic type";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
