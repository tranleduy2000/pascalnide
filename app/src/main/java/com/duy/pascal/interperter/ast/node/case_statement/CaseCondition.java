package com.duy.pascal.interperter.ast.node.case_statement;

import com.duy.pascal.interperter.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.interperter.ast.variablecontext.VariableContext;
import com.duy.pascal.interperter.linenumber.LineNumber;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

interface CaseCondition {
    boolean fits(VariableContext context, RuntimeExecutableCodeUnit<?> main, Object value) throws RuntimePascalException;

    LineNumber getLineNumber();
}
