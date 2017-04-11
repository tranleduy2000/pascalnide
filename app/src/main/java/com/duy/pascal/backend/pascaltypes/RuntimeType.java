package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.boxing.GetAddress;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.js.interpreter.runtime.VariableBoxer;

import java.util.Iterator;

public class RuntimeType implements ArgumentType {

    public final DeclaredType declaredType;
    public final boolean writable;

    public RuntimeType(DeclaredType declaredType, boolean writable) {
        this.declaredType = declaredType;
        this.writable = writable;
    }

    public ReturnsValue convert(ReturnsValue value, ExpressionContext f)
            throws ParsingException {

        RuntimeType other = value.getType(f);
        if (writable) {
            if (this.equals(other)) {
                return new GetAddress(value);
            } else {
                return null;
            }
        }
        return declaredType.convert(value, f);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RuntimeType) {
            RuntimeType other = (RuntimeType) obj;
            return other.writable == this.writable
                    && this.declaredType.equals(other.declaredType);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return (writable ? "" : "non-") + "writable " + declaredType.toString();
    }

    @Override
    public Class<?> getRuntimeClass() {
        if (writable) {
            return VariableBoxer.class;
        } else {
            return declaredType.getTransferClass();
        }
    }

    @Override
    public ReturnsValue convertArgType(Iterator<ReturnsValue> args,
                                       ExpressionContext f) throws ParsingException {
        if (!args.hasNext()) {
            return null;
        }
        return convert(args.next(), f);
    }

    @Override
    public ReturnsValue perfectFit(Iterator<ReturnsValue> args,
                                   ExpressionContext e) throws ParsingException {
        if (!args.hasNext()) {
            return null;
        }
        ReturnsValue val = args.next();
        RuntimeType other = val.getType(e);
        if (this.declaredType.equals(other.declaredType)) {
            if (writable) {
                return new GetAddress(val);
            } else {
                return other.declaredType.cloneValue(val);
            }
        } else {
            return null;
        }
    }

}
