package com.js.interpreter.runtime_value;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.debugable.DebuggableReturnValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;

public class ConstantAccess extends DebuggableReturnValue {
    public Object constant_value;
    private DeclaredType type;
    private LineInfo line;

    public ConstantAccess(@NonNull Object o, @Nullable LineInfo line) {
        this.constant_value = o;
        this.line = line;
    }

    public ConstantAccess(@NonNull Object o, @Nullable DeclaredType type, LineInfo line) {
        this.constant_value = o;
        this.type = type;
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main) {
        return constant_value;
    }

    @Override
    public String toString() {
        return constant_value.toString();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        if (type != null) {
            return new RuntimeType(type, false);
        }
        return new RuntimeType(BasicType.create(constant_value.getClass()), false);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return constant_value;
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }

}
