package com.duy.pascal.interperter.ast.instructions.conditional;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.CompoundStatement;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.debugable.DebuggableExecutable;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.UntilToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;

public class RepeatInstruction extends DebuggableExecutable {
    Executable command;

    RuntimeValue condition;
    LineInfo line;

    public RepeatInstruction(ExpressionContext f, GrouperToken grouperToken, LineInfo lineInfo)
            throws ParsingException {
        Token next = null;
        CompoundStatement command = new CompoundStatement(lineInfo);

        while (!(grouperToken.peekNoEOF() instanceof UntilToken)) {
            command.addCommand(grouperToken.getNextCommand(f));
            if (!(grouperToken.peekNoEOF() instanceof UntilToken)) {
                grouperToken.assertNextSemicolon();
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
                    condition.getRuntimeType(f).declType, f);
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
        return ExecutionResult.NOPE;
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
                        new ConstantAccess<>(true, condition.getLineNumber()), line);
            }

        }
        return new RepeatInstruction(command.compileTimeConstantTransform(c),
                condition, line);
    }
}
