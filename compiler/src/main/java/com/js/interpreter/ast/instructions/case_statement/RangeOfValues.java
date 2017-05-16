package com.js.interpreter.ast.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.operators.number.BinaryOperatorEvaluation;
import com.js.interpreter.runtime.exception.RuntimePascalException;

class RangeOfValues implements CaseCondition {

    private LineInfo line;

    private BinaryOperatorEvaluation greaterThanLower;
    private BinaryOperatorEvaluation lessThanHigher;

    RangeOfValues(ExpressionContext context, ReturnValue value, Object lower, Object higher,
                  LineInfo line) throws ParsingException {
        ConstantAccess low = new ConstantAccess(lower, line);
        ConstantAccess high = new ConstantAccess(higher, line);
        greaterThanLower = BinaryOperatorEvaluation.generateOp(context, value, low, OperatorTypes.GREATEREQ, line);
        lessThanHigher = BinaryOperatorEvaluation.generateOp(context, value, high, OperatorTypes.LESSEQ, line);
        this.line = line;
    }

    @Override
    public boolean fits(Object value) throws RuntimePascalException {
        return (Boolean) greaterThanLower.getValue(null, null)
                && (Boolean) lessThanHigher.getValue(null, null);
    }

    @Override
    public LineInfo getLine() {
        return line;
    }
}
