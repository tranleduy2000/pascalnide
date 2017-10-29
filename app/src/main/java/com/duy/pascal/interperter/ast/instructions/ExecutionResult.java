package com.duy.pascal.interperter.ast.instructions;

public enum ExecutionResult {
    NOPE("nope"), BREAK("break"), EXIT("exit"), CONTINUE("continue");
    public String str;

    ExecutionResult(String s) {
        this.str = s;
    }

    @Override
    public String toString() {
        return str;
    }
}
