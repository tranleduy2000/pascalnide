package com.js.interpreter.ast.returnsvalue;

import com.duy.pascal.backend.debugable.DebuggableLValue;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ObjectType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.FieldReference;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldAccess extends DebuggableLValue {
    private RValue container;
    private String name;
    private LineInfo line;

    public FieldAccess(RValue container, String name, LineInfo line) {
        this.container = container;
        this.name = name;
        this.line = line;
    }

    public FieldAccess(RValue container, WordToken name) {
        this(container, name.name, name.lineInfo);
    }


    @Override
    public RuntimeType get_type(ExpressionContext f) throws ParsingException {
        RuntimeType r = container.get_type(f);
        return new RuntimeType(((ObjectType) (r.declType)).getMemberType(name),
                r.writable);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        Object value = container.compileTimeValue(context);
        if (value != null) {
            try {
                return ((ContainsVariables) value).get_var(name);
            } catch (RuntimePascalException e) {
                throw new ConstantCalculationException(e);
            }
        } else {
            return null;
        }
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object value = container.getValue(f, main);
        return ((ContainsVariables) value).get_var(name);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        return new FieldReference((ContainsVariables)container.getValue(f,main), name);
    }

    @Override
    public RValue compileTimeExpressionFold(CompileTimeContext context)
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
