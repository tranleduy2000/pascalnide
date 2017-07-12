package com.duy.pascal.backend.ast.runtime_value;

public interface ScriptControl {
    void terminate();

    void pause();

    void resume();

    boolean doneExecuting();
}
