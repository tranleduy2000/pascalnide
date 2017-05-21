package com.js.interpreter.runtime_value;

import android.util.Log;

import com.duy.pascal.backend.debugable.DebuggableAssignableValue;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.tokens.WordToken;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.expressioncontext.ExpressionContextMixin;
import com.js.interpreter.instructions.FieldReference;
import com.js.interpreter.runtime.FunctionOnStack;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.references.Reference;

public class VariableAccess extends DebuggableAssignableValue {
    private static final String TAG = "VariableAccess";
    public String name;
    private LineInfo line;

    public VariableAccess(WordToken t) {
        this.name = t.name;
        this.line = t.lineInfo;
    }

    public VariableAccess(String name, LineInfo line) {
        this.name = name;
        this.line = line;
    }

    @Override
    public RuntimeValue[] getOutputFormat() {
        return super.getOutputFormat();
    }

    @Override
    public void setOutputFormat(RuntimeValue[] formatInfo) {
        super.setOutputFormat(formatInfo);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Object getValueImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        Log.d(TAG, "getValueImpl() called with: f = [" + f + "], main = [" + main + "]");

        Object var = f.getVar(name);
        Log.d(TAG, "getValueImpl() returned: " + var);
        return var;
    }

    @Override
    public Reference<?> getReferenceImpl(VariableContext f, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        try {
            if (f instanceof FunctionOnStack) {
                ExpressionContextMixin declarations = ((FunctionOnStack) f).getPrototype().declarations;
                RuntimeType type = getType(declarations);
                return new FieldReference(f, name, type);
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return new FieldReference(f, name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public RuntimeType getType(ExpressionContext f) throws ParsingException {
        return new RuntimeType(f.getVariableDefinition(name).type, true);
    }

    @Override
    public Object compileTimeValue(CompileTimeContext context)
            throws ParsingException {
        return null;
    }


    @Override
    public RuntimeValue compileTimeExpressionFold(CompileTimeContext context)
            throws ParsingException {
        return this;
    }
}
