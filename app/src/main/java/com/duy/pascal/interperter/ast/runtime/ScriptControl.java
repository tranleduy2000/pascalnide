package com.duy.pascal.interperter.ast.runtime;

public interface ScriptControl {
    void terminate();

    void pause();

    void resume();

    boolean doneExecuting();
}
