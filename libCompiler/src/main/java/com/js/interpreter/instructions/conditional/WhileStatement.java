package com.js.interpreter.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.debug.DebugManager;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.instructions.NoneInstruction;
import com.js.interpreter.runtime_value.ConstantAccess;
import com.js.interpreter.runtime_value.RuntimeValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.Token;

public class WhileStatement extends DebuggableExecutable {
    private RuntimeValue condition;
    private Executable command;
    private LineInfo line;

    /**
     * constructor
     * Declare while statement
     * <p>
     * while <condition> do <command>
     * <p>
     */
    public WhileStatement(ExpressionContext context, GrouperToken grouperToken, LineInfo lineNumber)
            throws ParsingException {

        //check condition return boolean type
        RuntimeValue condition = grouperToken.getNextExpression(context);
        RuntimeValue convert = BasicType.Boolean.convert(condition, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(condition, BasicType.Boolean, condition.getType(context).declType, true);
        }

        //check "do' token
        Token next;
        next = grouperToken.take();
        if (!(next instanceof DoToken)) {
            throw new ExpectedTokenException("do", next);
        }

        //indexOf command
        Executable command = grouperToken.getNextCommand(context);

        this.condition = condition;
        this.command = command;
        this.line = lineNumber;
    }

    public WhileStatement(RuntimeValue condition, Executable command,
                          LineInfo line) {
        this.condition = condition;
        this.command = command;
        this.line = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        while_loop:
        while ((Boolean) condition.getValue(context, main)) {
            DebugManager.outputConditionWhile(main.getDebugListener(), true);
            switch (command.execute(context, main)) {
                case CONTINUE:
                    continue while_loop;
                case BREAK:
                    break while_loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        }
        return ExecutionResult.NONE;
    }

    @Override
    public String toString() {
        return "while [" + condition + "] do [" + command + ']';
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Executable comm = command.compileTimeConstantTransform(c);
        Object cond = condition.compileTimeValue(c);
        if (cond != null) {
            if (!((Boolean) cond)) {
                return new NoneInstruction(line);
            } else {
                return new WhileStatement(new ConstantAccess(true,
                        condition.getLineNumber()), comm, line);
            }
        }
        return new WhileStatement(condition, comm, line);
    }
}
