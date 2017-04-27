package com.js.interpreter.ast.returnsvalue;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.duy.pascal.backend.exceptions.ChangeValueConstantException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;

public class ConstantAccess extends DebuggableReturnsValue {
    public Object constantValue;
    private DeclaredType type;
    private LineInfo line;

    public ConstantAccess(@NonNull Object o, @Nullable LineInfo line) {
        this.constantValue = o;
        this.line = line;
    }

    public ConstantAccess(@NonNull Object o, @Nullable DeclaredType type, LineInfo line) {
        this.constantValue = o;
        this.type = type;
        this.line = line;
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main) {
        return constantValue;
    }

    @Override
    public String toString() {
        return constantValue.toString();
    }


    @Override
    public RuntimeType getType(ExpressionContext f) {
        if (type != null) {
            return new RuntimeType(type, false);
        }
        return new RuntimeType(BasicType.anew(constantValue.getClass()), false);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context) {
        return constantValue;
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException, ChangeValueConstantException {
        throw new ChangeValueConstantException(line, constantValue.toString(), "", constantValue);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }

}
