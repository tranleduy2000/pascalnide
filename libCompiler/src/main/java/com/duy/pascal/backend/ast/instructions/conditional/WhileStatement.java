package com.duy.pascal.backend.ast.instructions.conditional;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.instructions.NopeInstruction;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectDoTokenException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.BasicToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.types.BasicType;

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
            throw new UnConvertibleTypeException(condition, BasicType.Boolean,
                    condition.getType(context).declType, context);
        }

        //check "do' token
        Token next;
        next = grouperToken.take();
        if (!(next instanceof DoToken)) {
            if (next instanceof BasicToken) {
                throw new ExpectedTokenException("do", next);
            } else {
                throw new ExpectDoTokenException(next.getLineNumber());
            }
        }

        //get command
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
                                       RuntimeExecutableCodeUnit<?> main, String contextName) throws RuntimePascalException {
        while_loop:
        while ((Boolean) condition.getValue(context, main)) {
            switch (command.execute(context, main, contextName)) {
                case CONTINUE:
                    continue while_loop;
                case BREAK:
                    break while_loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public String toString() {
        return "while (" + condition + ") do " + command;
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
                return new NopeInstruction(line);
            } else {
                return new WhileStatement(new ConstantAccess<>(true, condition.getLineNumber()),
                        comm, line);
            }
        }
        return new WhileStatement(condition, comm, line);
    }
}
