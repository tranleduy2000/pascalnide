package com.js.interpreter.instructions.case_statement;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.runtime.exception.RuntimePascalException;

interface CaseCondition {
    boolean fits(Object value) throws RuntimePascalException;

    LineInfo getLine();
}
