package com.duy.pascal.interperter.ast.node;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.util.LinkedList;

public class CompoundStatement extends DebuggableNode {
    private LinkedList<Node> instructions;
    private LineInfo startLine, endLine;

    public CompoundStatement(LineInfo startLine) {
        this.startLine = startLine;
        instructions = new LinkedList<>();
    }

    public void setEndLine(LineInfo endLine) {
        this.endLine = endLine;
    }

    @Override
    public LineInfo getLineNumber() {
        return startLine;
    }

    public void addCommand(Node e) {
        instructions.add(e);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        for (Node e : instructions) {
            switch (e.visit(context, main)) {
                case BREAK:
                    return ExecutionResult.BREAK;
                case EXIT:
                    return ExecutionResult.EXIT;
                case CONTINUE:
                    return ExecutionResult.CONTINUE;
            }
        }
        new NopeInstruction(endLine).visit(context, main);
        return ExecutionResult.NOPE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("begin\n");
        for (Node e : instructions) {
            builder.append(e).append("\n");
        }
        builder.append("end\n");
        return builder.toString();
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c) throws Exception {
        CompoundStatement nig = new CompoundStatement(startLine);
        for (Node e : instructions) {
            Node transformed = e.compileTimeConstantTransform(c);
            if (transformed == null) {
                nig.addCommand(e);
            } else if (transformed instanceof NopeInstruction) {
            } else {
                nig.addCommand(transformed);
            }
        }
        if (nig.getInstructions().size() == 0) {
            return new NopeInstruction(startLine);
        } else {
            return nig;
        }
    }

    public LinkedList<Node> getInstructions() {
        return instructions;
    }
}
