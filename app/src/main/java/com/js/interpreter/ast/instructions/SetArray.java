package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.IndexOutOfBoundsException;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class SetArray implements SetValueExecutable {
    private ReturnsValue container;
    private ReturnsValue index;
    private int offset;
    private ReturnsValue val;

    public SetArray(ReturnsValue container, ReturnsValue index, int offset, ReturnsValue val) {
        this.container = container;
        this.index = index;
        this.offset = offset;
        this.val = val;
    }

    @Override
    public LineInfo getLine() {
        return index.getLine();
    }

    @Override
    public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object cont = container.getValue(f, main);
        Integer ind = (Integer) index.getValue(f, main);
        Object v = val.getValue(f, main);
        try {
            Array.set(cont, ind - offset, v);
        } catch (java.lang.IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(this.getLine(),
                    ind, offset, offset + ((Object[]) cont).length + offset - 1);
        }
        return ExecutionResult.NONE;
    }

    @Override
    public void setAssignedValue(ReturnsValue value) {
        this.val = value;
    }

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new SetArray(container.compileTimeExpressionFold(c),
                index.compileTimeExpressionFold(c), offset,
                val.compileTimeExpressionFold(c));
    }

}
