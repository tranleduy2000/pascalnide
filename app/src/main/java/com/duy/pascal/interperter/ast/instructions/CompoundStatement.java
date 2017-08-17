package com.duy.pascal.interperter.ast.instructions;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.util.LinkedList;

public class CompoundStatement extends DebuggableExecutable {
    private LinkedList<Executable> instructions;
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

    public void addCommand(Executable e) {
        instructions.add(e);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        for (Executable e : instructions) {
            switch (e.execute(context, main)) {
                case BREAK:
                    return ExecutionResult.BREAK;
                case EXIT:
                    return ExecutionResult.EXIT;
                case CONTINUE:
                    return ExecutionResult.CONTINUE;
            }
        }
        new NopeInstruction(endLine).execute(context, main);
        return ExecutionResult.NOPE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("begin\n");
        for (Executable e : instructions) {
            builder.append(e).append("\n");
        }
        builder.append("end\n");
        return builder.toString();
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c) throws Exception {
        CompoundStatement nig = new CompoundStatement(startLine);
        for (Executable e : instructions) {
            Executable transformed = e.compileTimeConstantTransform(c);
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

    public LinkedList<Executable> getInstructions() {
        return instructions;
    }
}
