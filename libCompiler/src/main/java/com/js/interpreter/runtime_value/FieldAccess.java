package com.js.interpreter.runtime_value;

import android.util.Log;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.exceptions.operator.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.ObjectType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.FieldReference;
import com.js.interpreter.runtime.references.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class FieldAccess extends DebuggableAssignableValue {
    private static final String TAG = "FieldAccess";
    private RuntimeValue container;
    private String name;
    private LineInfo line;

    public FieldAccess(RuntimeValue container, String name, LineInfo line) {
        Log.d(TAG, "FieldAccess() called with: container = [" + container + "], name = [" + name + "], line = [" + line + "]");
        this.container = container;
        this.name = name;
        this.line = line;
    }

    public FieldAccess(RuntimeValue container, WordToken name) {
        this(container, name.name, name.lineInfo);
    }


    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        RuntimeType r = container.getType(f);
        if (r.declType instanceof ObjectType) {
            return new RuntimeType(((ObjectType) (r.declType)).getMemberType(name), r.writable);
        } else if (r.declType instanceof JavaClassBasedType) {
            return new RuntimeType(r.declType, r.writable);
        }
        return null;
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
                return ((ContainsVariables) value).getVar(name);
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
        return ((ContainsVariables) value).getVar(name);
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutable<?> main) throws RuntimePascalException {
        return new FieldReference((ContainsVariables) container.getValue(f, main), name);
    }

    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        Object val = this.compileTimeValue(context);
        if (val != null) {
            return new ConstantAccess(val, line);
        } else {
            return new FieldAccess(container.compileTimeExpressionFold(context), name, line);
        }
    }

    public RuntimeValue getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }
}
