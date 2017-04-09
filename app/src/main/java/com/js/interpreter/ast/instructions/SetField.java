package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.variables.ContainsVariables;

public class SetField implements SetValueExecutable {
    ReturnsValue container;
    String name;
    ReturnsValue toset;
    LineInfo line;

    public SetField(ReturnsValue container, String name, LineInfo line,
                    ReturnsValue toset) {
        this.container = container;
        this.name = name;
        this.line = line;
        this.toset = toset;
    }

    @Override
    public LineInfo getline() {
        return line;
    }

    @Override
    public ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException {
        ContainsVariables val = (ContainsVariables) container.getValue(f, main);
        val.setVariable(name, toset.getValue(f, main));
        return ExecutionResult.NONE;
    }

    @Override
    public void setAssignedValue(ReturnsValue value) {
        this.toset = value;
    }

    @Override
    public SetValueExecutable compileTimeConstantTransform(CompileTimeContext c)
            throws ParsingException {
        return this;
    }

}
