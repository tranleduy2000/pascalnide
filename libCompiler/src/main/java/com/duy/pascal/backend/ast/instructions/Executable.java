package com.duy.pascal.backend.ast.instructions;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.backend.ast.runtime_value.VariableContext;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public interface Executable {
    LineInfo getLineNumber();

    /*
     * This should not modify the state of the object, unless it is a plugin
     * call.
     */
    ExecutionResult execute(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException;
}
