package com.duy.pascal.backend.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.OperatorTypes;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.assign_statement.AssignStatement;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.instructions.assign_statement.AssignExecutable;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class ForDowntoStatement extends DebuggableExecutable {
    private AssignExecutable setfirst;
    private RuntimeValue lessthanlast;
    private AssignExecutable increment;
    private Executable command;
    private LineInfo line;

    public ForDowntoStatement(ExpressionContext f, AssignableValue assignableVar,
                              RuntimeValue first, RuntimeValue last, Executable command,
                              LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new AssignStatement(assignableVar, first, line);

        lessthanlast = BinaryOperatorEval.generateOp(f, assignableVar, last, OperatorTypes.GREATEREQ, this.line);

        increment = new AssignStatement(assignableVar,
                BinaryOperatorEval.generateOp(f, assignableVar, new ConstantAccess<>(1, BasicType.Integer,
                        this.line), OperatorTypes.MINUS, line), line);

        this.command = command;
    }

    public ForDowntoStatement(AssignExecutable setfirst,
                              RuntimeValue lessthanlast, AssignExecutable increment,
                              Executable command, LineInfo line) {
        super();
        this.setfirst = setfirst;
        this.lessthanlast = lessthanlast;
        this.increment = increment;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
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
            increment.execute(context, main);
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
        AssignExecutable first = setfirst.compileTimeConstantTransform(c);
        AssignExecutable inc = increment.compileTimeConstantTransform(c);
        Executable comm = command.compileTimeConstantTransform(c);
        RuntimeValue comp = lessthanlast;
        Object val = lessthanlast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            }
            comp = new ConstantAccess<>(val, lessthanlast.getLineNumber());
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
