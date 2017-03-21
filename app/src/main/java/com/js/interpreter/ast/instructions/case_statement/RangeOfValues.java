package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class RangeOfValues implements CaseCondition {

    LineInfo line;

    BinaryOperatorEvaluation greater_than_lower;
    BinaryOperatorEvaluation less_than_higher;

    public RangeOfValues(ReturnsValue value, Object lower, Object higher,
                         LineInfo line) throws ParsingException {
        ConstantAccess low = new ConstantAccess(lower, line);
        ConstantAccess hi = new ConstantAccess(higher, line);
        BinaryOperatorEvaluation greater_than_lower = BinaryOperatorEvaluation
                .generateOp(null, value, low, OperatorTypes.GREATEREQ, line);
        BinaryOperatorEvaluation less_than_higher = BinaryOperatorEvaluation
                .generateOp(null, value, hi, OperatorTypes.LESSEQ, line);
        this.line = line;
    }

    @Override
    public boolean fits(Object value) throws RuntimePascalException {

        return (Boolean) greater_than_lower.getValue(null, null)
                && (Boolean) less_than_higher.getValue(null, null);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }
}
