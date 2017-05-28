package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime.VariableContext;
import com.duy.pascal.backend.runtime.exception.RuntimePascalException;
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit;

interface CaseCondition {
    boolean fits(VariableContext context, RuntimeExecutableCodeUnit<?> main, Object value) throws RuntimePascalException;

    LineInfo getLine();
}
