package com.duy.pascal.interperter.ast.instructions;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public interface Executable {
    LineInfo getLineNumber();

    ExecutionResult execute(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    Executable compileTimeConstantTransform(CompileTimeContext c) throws ParsingException;
}
