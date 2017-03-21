package com.js.interpreter.ast;

import com.duy.interpreter.linenumber.LineInfo;

public interface NamedEntity {
    public LineInfo getLineNumber();

    public String getEntityType();

    public String name();
}
