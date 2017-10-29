package com.duy.pascal.interperter.ast.instructions.conditional;

import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.runtime.value.RuntimeValue;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectThenTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.BasicToken;
import com.duy.pascal.interperter.tokens.basic.ElseToken;
import com.duy.pascal.interperter.tokens.basic.ThenToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

public class IfStatement extends DebuggableNode {
    private RuntimeValue mCondition;
    private Node mStatement;
    private Node mElseStatement;
    private LineInfo line;

    public IfStatement(RuntimeValue condition, Node statement,
                       @Nullable Node elseStatement, LineInfo line) {


        this.mCondition = condition;
        this.mStatement = statement;
        this.mElseStatement = elseStatement;
        this.line = line;
    }

    /**
     * Declaration if statement
     * <p>
     * if <condition> then <command>
     * <p>
     * if <condition> then <command> else <commnad>
     *
     * @param lineNumber - the lineInfo of begin if token
     */
    public IfStatement(ExpressionContext context, GrouperToken grouperToken, LineInfo lineNumber) throws Exception {

        //check condition is boolean value
        RuntimeValue condition = grouperToken.getNextExpression(context);
        RuntimeValue convert = BasicType.Boolean.convert(condition, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(condition, BasicType.Boolean,
                    condition.getRuntimeType(context).declType, context);
        }

        //check then token
        Token next = grouperToken.take();
        if (!(next instanceof ThenToken)) {
            if (next instanceof BasicToken) {
                throw new ExpectedTokenException("then", next);
            } else {
                throw new ExpectThenTokenException(next.getLineNumber());
            }
        }

        //get command after then token
        Node command = grouperToken.getNextCommand(context);

        //if it in include else command
        Node elseCommand = null;
        next = grouperToken.peek();
        if (next instanceof ElseToken) {
            grouperToken.take();
            elseCommand = grouperToken.getNextCommand(context);
        }

        this.mCondition = condition;
        this.mStatement = command;
        this.mElseStatement = elseCommand;
        this.line = lineNumber;
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Boolean value = (Boolean) (mCondition.getValue(context, main));
        if (value) {
            return mStatement.visit(context, main);
        } else {
            if (mElseStatement != null) {
                return mElseStatement.visit(context, main);
            }
            return ExecutionResult.NOPE;
        }
    }

    @Override
    public String toString() {
        return "if [" + mCondition.toString() + "] then [\n" + mStatement + ']';
    }

    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
        Object o = mCondition.compileTimeValue(c);
        if (o != null) {
            Boolean b = (Boolean) o;
            if (b) {
                return mStatement.compileTimeConstantTransform(c);
            } else {
                return mElseStatement.compileTimeConstantTransform(c);
            }
        } else {
            return new IfStatement(mCondition,
                    mStatement.compileTimeConstantTransform(c),
                    mElseStatement.compileTimeConstantTransform(c), line);
        }
    }
}
