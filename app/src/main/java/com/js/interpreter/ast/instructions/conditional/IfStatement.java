package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class IfStatement extends DebuggableExecutable {
    private ReturnsValue condition;
    private Executable instruction;
    private Executable elseInstruction;
    private LineInfo line;

    public IfStatement(ReturnsValue condition, Executable instruction,
                       Executable elseInstruction, LineInfo line) {
        this.condition = condition;
        this.instruction = instruction;
        this.elseInstruction = elseInstruction;
        this.line = line;
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        Boolean value = (Boolean) (condition.getValue(f, main));
        if (value) {
            return instruction.execute(f, main);
        } else {
            if (elseInstruction != null) {
                return elseInstruction.execute(f, main);
            }
            return ExecutionResult.NONE;
        }
    }

    @Override
    public String toString() {
        return "if [" + condition.toString() + "] then [\n" + instruction + ']';
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Object o = condition.compileTimeValue(c);
        if (o != null) {
            Boolean b = (Boolean) o;
            if (b) {
                return instruction.compileTimeConstantTransform(c);
            } else {
                return elseInstruction.compileTimeConstantTransform(c);
            }
        } else {
            return new IfStatement(condition,
                    instruction.compileTimeConstantTransform(c),
                    elseInstruction.compileTimeConstantTransform(c), line);
        }
    }
}
