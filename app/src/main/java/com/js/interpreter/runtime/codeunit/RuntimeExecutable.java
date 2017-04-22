package com.js.interpreter.runtime.codeunit;

import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.exceptions.StackOverflowException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.runtime.ScriptControl;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.ScriptTerminatedException;

import java.util.HashMap;
import java.util.Map;

public abstract class RuntimeExecutable<parent extends ExecutableCodeUnit> extends RuntimeCodeUnit<parent>
        implements ScriptControl {

    public static final int MAX_STACK = 4000;

    private static final String TAG = "RuntimeExecutable";
    private long stack = 0;
    private Map<Library, RuntimeLibrary> RuntimeLibs = new HashMap<>();
    private volatile ControlMode runMode = ControlMode.RUNNING;
    private volatile boolean doneExecuting = false;
    private DebugListener debugListener;
    private boolean debugMode = false;

    public RuntimeExecutable(parent definition) {
        super(definition);
    }

    /**
     * if enable debug, uses this constructor
     *
     * @param debugListener - callback
     */
    public RuntimeExecutable(parent definition, DebugListener debugListener) {
        super(definition);
        this.debugListener = debugListener;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public RuntimeLibrary getLibrary(Library l) {
        RuntimeLibrary result = RuntimeLibs.get(l);
        if (result == null) {
            result = l.run();
            RuntimeLibs.put(l, result);
        }
        return result;
    }

    public void run() throws RuntimePascalException {
        try {
            runImpl();
        } catch (RuntimePascalException e) {
            this.doneExecuting = true;
            throw e;
        }
        this.doneExecuting = true;
    }

    public abstract void runImpl() throws RuntimePascalException;

    @Override
    public boolean doneExecuting() {
        return doneExecuting;
    }

    @Override
    public void pause() {
        runMode = ControlMode.PAUSED;
    }

    public void enableDebug() {
        debugMode = true;
    }

    public void disableDebug() {
        debugMode = false;
    }

    @Override
    public void resume() {
        runMode = ControlMode.RUNNING;
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public void terminate() {
        runMode = ControlMode.TERMINATED;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void scriptControlCheck(LineInfo line)
            throws ScriptTerminatedException {
        do {
            if (runMode == ControlMode.PAUSED || debugMode) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }

            if (runMode == ControlMode.RUNNING) {
                return;
            }
            if (runMode == ControlMode.TERMINATED) {
                throw new ScriptTerminatedException(line);
            }

        } while (true);
    }

    public DebugListener getDebugListener() {
        return debugListener;
    }

    public void setDebugListener(DebugListener debugListener) {
        this.debugListener = debugListener;
    }

    /**
     * asdas das das
     *
     * @return
     */
    public boolean isRunning() {
        return doneExecuting;
    }

    public void incStack(LineInfo lineInfo) throws StackOverflowException {
        stack++;
        if (stack > MAX_STACK) {
            throw new StackOverflowException(lineInfo);
        }
    }
    //4000 method

    public void decStack() {
        stack--;
    }

    public enum ControlMode {
        RUNNING, PAUSED, TERMINATED, debug
    }
}
