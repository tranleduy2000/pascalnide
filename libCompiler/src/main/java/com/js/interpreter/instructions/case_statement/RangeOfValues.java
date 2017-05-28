package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
import com.duy.pascal.backend.pascaltypes.rangetype.Containable;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.duy.pascal.backend.runtime.operators.BinaryOperatorEval;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;
import com.js.interpreter.expressioncontext.ExpressionContext;

class RangeOfValues  implements Containable {

    private LineInfo line;

    private BinaryOperatorEval greaterThanLower;
    private BinaryOperatorEval lessThanHigher;

    RangeOfValues(ExpressionContext context, RuntimeValue value, Object lower, Object higher,
                  LineInfo line) throws ParsingException {
        ConstantAccess<Object> low = new ConstantAccess<>(lower, line);
        ConstantAccess<Object> high = new ConstantAccess<>(higher, line);
        greaterThanLower = BinaryOperatorEval.generateOp(context, value, low, OperatorTypes.GREATEREQ, line);
        lessThanHigher = BinaryOperatorEval.generateOp(context, value, high, OperatorTypes.LESSEQ, line);
        this.line = line;
    }


    @Override
    public boolean contain(VariableContext f, RuntimeExecutableCodeUnit<?> main, Object value)
            throws RuntimePascalException {
        return (Boolean) greaterThanLower.getValue(f, main)
                && (Boolean) lessThanHigher.getValue(f, main);
    }

    @Override
    public LineInfo getLineNumber() {
        return line;
    }


}
