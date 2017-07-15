package com.duy.pascal.interperter.ast.runtime_value;

public interface ScriptControl {
    void terminate();

    void pause();

    void resume();

    boolean doneExecuting();
}
