package com.duy.pascal.interperter.ast.node;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.expressioncontext.CompileTimeContext;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public interface Node {
    LineNumber getLineNumber();

    ExecutionResult visit(VariableContext context, RuntimeExecutableCodeUnit<?> main)
            throws RuntimePascalException;

    Node compileTimeConstantTransform(CompileTimeContext c) throws Exception;
}
