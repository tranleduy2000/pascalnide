package com.duy.pascal.interperter.ast.instructions.case_statement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.CompoundStatement;
import com.duy.pascal.interperter.ast.instructions.Node;
import com.duy.pascal.interperter.ast.instructions.ExecutionResult;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.debugable.DebuggableNode;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonConstantExpressionException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.tokens.EOFToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.DotDotToken;
import com.duy.pascal.interperter.tokens.basic.ElseToken;
import com.duy.pascal.interperter.tokens.basic.OfToken;
import com.duy.pascal.interperter.tokens.grouping.CaseToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.declaration.lang.types.Type;

import java.util.ArrayList;
import java.util.List;

public class CaseInstruction extends DebuggableNode {
    private RuntimeValue mSwitchValue;
    private CasePossibility[] possibilities;
    private CompoundStatement otherwise;
    private LineInfo line;

    public CaseInstruction(CaseToken token, ExpressionContext context)
            throws Exception {
        this.line = token.getLineNumber();
        mSwitchValue = token.getNextExpression(context);

        Token next = token.take();
        if (!(next instanceof OfToken)) {
            throw new ExpectedTokenException("of", next);
        }

        //this Object used to check compare type with another element
        Type switchValueType = mSwitchValue.getRuntimeType(context).declType;
        List<CasePossibility> possibilities = new ArrayList<>();

        while (!(token.peek() instanceof ElseToken) && !(token.peek() instanceof EOFToken)) {
            List<CaseCondition> conditions = new ArrayList<>();
            while (true) {
                RuntimeValue valueToSwitch = token.getNextExpression(context);

                //check type
                assertType(switchValueType, valueToSwitch, context);

                Object v = valueToSwitch.compileTimeValue(context);
                if (v == null) {
                    throw new NonConstantExpressionException(valueToSwitch);
                }
                if (token.peek() instanceof DotDotToken) {
                    token.take();
                    RuntimeValue upper = token.getNextExpression(context);
                    Object hi = upper.compileTimeValue(context);
                    if (hi == null) {
                        throw new NonConstantExpressionException(upper);
                    }
                    conditions.add(new RangeValue(context, mSwitchValue, v, hi, valueToSwitch.getLineNumber()));
                } else {
                    conditions.add(new SingleValue(v, valueToSwitch.getLineNumber()));
                }
                if (token.peek() instanceof CommaToken) {
                    token.take();
                } else if (token.peek() instanceof ColonToken) {
                    token.take();
                    break;
                } else {
                    throw new ExpectedTokenException("[comma (,) or colon (:)]", token.take());
                }
            }
            Node command = token.getNextCommand(context);
            assertNextSemicolon(token);
            possibilities.add(new CasePossibility(conditions.toArray(new CaseCondition[conditions.size()]), command));
        }

        otherwise = new CompoundStatement(token.peek().getLineNumber());
        if (token.peek() instanceof ElseToken) {
            token.take();
            while (token.hasNext()) {
                otherwise.addCommand(token.getNextCommand(context));
                token.assertNextSemicolon();
            }
        }
        this.possibilities = possibilities.toArray(new CasePossibility[possibilities.size()]);
    }

    //check type
    private void assertType(Type switchValueType, RuntimeValue val,
                            ExpressionContext context) throws Exception {
        Type valueType = val.getRuntimeType(context).declType;
        RuntimeValue converted = switchValueType.convert(val, context);
        if (converted == null) {
            throw new UnConvertibleTypeException(val, switchValueType, valueType, mSwitchValue, context);
        }

    } // end check type

    /**
     * check semicolon symbol
     */
    private void assertNextSemicolon(GrouperToken grouperToken) throws Exception {
        if (grouperToken.peek() instanceof ElseToken) return;
        grouperToken.assertNextSemicolon();
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object value = mSwitchValue.getValue(context, main);
        for (CasePossibility possibility : possibilities) {
            for (int j = 0; j < possibility.conditions.length; j++) {
                if (possibility.conditions[j].fits(context, main, value)) {
                    return possibility.visit(context, main);
                }
            }
        }
        return otherwise.visit(context, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


    @Override
    public Node compileTimeConstantTransform(CompileTimeContext c)
            throws Exception {
      /*  Object value = mSwitchValue.compileTimeValue(c);
        if (value == null) {
            return this;
        }
        try {
            for (CasePossibility possibily : possibilities) {
                for (int j = 0; j < possibily.conditions.length; j++) {
                    if (possibily.conditions[j].fits(null, null, value)) {
                        return possibily;
                    }
                }
            }
            return otherwise;
        } catch (RuntimePascalException e) {
            throw new ConstantCalculationException(e);
        }*/
        return null;
    }
}
