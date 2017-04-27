package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.NonArrayIndexed;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.pascaltypes.bytecode.RegisterAllocator;
import com.duy.pascal.backend.pascaltypes.bytecode.TransformationInput;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.duy.pascal.backend.pascaltypes.typeconversion.StringBuilderWithRangeType;
import com.duy.pascal.backend.pascaltypes.typeconversion.TypeConverter;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.StringIndexAccess;
import com.js.interpreter.ast.returnsvalue.boxing.CharacterBoxer;
import com.js.interpreter.ast.returnsvalue.boxing.StringBoxer;
import com.js.interpreter.ast.returnsvalue.cloning.StringBuilderCloner;
import com.ncsa.common.util.TypeUtils;

import java.io.File;
import java.util.List;

import serp.bytecode.Code;

public enum BasicType implements DeclaredType {
    Boolean(Character.class) {
        @Override
        Object getDefaultValue() {
            return false;
        }

        @Override
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;
        }

        @Override
        public String toString() {
            return "Boolean";
        }

        @Override
        public void convertStackToStorageType(Code c) {
            c.invokestatic().setMethod(Boolean.class, "valueOf", Boolean.class, new Class[]{boolean.class});
        }

        @Override
        public void arrayStoreOperation(Code c) {
            c.bastore();
        }
    },
    Character(Character.class) {
        @Override
        Object getDefaultValue() {
            return '\0';
        }

        @Override
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;

        }

        @Override
        public String toString() {
            return "Character";
        }

        @Override
        public void convertStackToStorageType(Code c) {
            c.invokestatic().setMethod(Character.class, "valueOf",
                    Character.class, new Class[]{char.class});
        }

        @Override
        public void arrayStoreOperation(Code c) {
            c.castore();
        }
    },
    StringBuilder(StringBuilder.class) {
        private ReturnsValue length; //max size

        @Override
        Object getDefaultValue() {
            return new StringBuilder();
        }

        @Override
        public void setLength(ReturnsValue length) {
            this.length = length;
        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;
        }

        @Override
        public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
            constructor_code.anew().setType(StringBuilder.class);
            constructor_code.dup();
            try {
                constructor_code.invokespecial().setMethod(StringBuilder.class.getConstructor());
            } catch (SecurityException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return "String";
        }

        @Override
        public ReturnsValue convert(ReturnsValue valueToAssign, ExpressionContext f)
                throws ParsingException {
            RuntimeType otherType = valueToAssign.getType(f);

            if (otherType.declaredType instanceof BasicType) {
                if (this.equals(otherType.declaredType)) {
                    return new StringBuilderWithRangeType(valueToAssign, length);
                }
                if (otherType.declaredType == BasicType.Character) {
                    return new CharacterBoxer(valueToAssign);
                }
                if (((BasicType) otherType.declaredType).storeClass == String.class) {
                    return new StringBoxer(valueToAssign);
                }
                return TypeConverter.autoConvert(this, valueToAssign, (BasicType) otherType.declaredType);
            }
            return null;
        }

        @Override
        public ReturnsValue generateArrayAccess(ReturnsValue array,
                                                ReturnsValue index) throws NonArrayIndexed {
            return new StringIndexAccess(array, index);
        }

        @Override
        public ReturnsValue cloneValue(ReturnsValue value) {
            return new StringBuilderCloner(value);
        }

        @Override
        public void cloneValueOnStack(TransformationInput t) {
            Code c = t.getCode();
            c.anew().setType(StringBuilder.class);
            t.pushInputOnStack();
            try {
                c.invokespecial().setMethod(
                        StringBuilder.class.getConstructor(CharSequence.class));
            } catch (SecurityException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

    },
    Long(Long.class) {
        @Override
        Object getDefaultValue() {
            return 0L;
        }

        @Override
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;

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
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;

        }

        @Override
        public String toString() {
            return "Double";
        }

        @Override
        public void convertStackToStorageType(Code c) {
            c.invokestatic().setMethod(Double.class, "valueOf", Double.class,
                    new Class[]{double.class});
        }

        @Override
        public void arrayStoreOperation(Code c) {
            c.dastore();
        }
    },
    Integer(Integer.class) {
        @Override
        Object getDefaultValue() {
            return 0;
        }

        @Override
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;
        }

        @Override
        public String toString() {
            return "Integer";
        }

        @Override
        public void convertStackToStorageType(Code c) {
            c.invokestatic().setMethod(Integer.class, "valueOf", Integer.class, new Class[]{int.class});
        }

        @Override
        public void arrayStoreOperation(Code c) {
            c.iastore();
        }
    },
    Text(File.class) {
        @Override
        Object getDefaultValue() {
            return null;
        }

        @Override
        public void setLength(ReturnsValue length) {

        }

        @Override
        public ReturnsValue getStringWithLength(StringBuilder stringBuilder) {
            return null;
        }

        @Override
        public String toString() {
            return "File";
        }

        @Override
        public void convertStackToStorageType(Code c) {
        }

        @Override
        public void arrayStoreOperation(Code c) {
            c.iastore();
        }
    };

    private Class storeClass;

    BasicType(Class name) {
        storeClass = name;
    }

    public static DeclaredType anew(Class c) {
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
            Class other = ((JavaClassBasedType) obj).c;
            return storeClass == other || storeClass == Object.class || other == Object.class;
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
                return storeClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * set length of string type
     *
     * @param length
     */
    public abstract void setLength(ReturnsValue length);

    public abstract ReturnsValue getStringWithLength(StringBuilder stringBuilder);

    @Override
    public Class getTransferClass() {
        return storeClass;
    }

    @Override
    public abstract String toString();

    @Override
    public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other_type = value.getType(f);
        if (other_type.declaredType instanceof BasicType) {
            if (this.equals(other_type.declaredType)) {
                return cloneValue(value);
            }
            return TypeConverter.autoConvert(this, value,
                    (BasicType) other_type.declaredType);
        }
        return null;
    }

    @Override
    public void pushDefaultValue(Code constructor_code, RegisterAllocator ra) {
        constructor_code.constant().setValue(getDefaultValue());
    }

    @Override
    public ReturnsValue cloneValue(final ReturnsValue r) {
        return r;
    }

    @Override
    public void cloneValueOnStack(TransformationInput t) {
        t.pushInputOnStack();
    }

    @Override
    public ReturnsValue generateArrayAccess(ReturnsValue array,
                                            ReturnsValue index) throws NonArrayIndexed {
        throw new NonArrayIndexed(array.getLine(), this);
    }

    @Override
    public Class<?> getStorageClass() {
        Class c2 = TypeUtils.getTypeForClass(storeClass);
        return c2 == null ? storeClass : c2;
    }

    @Override
    public void arrayStoreOperation(Code c) {
        c.aastore();
    }

    @Override
    public void convertStackToStorageType(Code c) {
        // By default, nothing is necessary.
    }

    @Override
    public void pushArrayOfType(Code code, RegisterAllocator ra,
                                List<IntegerSubrangeType> ranges) {
        // Because I cannot mix this method into DeclaredType (no multiple
        // inheritance) I have to duplicate it.
        ArrayType.pushArrayOfNonArrayType(this, code, ra, ranges);

    }
}
