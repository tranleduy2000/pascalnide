package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import static com.js.interpreter.ast.instructions.ExecutionResult.NONE;


public class ForStatement extends DebuggableExecutable {
    private SetValueExecutable setFirst;
    private ReturnsValue lessThanLast;
    private SetValueExecutable incrementTemp;
    private Executable command;
    private LineInfo line;

    public ForStatement(ExpressionContext f, ReturnsValue tempVariable,
                        ReturnsValue first, ReturnsValue last, Executable command,
                        LineInfo line) throws ParsingException {
        this.line = line;
        setFirst = tempVariable.createSetValueInstruction(first);
        lessThanLast = BinaryOperatorEvaluation.generateOp(f, tempVariable, last,
                OperatorTypes.LESSEQ, this.line);
        incrementTemp = tempVariable.createSetValueInstruction(
                BinaryOperatorEvaluation.generateOp(f, tempVariable, new ConstantAccess(1, this.line),
                        OperatorTypes.PLUS, this.line));
        this.command = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        setFirst.execute(f, main);
        while_loop:
        while ((Boolean) lessThanLast.getValue(f, main)) {
            ExecutionResult result = command.execute(f, main);
            switch (result) {
                case EXIT:
                    return ExecutionResult.EXIT;
                case BREAK:
                    break while_loop;
            }
            incrementTemp.execute(f, main);
        }
//        DebugManager.outputConditionFor(main.getDebugListener(), true);
        return NONE;
    }

    @Override
    public LineInfo getLine() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        SetValueExecutable first = setFirst.compileTimeConstantTransform(c);
        SetValueExecutable inc = incrementTemp.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        ReturnsValue comp = lessThanLast;
        Object val = lessThanLast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            } else {
                comp = new ConstantAccess(val, lessThanLast.getLine());
            }
        }
        return new DowntoForStatement(first, comp, inc, comm, line);
    }
}
