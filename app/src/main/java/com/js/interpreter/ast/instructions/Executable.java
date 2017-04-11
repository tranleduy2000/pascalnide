package com.js.interpreter.ast.instructions;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.expressioncontext.CompileTimeContext;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

public interface Executable {
    LineInfo getLine();

    /*
     * This should not modify the state of the object, unless it is a plugin
     * call.
     */
    ExecutionResult execute(VariableContext f, RuntimeExecutable<?> main)
            throws RuntimePascalException, StackOverflowException;

    Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException;
}
