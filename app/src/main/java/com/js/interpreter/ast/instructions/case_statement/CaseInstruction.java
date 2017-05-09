package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnConvertibleTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.List;

public class CaseInstruction extends DebuggableExecutable {
    private ReturnValue mSwitchValue;
    private CasePossibility[] possibilies;
    private InstructionGrouper otherwise;
    private LineInfo line;

    public CaseInstruction(CaseToken token, ExpressionContext context)
            throws ParsingException {
        this.line = token.lineInfo;
//        mSwitchValue = new CachedReturnValue(token.getNextExpression(context));
        mSwitchValue = token.getNextExpression(context);
        Token next = token.take();
        if (!(next instanceof OfToken)) {
            throw new ExpectedTokenException("of", next);
        }

        //this Object used to check compare type with another element
        DeclaredType switchValueType = mSwitchValue.getType(context).declType;
        List<CasePossibility> possibilities = new ArrayList<>();

        while (!(token.peek() instanceof ElseToken) && !(token.peek() instanceof EOFToken)) {
            List<CaseCondition> conditions = new ArrayList<>();
            while (true) {
                ReturnValue valueToSwitch = token.getNextExpression(context);

                //check type
                assertType(switchValueType, valueToSwitch, context);

                Object v = valueToSwitch.compileTimeValue(context);
                if (v == null) {
                    throw new NonConstantExpressionException(valueToSwitch);
                }
                if (token.peek() instanceof DotDotToken) {
                    token.take();
                    ReturnValue upper = token.getNextExpression(context);
                    Object hi = upper.compileTimeValue(context);
                    if (hi == null) {
                        throw new NonConstantExpressionException(upper);
                    }
                    conditions.add(new RangeOfValues(context, mSwitchValue, v, hi, valueToSwitch.getLineNumber()));
                } else {
                    conditions.add(new SingleValue(v, valueToSwitch.getLineNumber()));
                }
                if (token.peek() instanceof CommaToken) {
                    token.take();
                } else if (token.peek() instanceof ColonToken) {
                    token.take();
                    break;
                } else {
                    throw new ExpectedTokenException("[comma or colon]", token.take());
                }
            }
            Executable command = token.getNextCommand(context);
            assertNextSemicolon(token);
            possibilities.add(new CasePossibility(conditions.toArray(new CaseCondition[conditions.size()]), command));
        }

        otherwise = new InstructionGrouper(token.peek().lineInfo);
        if (token.peek() instanceof ElseToken) {
            token.take();
            while (token.hasNext()) {
                otherwise.add_command(token.getNextCommand(context));
                token.assertNextSemicolon(token);
            }
        }
        this.possibilies = possibilities.toArray(new CasePossibility[possibilities.size()]);
    }

    //check type
    private void assertType(DeclaredType switchValueType, ReturnValue val, ExpressionContext context) throws ParsingException {
        DeclaredType inputType = val.getType(context).declType;
        ReturnValue converted = switchValueType.convert(val, context);
        if (converted == null) {
            throw new UnConvertibleTypeException(val, inputType, switchValueType, true);
        }

    } // end check type

    /**
     * check semicolon symbol
     */
    private void assertNextSemicolon(GrouperToken grouperToken) throws ParsingException {
        if (grouperToken.peek() instanceof ElseToken) return;
        grouperToken.assertNextSemicolon(grouperToken);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext context,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        Object value = mSwitchValue.getValue(context, main);
        for (CasePossibility possibily : possibilies) {
            for (int j = 0; j < possibily.conditions.length; j++) {
                if (possibily.conditions[j].fits(value)) {
                    return possibily.execute(context, main);
                }
            }
        }
        return otherwise.execute(context, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Object value = mSwitchValue.compileTimeValue(c);
        if (value == null) {
            return this;
        }
        try {
            for (CasePossibility possibily : possibilies) {
                for (int j = 0; j < possibily.conditions.length; j++) {
                    if (possibily.conditions[j].fits(value)) {
                        return possibily;
                    }
                }
            }
            return otherwise;
        } catch (RuntimePascalException e) {
            throw new ConstantCalculationException(e);
        }
    }
}
