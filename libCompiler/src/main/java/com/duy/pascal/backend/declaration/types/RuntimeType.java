package com.duy.pascal.backend.declaration.types;

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.boxing.GetAddress;
import com.duy.pascal.backend.ast.runtime_value.value.RecordValue;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.declaration.types.set.ArrayType;
import com.duy.pascal.backend.declaration.types.set.SetType;
import com.duy.pascal.backend.declaration.types.util.TypeUtils;

import java.util.Iterator;

public class RuntimeType implements ArgumentType {

    public final DeclaredType declType;
    public final boolean writable;

    public RuntimeType(DeclaredType declType, boolean writable) {
        this.declType = declType;
        this.writable = writable;
    }

    /**
     * return <code>true</code> if this return string, integer, long, double, char
     * otherwise return false
     */
    public static boolean canOutputWithFormat(Class<?> runtimeClass, int index) {
        if (index == 1) {
            return TypeUtils.isPrimitiveWrapper(runtimeClass) || runtimeClass == StringBuilder.class
                    || runtimeClass == String.class || runtimeClass == RecordValue.class;
        } else if (index == 2) {
            return TypeUtils.isNumber(runtimeClass);
        }
        return false;
    }

    public boolean isWritable() {
        return writable;
    }

    public DeclaredType getDeclType() {
        return declType;
    }

    public RuntimeValue convert(RuntimeValue value, ExpressionContext f)
            throws ParsingException {
        RuntimeType other = value.getType(f);
        if (writable) {
            if (this.equals(other)) {
                return new GetAddress((AssignableValue) value);
            } else {
                return null;
            }
        }
        return declType.convert(value, f);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RuntimeType) {
            RuntimeType other = (RuntimeType) obj;
            return other.writable == this.writable
                    && this.declType.equals(other.declType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return (writable ? "^" : "") + declType.toString();
    }

    @Override
    public Class<?> getRuntimeClass() {
        if (writable) {
            return PascalReference.class;
        } else {
            return declType.getTransferClass();
        }
    }

    @Override
    public RuntimeValue convertArgType(Iterator<RuntimeValue> args,
                                       ExpressionContext f) throws ParsingException {
        if (!args.hasNext()) {
            return null;
        }
        return convert(args.next(), f);
    }

    @Override
    public RuntimeValue perfectFit(Iterator<RuntimeValue> args,
                                   ExpressionContext e) throws ParsingException {
        if (!args.hasNext()) {
            return null;
        }
        RuntimeValue otherValue = args.next();
        RuntimeType otherType = otherValue.getType(e);
        if (this.declType.equals(otherType.declType)) {
            if (writable) {
                return new GetAddress((AssignableValue) otherValue);
            } else {
                return otherType.declType.cloneValue(otherValue);
            }
        } else if (this.declType instanceof ArrayType && ((ArrayType) this.declType).isDynamic()
                && otherType.declType instanceof SetType) { //dynamic array
            if (writable) {
                return null;
            } else {
                return this.declType.convert(otherValue, e);
            }
        } else {
            return null;
        }
    }


}
