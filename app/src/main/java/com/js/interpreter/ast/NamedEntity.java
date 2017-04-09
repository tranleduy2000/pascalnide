package com.js.interpreter.ast;

import com.duy.pascal.backend.linenumber.LineInfo;

public interface NamedEntity {
    public LineInfo getline();

    public String getEntityType();

    public String name();
}
