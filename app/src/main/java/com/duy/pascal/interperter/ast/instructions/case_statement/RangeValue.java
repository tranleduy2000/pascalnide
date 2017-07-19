package com.duy.pascal.interperter.ast.instructions.case_statement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.runtime_exception.RuntimePascalException;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;

public class RangeValue implements CaseCondition {

    private LineInfo line;

    private BinaryOperatorEval greaterThanLower;
    private BinaryOperatorEval lessThanHigher;

    RangeValue(ExpressionContext context, RuntimeValue value, Object lower, Object higher,
               LineInfo line) throws ParsingException {
        ConstantAccess<Object> low = new ConstantAccess<>(lower, line);
        ConstantAccess<Object> high = new ConstantAccess<>(higher, line);
        greaterThanLower = BinaryOperatorEval.generateOp(context, value, low, OperatorTypes.GREATEREQ, line);
        lessThanHigher = BinaryOperatorEval.generateOp(context, value, high, OperatorTypes.LESSEQ, line);
        this.line = line;
    }


    @Override
    public boolean fits(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object value)
            throws RuntimePascalException {
        return (Boolean) greaterThanLower.getValue(f, main)
                && (Boolean) lessThanHigher.getValue(f, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


}
