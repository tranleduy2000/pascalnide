package com.duy.pascal.backend.ast.instructions.case_statement;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExecutionResult;
import com.duy.pascal.backend.ast.instructions.InstructionGrouper;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;

import java.util.ArrayList;
import java.util.List;

public class CaseInstruction extends DebuggableExecutable {
    private RuntimeValue mSwitchValue;
    private CasePossibility[] possibilities;
    private InstructionGrouper otherwise;
    private LineInfo line;

    public CaseInstruction(CaseToken token, ExpressionContext context)
            throws ParsingException {
        this.line = token.getLineNumber();
        mSwitchValue = token.getNextExpression(context);

        Token next = token.take();
        if (!(next instanceof OfToken)) {
            throw new ExpectedTokenException("of", next);
        }

        //this Object used to check compare type with another element
        DeclaredType switchValueType = mSwitchValue.getType(context).declType;
        List<CasePossibility> possibilities = new ArrayList<>();

        while (!(token.peek() instanceof ElseToken) && !(token.peek() instanceof EOFToken)) {
            List<Containable> conditions = new ArrayList<>();
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
            Executable command = token.getNextCommand(context);
            assertNextSemicolon(token);
            possibilities.add(new CasePossibility(conditions.toArray(new Containable[conditions.size()]), command));
        }

        otherwise = new InstructionGrouper(token.peek().getLineNumber());
        if (token.peek() instanceof ElseToken) {
            token.take();
            while (token.hasNext()) {
                otherwise.addCommand(token.getNextCommand(context));
                token.assertNextSemicolon(token);
            }
        }
        this.possibilities = possibilities.toArray(new CasePossibility[possibilities.size()]);
    }

    //check type
    private void assertType(DeclaredType switchValueType, RuntimeValue val,
                            ExpressionContext context) throws ParsingException {
        DeclaredType valueType = val.getType(context).declType;
        RuntimeValue converted = switchValueType.convert(val, context);
        if (converted == null) {
            throw new UnConvertibleTypeException(val, switchValueType, valueType, mSwitchValue, context);
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
                                       RuntimeExecutableCodeUnit<?> main) throws RuntimePascalException {
        Object value = mSwitchValue.getValue(context, main);
        for (CasePossibility possibility : possibilities) {
            for (int j = 0; j < possibility.conditions.length; j++) {
                if (possibility.conditions[j].contain(context, main, value)) {
                    return possibility.execute(context, main);
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
