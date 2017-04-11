package com.js.interpreter.ast;


import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class ConstantDefinition implements NamedEntity {
    public DeclaredType type;
    private String name;
    private Object value;
    private LineInfo line;

    public ConstantDefinition(String name, Object value, LineInfo line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public ConstantDefinition(String name, DeclaredType type, Object value, LineInfo line) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public Object getValue() {
        return value;
    }

    public LineInfo getLine() {
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
