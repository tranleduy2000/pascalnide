package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ConstantCalculationException;
import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.EOF_Token;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExecutionResult;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.returnsvalue.CachedReturnsValue;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.ArrayList;
import java.util.List;

public class CaseInstruction extends DebuggableExecutable {
    ReturnsValue switch_value;
    CasePossibility[] possibilies;
    InstructionGrouper otherwise;
    LineInfo line;

    public CaseInstruction(CaseToken i, ExpressionContext context)
            throws ParsingException {
        this.line = i.lineInfo;
        switch_value = new CachedReturnsValue(i.getNextExpression(context));
        Token next = i.take();
        if (!(next instanceof OfToken)) {
            throw new ExpectedTokenException("of", next);
        }
        List<CasePossibility> possibilities = new ArrayList<CasePossibility>();
        while (!(i.peek() instanceof ElseToken)
                && !(i.peek() instanceof EOF_Token)) {
            List<CaseCondition> conditions = new ArrayList<CaseCondition>();
            while (true) {
                ReturnsValue val = i.getNextExpression(context);
                Object v = val.compileTimeValue(context);
                if (v == null) {
                    throw new NonConstantExpressionException(val);
                }
                if (i.peek() instanceof DotDotToken) {
                    i.take();
                    ReturnsValue upper = i.getNextExpression(context);
                    Object hi = upper.compileTimeValue(context);
                    if (hi == null) {
                        throw new NonConstantExpressionException(upper);
                    }
                    conditions.add(new RangeOfValues(switch_value, v, hi, val
                            .getLineNumber()));
                } else {
                    conditions.add(new SingleValue(v, val.getLineNumber()));
                }
                if (i.peek() instanceof CommaToken) {
                    i.take();
                    continue;
                } else if (i.peek() instanceof ColonToken) {
                    i.take();
                    break;
                } else {
                    throw new ExpectedTokenException("[comma or colon]", i.take());
                }
            }
            Executable command = i.getNextCommand(context);
            i.assertNextSemicolon();
            possibilities.add(new CasePossibility(conditions.toArray(new CaseCondition[conditions.size()]), command));
        }
        otherwise = new InstructionGrouper(i.peek().lineInfo);
        if (i.peek() instanceof ElseToken) {
            i.take();
            while (i.hasNext()) {
                otherwise.addCommand(i.getNextCommand(context));

                // TODO: 27-Mar-17 check EOF
                /**
                 * case i of
                 *  1 : writeln;
                 *  2 : writeln;
                 * else
                 *  writeln  //Adding a semicolon is not necessary
                 * end;
                 */
//                Token t = i.take();
//                if (i.peek() instanceof ElseToken)
//                if (!(t instanceof SemicolonToken)) {
//                    throw new ExpectedTokenException(";", t);
//                }
                i.assertNextSemicolon();
            }
        }
        this.possibilies = possibilities.toArray(new CasePossibility[possibilities.size()]);
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f,
                                       RuntimeExecutable<?> main) throws RuntimePascalException {
        Object value = switch_value.getValue(f, main);
        for (int i = 0; i < possibilies.length; i++) {
            for (int j = 0; j < possibilies[i].conditions.length; j++) {
                if (possibilies[i].conditions[j].fits(value)) {
                    return possibilies[i].execute(f, main);
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
            for (int i = 0; i < possibilies.length; i++) {
                for (int j = 0; j < possibilies[i].conditions.length; j++) {
                    if (possibilies[i].conditions[j].fits(value)) {
                        return possibilies[i];
                    }
                }
            }
            return otherwise;
        } catch (RuntimePascalException e) {
            throw new ConstantCalculationException(e);
        }
    }
}
