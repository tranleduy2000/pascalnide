package com.js.interpreter.runtime;

public interface ScriptControl {
public void terminate();
public void pause();
public void resume();
public abstract boolean doneExecuting();
}
