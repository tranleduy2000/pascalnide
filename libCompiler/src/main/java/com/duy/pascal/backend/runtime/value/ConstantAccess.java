package com.duy.pascal.backend.runtime.value;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;

public class ConstantAccess<T> extends DebuggableReturnValue {
    private T value;
    private DeclaredType type;
    private LineInfo mLineNumber;
    @Nullable
    private String name = null;

    public ConstantAccess(@Nullable T o, @Nullable LineInfo mLineNumber) {
        this.value = o;
        this.mLineNumber = mLineNumber;
    }

    public ConstantAccess(@Nullable T o, @Nullable DeclaredType type, @Nullable LineInfo mLineNumber) {
        this.value = o;
        this.type = type;
        this.mLineNumber = mLineNumber;
    }

    public T getValue() {
        return value;
    }

    @Override
    public LineInfo getLineNumber() {
        return mLineNumber;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) {
        return value;
    }

    @Override
    public String toString() {
        if (name == null) {
            return String.valueOf(value);
        } else {
            return name + (value != null ? " = " + value : "");
        }
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        if (type != null) {
            return new RuntimeType(type, false);
        }
        return new RuntimeType(BasicType.create(value.getClass()), false);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return value;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
