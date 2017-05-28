package com.js.interpreter.instructions.conditional;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.UntilToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.CompileTimeContext;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExecutionResult;
import com.js.interpreter.instructions.InstructionGrouper;

public class RepeatInstruction extends DebuggableExecutable {
    Executable command;

    RuntimeValue condition;
    LineInfo line;

    public RepeatInstruction(ExpressionContext f, GrouperToken grouperToken, LineInfo lineInfo)
            throws ParsingException {
        Token next = null;
        InstructionGrouper command = new InstructionGrouper(lineInfo);

        while (!(grouperToken.peekNoEOF() instanceof UntilToken)) {
            command.addCommand(grouperToken.getNextCommand(f));
            if (!(grouperToken.peekNoEOF() instanceof UntilToken)) {
                grouperToken.assertNextSemicolon(next);
            }
        }
        next = grouperToken.take();
        if (!(next instanceof UntilToken)) {
            throw new ExpectedTokenException("until", next);
        }
        RuntimeValue condition = grouperToken.getNextExpression(f);

        RuntimeValue convert = BasicType.Boolean.convert(condition, f);
        if (convert == null) {
            throw new UnConvertibleTypeException(condition, BasicType.Boolean,
                    condition.getType(f).declType, condition, f);
        }

        this.command = command;
        this.condition = condition;
        this.line = lineInfo;
    }
    public RepeatInstruction(Executable command, RuntimeValue condition,
                             LineInfo line) {
        this.command = command;
        this.condition = condition;
        this.line = line;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        do_loop:
        do {
            switch (command.execute(context, main)) {
                case CONTINUE:
                    continue do_loop;
                case BREAK:
                    break do_loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        } while (!((Boolean) condition.getValue(context, main)));
        return ExecutionResult.NONE;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Object o = condition.compileTimeValue(c);
        if (o != null) {
            Boolean b = (Boolean) o;
            if (!b) {
                return command.compileTimeConstantTransform(c);
            } else {
                return new RepeatInstruction(
                        command.compileTimeConstantTransform(c),
                        new ConstantAccess(true, condition.getLineNumber()), line);
            }

        }
        return new RepeatInstruction(command.compileTimeConstantTransform(c),
                condition, line);
    }
}
