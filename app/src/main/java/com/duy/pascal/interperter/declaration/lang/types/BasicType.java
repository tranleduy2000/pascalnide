package com.duy.pascal.interperter.declaration.lang.types;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime.value.access.StringIndex;
import com.duy.pascal.interperter.ast.runtime.value.boxing.CharacterBoxer;
import com.duy.pascal.interperter.ast.runtime.value.boxing.StringBoxer;
import com.duy.pascal.interperter.ast.runtime.value.boxing.StringBuilderBoxer;
import com.duy.pascal.interperter.ast.runtime.value.cloning.StringBuilderCloner;
import com.duy.pascal.interperter.declaration.Modifier;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.types.converter.StringBuilderLimitBoxer;
import com.duy.pascal.interperter.declaration.lang.types.converter.TypeConverter;
import com.duy.pascal.interperter.declaration.lang.types.string.StringLimitType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.SubrangeType;
import com.duy.pascal.interperter.exceptions.parsing.index.NonArrayIndexed;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.io.File;

public enum BasicType implements Type {
    Boolean(Boolean.class) {
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
        public RuntimeValue convert(RuntimeValue other, ExpressionContext context) throws Exception {
            if (other instanceof ConstantAccess) {
                Name name = ((ConstantAccess) other).getName();
                if (name != null && name.equals("null")) {
                    return other;
                }
            }

            RuntimeType otherType = other.getRuntimeType(context);
            if (otherType.declType instanceof BasicType) {
                if (this.equals(otherType.declType)) {
                    return new StringBuilderBoxer(other);
                }
                if (otherType.declType.equals(BasicType.Character)) {
                    return new CharacterBoxer(other);
                }
                if (((BasicType) otherType.declType).clazz == String.class) {
                    return new StringBoxer(other);
                }
                return TypeConverter.autoConvert(this, other, otherType.declType);
            } else if (otherType.declType instanceof StringLimitType) {
                if (this.equals(otherType.declType)) {
                    return new StringBuilderLimitBoxer(other, ((StringLimitType) otherType.declType).getLength());
                }
                if (otherType.declType.equals(BasicType.Character)) {
                    return new CharacterBoxer(other);
                }
                if ((otherType.declType.getStorageClass() == String.class)) {
                    return new StringBoxer(other);
                }
                return TypeConverter.autoConvert(this, other, otherType.declType);
            } else if (otherType.declType instanceof JavaClassBasedType &&
                    otherType.declType.getStorageClass() == String.class) {
                return new StringBuilderBoxer(other);
            }
            return null;
        }

        @Override
        public boolean equals(@NonNull Type otherType) {
            return super.equals(otherType) || otherType instanceof StringLimitType;
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
    //integer in pascal is 2byte
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
            return 0f;
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
    private Name name;

    BasicType(Class name) {
        clazz = name;
    }

    public static Type create(Class c) {
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
    public boolean equals(@NonNull Type otherType) {
        if (this == otherType) {
            return true;
        }
        if (otherType instanceof JavaClassBasedType) {
            Class other = ((JavaClassBasedType) otherType).getStorageClass();
            return clazz == other || clazz == Object.class || other == Object.class;
        } else if (otherType instanceof SubrangeType) {
            return otherType.getStorageClass() == this.clazz;
        }
        return false;
    }

    @NonNull
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

    @NonNull
    @Override
    public Class getTransferClass() {
        return clazz;
    }

    @Override
    public abstract String toString();

    @Override
    public RuntimeValue convert(RuntimeValue other, ExpressionContext context)
            throws Exception {
        RuntimeType otherType = other.getRuntimeType(context);
        if (otherType.declType instanceof BasicType) {
            if (this.equals(otherType.declType)) {
                return cloneValue(other);
            }
            return TypeConverter.autoConvert(this, other, otherType.declType);
        } else {
            if (otherType.declType.getStorageClass() == getStorageClass()) {
                return cloneValue(other);
            }
        }
        return null;
    }

    @Override
    public RuntimeValue cloneValue(final RuntimeValue value) {
        return value;
    }

    @NonNull
    @Override
    public RuntimeValue generateArrayAccess(RuntimeValue array,
                                            RuntimeValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLineNumber(), this);
    }

    @NonNull
    @Override
    public Class<?> getStorageClass() {
        return clazz;
    }

    @NonNull
    @Override
    public LineInfo getLineNumber() {
        if (lineInfo == null) {
            return new LineInfo(-1, "");
        }
        return lineInfo;
    }

    @Override
    public void setLineNumber(@NonNull LineInfo lineNumber) {
        this.lineInfo = lineNumber;
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "basic type";
    }

    @Override
    public String getDescription() {
        return "basic type";
    }

    @NonNull
    @Override
    public Name getName() {
        return Name.create(toString());
    }

    @Override
    public void setName(Name name) {
        //this.name = name;
        //ignored
    }

    @Override
    public int getModifiers() {
        return Modifier.PUBLIC;
    }
}
