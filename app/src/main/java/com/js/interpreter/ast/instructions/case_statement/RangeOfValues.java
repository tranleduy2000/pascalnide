package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RangeOfValues implements CaseCondition {

    private LineInfo line;

    private BinaryOperatorEvaluation greater_than_lower;
    private BinaryOperatorEvaluation less_than_higher;

    public RangeOfValues(ExpressionContext context, ReturnsValue value, Object lower, Object higher,
                         LineInfo line) throws ParsingException {
        ConstantAccess low = new ConstantAccess(lower, line);
        ConstantAccess hi = new ConstantAccess(higher, line);
        greater_than_lower = BinaryOperatorEvaluation.generateOp(context, value, low, OperatorTypes.GREATEREQ, line);
        less_than_higher = BinaryOperatorEvaluation.generateOp(context, value, hi, OperatorTypes.LESSEQ, line);
        this.line = line;
    }

    @Override
    public boolean fits(Object value) throws RuntimePascalException {
        return (Boolean) greater_than_lower.getValue(null, null) && (Boolean) less_than_higher.getValue(null, null);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }
}
