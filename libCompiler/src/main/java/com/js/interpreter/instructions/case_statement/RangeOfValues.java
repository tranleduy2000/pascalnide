package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.operators.BinaryOperatorEval;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

class RangeOfValues implements CaseCondition {

    private LineInfo line;

    private BinaryOperatorEval greaterThanLower;
    private BinaryOperatorEval lessThanHigher;

    RangeOfValues(ExpressionContext context, RuntimeValue value, Object lower, Object higher,
                  LineInfo line) throws ParsingException {
        ConstantAccess low = new ConstantAccess(lower, line);
        ConstantAccess high = new ConstantAccess(higher, line);
        greaterThanLower = BinaryOperatorEval.generateOp(context, value, low, OperatorTypes.GREATEREQ, line);
        lessThanHigher = BinaryOperatorEval.generateOp(context, value, high, OperatorTypes.LESSEQ, line);
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
