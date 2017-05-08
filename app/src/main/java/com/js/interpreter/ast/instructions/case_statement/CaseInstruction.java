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
import com.js.interpreter.ast.returnsvalue.CachedReturnValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.List;

public class CaseInstruction extends DebuggableExecutable {
    private ReturnValue switch_value;
    private CasePossibility[] possibilies;
    private InstructionGrouper otherwise;
    private LineInfo line;

    public CaseInstruction(CaseToken i, ExpressionContext context)
            throws ParsingException {
        this.line = i.lineInfo;
        switch_value = new CachedReturnValue(i.getNextExpression(context));
        Token next = i.take();
        if (!(next instanceof OfToken)) {
            throw new ExpectedTokenException("of", next);
        }

        //this Object used to check compare type with another element
        DeclaredType mSwitchValueType = switch_value.getType(context).declType;
        List<CasePossibility> possibilities = new ArrayList<>();

        while (!(i.peek() instanceof ElseToken) && !(i.peek() instanceof EOFToken)) {
            List<CaseCondition> conditions = new ArrayList<>();
            while (true) {
                ReturnValue valueToSwitch = i.getNextExpression(context);

                //check type
                assertType(mSwitchValueType, valueToSwitch, context);

                Object v = valueToSwitch.compileTimeValue(context);
                if (v == null) {
                    throw new NonConstantExpressionException(valueToSwitch);
                }
                if (i.peek() instanceof DotDotToken) {
                    i.take();
                    ReturnValue upper = i.getNextExpression(context);
                    Object hi = upper.compileTimeValue(context);
                    if (hi == null) {
                        throw new NonConstantExpressionException(upper);
                    }
                    conditions.add(new RangeOfValues(context, switch_value, v, hi, valueToSwitch.getLineNumber()));
                } else {
                    conditions.add(new SingleValue(v, valueToSwitch.getLineNumber()));
                }
                if (i.peek() instanceof CommaToken) {
                    i.take();
                } else if (i.peek() instanceof ColonToken) {
                    i.take();
                    break;
                } else {
                    throw new ExpectedTokenException("[comma or colon]", i.take());
                }
            }
            Executable command = i.getNextCommand(context);
            assertNextSemicolon(i);
            possibilities.add(new CasePossibility(conditions.toArray(new CaseCondition[conditions.size()]), command));
        }

        otherwise = new InstructionGrouper(i.peek().lineInfo);
        if (i.peek() instanceof ElseToken) {
            i.take();
            while (i.hasNext()) {
                otherwise.add_command(i.getNextCommand(context));
                /*
                  case i of
                   1 : writeln;
                   2 : writeln;
                  else
                   writeln  //Adding a semicolon is not necessary
                  end;
                 */
//                Token t = i.take();
//                if (i.peek() instanceof ElseToken)
//                if (!(t instanceof SemicolonToken)) {
//                    throw new ExpectedTokenException(";", t);
//                }
                i.assertNextSemicolon(i);
//                if (!(i.peek() instanceof EndToken)
//                        && !(i.peek() instanceof EOFToken)) {
//                    i.assertNextSemicolon();
//                }
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
     *
     * @param grouperToken
     * @throws ParsingException
     */
    private void assertNextSemicolon(GrouperToken grouperToken) throws ParsingException {
//        i.assertNextSemicolon();
        if (grouperToken.peek() instanceof ElseToken) return;
        grouperToken.assertNextSemicolon(grouperToken);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        Object value = switch_value.getValue(f, main);
        for (CasePossibility possibily : possibilies) {
            for (int j = 0; j < possibily.conditions.length; j++) {
                if (possibily.conditions[j].fits(value)) {
                    return possibily.execute(f, main);
                }
            }
        }
        return otherwise.execute(f, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public Executable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        Object value = switch_value.compileTimeValue(c);
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
