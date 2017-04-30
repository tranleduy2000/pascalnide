package com.js.interpreter.ast;

import com.duy.pascal.backend.linenumber.LineInfo;

public interface NamedEntity {
    LineInfo getLineNumber();

    String getEntityType();

    String name();

    String description();
}
