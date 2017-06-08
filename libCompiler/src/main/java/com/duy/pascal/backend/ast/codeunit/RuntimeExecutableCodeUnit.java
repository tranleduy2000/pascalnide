/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.ast.codeunit;

import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.ast.MethodDeclaration;
import com.duy.pascal.backend.ast.codeunit.library.RuntimeUnitPascal;
import com.duy.pascal.backend.ast.codeunit.library.UnitPascal;
import com.duy.pascal.backend.ast.runtime_value.ScriptControl;
import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.backend.runtime_exception.ScriptTerminatedException;
import com.duy.pascal.backend.runtime_exception.StackOverflowException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RuntimeExecutableCodeUnit<parent extends ExecutableCodeUnit> extends RuntimeCodeUnit<parent>
        implements ScriptControl {
    private static final String TAG = "RuntimeExecutable";

    private volatile long MAX_STACK = 45000;
    private volatile ControlMode runMode = ControlMode.RUNNING;
    private volatile boolean doneExecuting = false;
    private volatile long stack = 0;
    private volatile boolean debug = false;

    private Map<UnitPascal, RuntimeUnitPascal> runtimeLibs = new HashMap<>();

    private DebugMode debugMode;
    private DebugListener debugListener;

    public RuntimeExecutableCodeUnit(parent definition) {
        super(definition);
    }

    public Map<UnitPascal, RuntimeUnitPascal> getRuntimeLibs() {
        return runtimeLibs;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public RuntimeUnitPascal getLibrary(UnitPascal l) {
        HashMap<UnitPascal, RuntimeUnitPascal> unitsMap = definition.getContext().getUnitsMap();
        return unitsMap.get(l);
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
        debug = true;
    }

    public void disableDebug() {
        debug = false;
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
        scriptControlCheck(line, debug);
    }

    /**
     * check mode program
     *
     * @param debug - is DEBUG enable
     * @throws ScriptTerminatedException - stop program
     */
    public void scriptControlCheck(LineInfo line, boolean debug) throws ScriptTerminatedException {
        do {
            if (runMode == ControlMode.PAUSED || debug) {
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
                List<AbstractFunction> shutdown = this.getDefinition().getProgram().getCallableFunctionsLocal("shutdown");
                for (AbstractFunction function : shutdown) {
                    if (function instanceof MethodDeclaration) {
                        try {
                            ((MethodDeclaration) function).call(null, this, new Object[]{});
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (StackOverflowException e) {
                            e.printStackTrace();
                        } catch (RuntimePascalException e) {
                            e.printStackTrace();
                        }
                    }
                }
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


    public boolean isRunning() {
        return doneExecuting;
    }

    public void incStack(LineInfo lineInfo) throws StackOverflowException {
        stack++;
        if (stack > MAX_STACK) {
            throw new StackOverflowException(lineInfo);
        }
    }

    public void decStack() {
        stack--;
    }

    public void setMaxStackSize(long maxStackSize) {
        this.MAX_STACK = maxStackSize;
    }

    public enum ControlMode {
        RUNNING, PAUSED, TERMINATED, DEBUG
    }
}
