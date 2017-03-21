package com.js.interpreter.ast;


import com.duy.pascal.backend.linenumber.LineInfo;

public class ConstantDefinition implements NamedEntity {
    private String name;
    private Object value;
    private LineInfo line;

    public ConstantDefinition(String name, Object value, LineInfo line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public Object getValue() {
        return value;
    }

    public LineInfo getLineNumber() {
        return line;
    }

    @Override
    public String getEntityType() {
        return "constant";
    }

    @Override
    public String name() {
        return name;
    }
}
