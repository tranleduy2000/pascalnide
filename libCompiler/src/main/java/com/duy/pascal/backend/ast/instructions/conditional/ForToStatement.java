package com.duy.pascal.backend.ast.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
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

/**
 * For to do loop
 * <p>
 * see in https://www.freepascal.org/docs-html/ref/refsu58.html#x164-18600013.2.4
 */
public class ForToStatement extends DebuggableExecutable {
    private AssignExecutable setfirst;
    private RuntimeValue lessThanLast;
    private AssignExecutable increment;
    private Executable command;
    private LineInfo line;

    public ForToStatement(ExpressionContext context, AssignableValue tempVar,
                          RuntimeValue first, RuntimeValue last, Executable command,
                          LineInfo line) throws ParsingException {
        this.line = line;
        setfirst = new AssignStatement(tempVar, first, line);
        lessThanLast = BinaryOperatorEval.generateOp(context, tempVar, last,
                OperatorTypes.LESSEQ, this.line);
        increment = new AssignStatement(tempVar, BinaryOperatorEval.generateOp(
                context, tempVar, new ConstantAccess<>(1, this.line),
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
        RuntimeValue comp = lessThanLast;
        Object val = lessThanLast.compileTimeValue(c);
        if (val != null) {
            if (((Boolean) val)) {
                return first;
            } else {
                comp = new ConstantAccess<>(val, lessThanLast.getLineNumber());
            }
        }
        return new ForDowntoStatement(first, comp, inc, comm, line);
    }
}
