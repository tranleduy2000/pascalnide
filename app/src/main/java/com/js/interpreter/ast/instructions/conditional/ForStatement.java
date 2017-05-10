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
import com.js.interpreter.ast.returnsvalue.LeftValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;


public class ForStatement extends DebuggableExecutable {
    private SetValueExecutable setfirst;
    private ReturnValue lessThanLast;
    private SetValueExecutable increment_temp;
    private Executable command;
    private LineInfo line;

    public ForStatement(ExpressionContext context, LeftValue tempVar,
                        ReturnValue first, ReturnValue last, Executable command,
                        LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new Assignment(tempVar, first, line);
        lessThanLast = BinaryOperatorEvaluation.generateOp(context, tempVar, last,
                OperatorTypes.LESSEQ, this.line);
        increment_temp = new Assignment(tempVar, BinaryOperatorEvaluation.generateOp(
                context, tempVar, new ConstantAccess(1, this.line),
                OperatorTypes.PLUS, this.line), line);

        this.command = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        setfirst.execute(context, main);
        whileLoop:
        while ((Boolean) lessThanLast.getValue(context, main)) {
            ExecutionResult result = command.execute(context, main);
            switch (result) {
                case EXIT:
                    return ExecutionResult.EXIT;
                case BREAK:
                    break whileLoop;
                case CONTINUE:

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
        ReturnValue comp = lessThanLast;
        Object val = lessThanLast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            } else {
                comp = new ConstantAccess(val, lessThanLast.getLineNumber());
            }
        }
        return new DowntoForStatement(first, comp, inc, comm, line);
    }
}
