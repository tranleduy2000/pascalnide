package com.js.interpreter.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Assignment;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.SetValueExecutable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.AssignableValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.operators.number.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class ForDowntoStatement extends DebuggableExecutable {
    private SetValueExecutable setfirst;
    private ReturnValue lessthanlast;
    private SetValueExecutable increment_temp;
    private Executable command;
    private LineInfo line;

    public ForDowntoStatement(ExpressionContext f, AssignableValue temp_var,
                              ReturnValue first, ReturnValue last, Executable command,
                              LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new Assignment(temp_var, first, line);
        lessthanlast = BinaryOperatorEvaluation.generateOp(f, temp_var, last,
                OperatorTypes.GREATEREQ, this.line);
        increment_temp = new Assignment(temp_var, BinaryOperatorEvaluation.generateOp(
                f, temp_var, new ConstantAccess(1, this.line),
                OperatorTypes.MINUS, this.line), line);

        this.command = command;
    }

    public ForDowntoStatement(SetValueExecutable setfirst,
                              ReturnValue lessthanlast, SetValueExecutable increment_temp,
                              Executable command, LineInfo line) {
        super();
        this.setfirst = setfirst;
        this.lessthanlast = lessthanlast;
        this.increment_temp = increment_temp;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        setfirst.execute(context, main);
        while_loop:
        while ((Boolean) lessthanlast.getValue(context, main)) {
            switch (command.execute(context, main)) {
                case EXIT:
                    return ExecutionResult.EXIT;
                case BREAK:
                    break while_loop;
                case CONTINUE:
                    continue while_loop;
            }
            increment_temp.execute(context, main);
        }
        return ExecutionResult.NONE;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        SetValueExecutable first = setfirst.compileTimeConstantTransform(c);
        SetValueExecutable inc = increment_temp.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        ReturnValue comp = lessthanlast;
        Object val = lessthanlast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            }
            comp = new ConstantAccess(val, lessthanlast.getLineNumber());
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
