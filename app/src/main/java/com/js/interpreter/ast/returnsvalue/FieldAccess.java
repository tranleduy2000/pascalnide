package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.debugable.DebuggableReturnsValue;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ObjectType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.SetField;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldAccess extends DebuggableReturnsValue {
    ReturnsValue container;
    String name;
    LineInfo line;

    public FieldAccess(ReturnsValue container, String name, LineInfo line) {
        this.container = container;
        this.name = name;
        this.line = line;
    }

    public FieldAccess(ReturnsValue container, WordToken name) {
        this(container, name.name, name.lineInfo);
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType r = container.getType(f);
        return new RuntimeType(((ObjectType) (r.declaredType)).getMemberType(name),
                r.writable);
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object value = container.compileTimeValue(context);
        if (value != null) {
            try {
                return ((ContainsVariables) value).getVariable(name);
            } catch (RuntimePascalException e) {
                throw new ConstantCalculationException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public SetValueExecutable createSetValueInstruction(ReturnsValue r)
            throws UnAssignableTypeException {
        return new SetField(container, name, line, r);
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object value = container.getValue(f, main);
        return ((ContainsVariables) value).getVariable(name);
    }

    @Override
    public ReturnsValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new FieldAccess(
                    container.compileTimeExpressionFold(context), name, line);
        }
    }
}
