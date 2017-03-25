package com.js.interpreter.ast.instructions;

public enum ExecutionResult {
    NONE("none"), BREAK("break"), RETURN("return");
    public String toString;

    ExecutionResult(String s) {
        this.toString = s;
    }

    @Override
    public String toString() {
        return toString;
    }
}
