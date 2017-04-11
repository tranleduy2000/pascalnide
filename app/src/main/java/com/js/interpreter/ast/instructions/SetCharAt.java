package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class SetCharAt implements SetValueExecutable {
    ReturnsValue container;
    ReturnsValue index;
    ReturnsValue value;

    public SetCharAt(ReturnsValue container, ReturnsValue index,
                     ReturnsValue value) {
        this.container = container;
        this.index = index;
        this.value = value;
    }

    @Override
    public LineInfo getLine() {
        return container.getLine();
    }

    @Override
    public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        StringBuilder s = (StringBuilder) container.getValue(f, main);
        Integer i = (Integer) index.getValue(f, main);
        Character c = (Character) value.getValue(f, main);
        s.setCharAt(i - 1, c);
        return ExecutionResult.NONE;
    }

    @Override
    public void setAssignedValue(ReturnsValue value) {
        this.value = value;
    }

    @Override
    public SetCharAt compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SetCharAt(container.compileTimeExpressionFold(c),
                index.compileTimeExpressionFold(c),
                value.compileTimeExpressionFold(c));
    }

}
