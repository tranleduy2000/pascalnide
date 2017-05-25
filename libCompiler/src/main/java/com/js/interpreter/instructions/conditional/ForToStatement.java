package com.js.interpreter.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Assignment;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.instructions.SetValueExecutable;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.AssignableValue;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.operators.BinaryOperatorEval;
import com.duy.pascal.backend.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;


public class ForToStatement extends DebuggableExecutable {
    private SetValueExecutable setfirst;
    private RuntimeValue lessThanLast;
    private SetValueExecutable increment_temp;
    private Executable command;
    private LineInfo line;

    public ForToStatement(ExpressionContext context, AssignableValue tempVar,
                          RuntimeValue first, RuntimeValue last, Executable command,
                          LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new Assignment(tempVar, first, line);
        lessThanLast = BinaryOperatorEval.generateOp(context, tempVar, last,
                OperatorTypes.LESSEQ, this.line);
        increment_temp = new Assignment(tempVar, BinaryOperatorEval.generateOp(
                context, tempVar, new ConstantAccess(1, this.line),
                OperatorTypes.PLUS, this.line), line);

        this.command = command;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context, RuntimeExecutableCodeUnit<?> main)
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
        RuntimeValue comp = lessThanLast;
        Object val = lessThanLast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            } else {
                comp = new ConstantAccess(val, lessThanLast.getLineNumber());
            }
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
