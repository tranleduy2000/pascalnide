package com.js.interpreter.runtime;

public interface ScriptControl {
    void terminate();

    void pause();

    void resume();

    boolean doneExecuting();
}
