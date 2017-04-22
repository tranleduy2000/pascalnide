package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.debugable.DebuggableExecutable;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public class VariableSet extends DebuggableExecutable implements
        SetValueExecutable {
    private String name;

    private ReturnsValue value;
    private LineInfo line;

    public VariableSet(String name, ReturnsValue value, LineInfo line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public ReturnsValue getValue() {
        return value;
    }

    public LineInfo getLine() {
        return line;
    }

    @Override
    public ExecutionResult executeImpl(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        Object objValue = this.value.getValue(f, main);
        f.setVariable(name, objValue);
        if (main.isDebugMode()) {
            main.getDebugListener().onVariableChangeValue(name, null, objValue);
        }
        return ExecutionResult.NONE;
    }

    @Override
    public String toString() {
        return name + " := " + value;
    }


    @Override
    public void setAssignedValue(ReturnsValue value) {
        this.value = value;
    }

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return new VariableSet(name, value.compileTimeExpressionFold(c), line);
    }
}
