package com.duy.pascal.interperter.ast.node.conditional;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.CompoundNode;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.UntilToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public class RepeatNode extends DebuggableNode {
    @NonNull
    private LineNumber mLine;
    @NonNull
    private Node mCommand;
    @NonNull
    private RuntimeValue mCondition;

    public RepeatNode(ExpressionContext f, GrouperToken grouperToken, @NonNull LineNumber lineNumber)
            throws Exception {
        Token next = null;
        CompoundNode command = new CompoundNode(lineNumber);

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

        this.mCommand = command;
        this.mCondition = condition;
        this.mLine = lineNumber;
    }

    public RepeatNode(@NonNull Node command, @NonNull RuntimeValue condition, @NonNull LineNumber line) {
        this.mCommand = command;
        this.mCondition = condition;
        this.mLine = line;
    }

    @Override
    public LineNumber getLineNumber() {
        return mLine;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        do_loop:
        do {
            switch (mCommand.visit(context, main)) {
                case CONTINUE:
                    continue do_loop;
                case BREAK:
                    break do_loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        } while (!((Boolean) mCondition.getValue(context, main)));
        return ExecutionResult.NOPE;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        Object value = mCondition.compileTimeValue(c);
        if (value != null) {
            Boolean condition = (Boolean) value;
            if (!condition) {
                return mCommand.compileTimeConstantTransform(c);
            } else {
                return new RepeatNode(mCommand.compileTimeConstantTransform(c),
                        new ConstantAccess<>(true, mCondition.getLineNumber()), mLine);
            }

        }
        return new RepeatNode(mCommand.compileTimeConstantTransform(c), mCondition, mLine);
    }
}
