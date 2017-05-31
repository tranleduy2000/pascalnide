package com.duy.pascal.backend.ast.runtime_value.value;

import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;

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

    public String toCode() {
        if (value instanceof StringBuilder || value instanceof String || value instanceof Character) {
            return "'" + value.toString() + "'";
        }
        return value.toString();
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
