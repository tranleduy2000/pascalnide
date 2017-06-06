package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

import java.util.LinkedList;
import java.util.List;

public class InstructionGrouper extends DebuggableExecutable {
    List<Executable> instructions;
    LineInfo line;

    public InstructionGrouper(LineInfo line) {
        this.line = line;
        instructions = new LinkedList<>();
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
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
        return ExecutionResult.NONE;
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
    public Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException {
        InstructionGrouper nig = new InstructionGrouper(line);
        for (Executable e : instructions) {
            Executable transformed = e.compileTimeConstantTransform(c);
            if (transformed == null) {
                nig.instructions.add(e);
            } else if (transformed instanceof NoneInstruction) {
            } else {
                nig.instructions.add(transformed);
            }
        }
        if (nig.instructions.size() == 0) {
            return new NoneInstruction(line);
        } else {
            return nig;
        }
    }
}
