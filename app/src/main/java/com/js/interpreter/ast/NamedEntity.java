package com.js.interpreter.ast;

import com.duy.pascal.backend.linenumber.LineInfo;

public interface NamedEntity {
    LineInfo getLine();

    String getEntityType();

    String name();

    String description();
}
