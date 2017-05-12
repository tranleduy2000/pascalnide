package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.LeftValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.Reference;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class Assignment extends DebuggableExecutable implements SetValueExecutable {
    private LeftValue left;

    private ReturnValue value;
    private LineInfo line;

    public Assignment(LeftValue left, ReturnValue value, LineInfo line) {
        this.left = left;
        this.value = value;
        this.line = line;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
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
    public void setAssignedValue(ReturnValue value) {
        this.value = value;
    }

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new Assignment(left, value.compileTimeExpressionFold(c), line);
    }
}
