package com.duy.pascal.backend.ast.instructions;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.references.Reference;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.debug.DebugManager;

public class AssignStatement extends DebuggableExecutable implements SetValueExecutable {
    private AssignableValue left;
    private RuntimeValue value;
    private LineInfo line;

    public AssignStatement(@NonNull AssignableValue left, @NonNull RuntimeValue value, @NonNull LineInfo line) {
        this.left = left;
        this.value = value;
        this.line = line;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        DebugManager.debugAssign(line, left, value, main);

        Reference ref = left.getReference(context, main);
        Object v = this.value.getValue(context, main);
        ref.set(v);
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
        return new AssignStatement(left, value.compileTimeExpressionFold(c), line);
    }
}
