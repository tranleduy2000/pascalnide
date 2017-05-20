package com.js.interpreter.instructions;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.runtime_value.AssignableValue;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.references.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class Assignment extends DebuggableExecutable implements SetValueExecutable {
    private AssignableValue left;

    private RuntimeValue value;
    private LineInfo line;

    public Assignment(@NonNull AssignableValue left, @NonNull RuntimeValue value, @NonNull LineInfo line) {
        this.left = left;
        this.value = value;
        this.line = line;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Reference ref = left.getReference(context, main);
        Object value = this.value.getValue(context, main);
        ref.set(value);
        return ExecutionResult.NONE;
    }

    @Override
    public String toString() {
        return left + " := " + value;
    }

    @Override
    public LineInfo getLineNumber() {
        return this.line;
    }

    @Override
    public void setAssignedValue(RuntimeValue value) {
        this.value = value;
    }

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new Assignment(left, value.compileTimeExpressionFold(c), line);
    }
}
