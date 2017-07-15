package com.duy.pascal.interperter.ast.instructions;

public enum ExecutionResult {
    NOPE("nope"), BREAK("break"), EXIT("exit"), CONTINUE("continue");
    public String toString;

    ExecutionResult(String s) {
        this.toString = s;
    }

    @Override
    public String toString() {
        return toString;
    }
}
