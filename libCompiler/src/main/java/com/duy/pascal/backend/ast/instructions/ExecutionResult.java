package com.duy.pascal.backend.ast.instructions;

public enum ExecutionResult {
    NONE("none"), BREAK("break"), EXIT("exit"), CONTINUE("continue");
    public String toString;

    ExecutionResult(String s) {
        this.toString = s;
    }

    @Override
    public String toString() {
        return toString;
    }
}
