package com.js.interpreter.ast;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.DeclaredType;

public class ConstantDefinition implements NamedEntity {
    public DeclaredType type;
    private String name;
    private Object value;
    private LineInfo line;
    public ConstantDefinition(@NonNull String name, @NonNull Object value, LineInfo line) {
        this.name = name;
        this.value = value;
        this.line = line;
    }

    public ConstantDefinition(@NonNull String name, @Nullable DeclaredType type, @NonNull Object init, LineInfo line) {
        this.name = name;
        this.type = type;
        this.value = init;
        this.line = line;
    }

    public DeclaredType getType() {
        return type;
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

    @Override
    public String description() {
        return null;
    }
}
