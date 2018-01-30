package com.duy.pascal.interperter.ast.node;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public class BreakNode extends DebuggableNode {
    private LineNumber line;

    public BreakNode(LineNumber line) {
        this.line = line;
    }

    @Override
    public LineNumber getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException {
        return ExecutionResult.BREAK;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c) {
        return this;
    }

}
