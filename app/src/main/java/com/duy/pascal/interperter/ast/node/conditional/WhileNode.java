package com.duy.pascal.interperter.ast.node.conditional;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.node.ExecutionResult;
import com.duy.pascal.interperter.ast.node.Node;
import com.duy.pascal.interperter.ast.node.NopeInstruction;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectDoTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.WrongStatementException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.BasicToken;
import com.duy.pascal.interperter.tokens.basic.DoToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public class WhileNode extends DebuggableNode {
    @NonNull
    private RuntimeValue mCondition;
    @NonNull
    private Node mCommand;
    @NonNull
    private LineInfo mLine;

    /**
     * constructor
     * Declare while statement
     * <p>
     * while <condition> do <command>
     * <p>
     */
    public WhileNode(ExpressionContext context, GrouperToken grouperToken, @NonNull LineInfo line)
            throws Exception {

        //check condition return boolean type
        RuntimeValue condition = grouperToken.getNextExpression(context);
        RuntimeValue convert = BasicType.Boolean.convert(condition, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(condition, BasicType.Boolean,
                    condition.getRuntimeType(context).declType, context);
        }

        //check "do' token
        Token next;
        next = grouperToken.take();
        if (!(next instanceof DoToken)) {
            if (next instanceof BasicToken) {
                throw new ExpectedTokenException("do", next);
            } else {
                throw new ExpectDoTokenException(next.getLineNumber(), WrongStatementException.Statement.WHILE_DO);
            }
        }

        //get command
        Node command = grouperToken.getNextCommand(context);

        this.mCondition = condition;
        this.mCommand = command;
        this.mLine = line;
    }

    public WhileNode(@NonNull RuntimeValue condition, @NonNull Node command, @NonNull LineInfo line) {
        this.mCondition = condition;
        this.mCommand = command;
        this.mLine = line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        loop:
        while ((Boolean) mCondition.getValue(context, main)) {
            switch (mCommand.visit(context, main)) {
                case CONTINUE:
                    continue loop;
                case BREAK:
                    break loop;
                case EXIT:
                    return ExecutionResult.EXIT;
            }
        }
        return ExecutionResult.NOPE;
    }

    @Override
    public String toString() {
        return "while (" + mCondition + ") do " + mCommand;
    }

    @Override
    public LineInfo getLineNumber() {
        return mLine;
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        Node cmdValue = mCommand.compileTimeConstantTransform(c);
        Object cond = mCondition.compileTimeValue(c);
        if (cond != null) {
            if (!((Boolean) cond)) {
                return new NopeInstruction(mLine);
            } else {
                return new WhileNode(new ConstantAccess<>(true, mCondition.getLineNumber()),
                        cmdValue, mLine);
            }
        }
        return null;
    }
}
