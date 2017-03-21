package com.js.interpreter.ast;

import com.duy.pascal.backend.linenumber.LineInfo;

public interface NamedEntity {
    public LineInfo getLineNumber();

    public String getEntityType();

    public String name();
}
