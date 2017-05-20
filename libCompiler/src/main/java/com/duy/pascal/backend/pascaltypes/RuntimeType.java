package com.duy.pascal.backend.pascaltypes;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime_value.AssignableValue;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime_value.boxing.GetAddress;
import com.js.interpreter.runtime.references.PascalReference;

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
        System.out.println("runtimeClass = [" + runtimeClass + "], index = [" + index + "]");
        if (index == 1) {
            return runtimeClass == double.class || runtimeClass == int.class
                    || runtimeClass == boolean.class || runtimeClass == long.class
                    || runtimeClass == char.class || runtimeClass == String.class
                    || runtimeClass == StringBuilder.class
                    || runtimeClass == Boolean.class || runtimeClass == Byte.class
                    || runtimeClass == Character.class
                    || runtimeClass == Double.class || runtimeClass == Float.class
                    || runtimeClass == Integer.class || runtimeClass == Long.class;
        } else if (index == 2) {
            return runtimeClass == double.class || runtimeClass == Double.class ||
                    runtimeClass == Float.class || runtimeClass == float.class;
        }
        return false;
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
        return (writable ? "" : "non-") + "writable " + declType.toString();
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
        RuntimeValue val = args.next();
        RuntimeType other = val.getType(e);
        if (this.declType.equals(other.declType)) {
            if (writable) {
                return new GetAddress((AssignableValue) val);
            } else {
                return other.declType.cloneValue(val);
            }
        } else {
            return null;
        }
    }


}
