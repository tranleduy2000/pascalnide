package com.duy.pascal.backend.runtime;

public interface ScriptControl {
    void terminate();

    void pause();

    void resume();

    boolean doneExecuting();
}
