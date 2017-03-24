package com.js.interpreter.runtime.codeunit;

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

    private Map<Library, RuntimeLibrary> RuntimeLibs = new HashMap<Library, RuntimeLibrary>();
    private volatile controlMode runmode = controlMode.running;
    private volatile boolean doneExecuting = false;

    public RuntimeExecutable(parent definition) {
        super(definition);
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
        runmode = controlMode.paused;
    }

    @Override
    public void resume() {
        runmode = controlMode.running;
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public void terminate() {
        runmode = controlMode.terminated;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void scriptControlCheck(LineInfo line)
            throws ScriptTerminatedException {
        do {
            if (runmode == controlMode.running) {
                return;
            }
            if (runmode == controlMode.terminated) {
                throw new ScriptTerminatedException(line);
            }
            if (runmode == controlMode.paused) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        } while (true);
    }

    public enum controlMode {
        running, paused, terminated
    }
}
