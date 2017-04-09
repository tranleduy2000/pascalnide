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

public class DowntoForStatement extends DebuggableExecutable {
    private SetValueExecutable setfirst;
    private ReturnsValue lessthanlast;
    private SetValueExecutable incrementTemp;
    private Executable command;
    private LineInfo line;

    public DowntoForStatement(ExpressionContext main,
                              ReturnsValue tempVariable,
                              ReturnsValue firstValue, ReturnsValue lastValue, Executable command,
                              LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = tempVariable.createSetValueInstruction(firstValue);
        lessthanlast = BinaryOperatorEvaluation.generateOp(main, tempVariable, lastValue,
                OperatorTypes.GREATEREQ, this.line);

        incrementTemp = tempVariable
                .createSetValueInstruction(BinaryOperatorEvaluation.generateOp(
                        main, tempVariable, new ConstantAccess(1, this.line),
                        OperatorTypes.MINUS, this.line));

        this.command = command;
    }

    public DowntoForStatement(SetValueExecutable setfirst,
                              ReturnsValue lessthanlast, SetValueExecutable incrementTemp,
                              Executable command, LineInfo line) {
        super();
        this.setfirst = setfirst;
        this.lessthanlast = lessthanlast;
        this.incrementTemp = incrementTemp;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        setfirst.execute(f, main);
        while_loop:
        while ((Boolean) lessthanlast.getValue(f, main)) {
            switch (command.execute(f, main)) {
                case EXIT:
                    return ExecutionResult.EXIT;
                case BREAK:
                    break while_loop;
            }
            incrementTemp.execute(f, main);
        }
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getline() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        SetValueExecutable first = setfirst.compileTimeConstantTransform(c);
        SetValueExecutable inc = incrementTemp.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        ReturnsValue comp = lessthanlast;
        Object val = lessthanlast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            }
            comp = new ConstantAccess(val, lessthanlast.getline());
        }
        return new DowntoForStatement(first, comp, inc, comm, line);
    }
}
