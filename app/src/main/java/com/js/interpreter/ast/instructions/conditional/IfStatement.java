package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class IfStatement extends DebuggableExecutable {
    ReturnsValue condition;

    Executable instruction;

    Executable else_instruction;
    LineInfo line;

    public IfStatement(ReturnsValue condition, Executable instruction,
                       Executable else_instruction, LineInfo line) {
        this.condition = condition;
        this.instruction = instruction;
        this.else_instruction = else_instruction;
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        if ((Boolean) (condition.getValue(f, main))) {
            return instruction.execute(f, main);
        } else {
            if (else_instruction != null) {
                return else_instruction.execute(f, main);
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
                return else_instruction.compileTimeConstantTransform(c);
            }
        } else {
            return new IfStatement(condition,
                    instruction.compileTimeConstantTransform(c),
                    else_instruction.compileTimeConstantTransform(c), line);
        }
    }
}
