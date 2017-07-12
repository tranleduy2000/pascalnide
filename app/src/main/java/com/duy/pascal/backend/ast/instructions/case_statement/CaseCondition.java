package com.duy.pascal.backend.ast.instructions.case_statement;

import com.duy.pascal.backend.ast.codeunit.RuntimeExecutableCodeUnit;
import com.duy.pascal.backend.ast.variablecontext.VariableContext;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

interface CaseCondition {
    boolean fits(VariableContext context, RuntimeExecutableCodeUnit<?> main, Object value) throws RuntimePascalException;

    LineInfo getLineNumber();
}
