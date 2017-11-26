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

package com.duy.pascal.interperter.ast.codeunit;

import com.duy.pascal.interperter.ast.runtime.ScriptControl;
import com.duy.pascal.interperter.config.DebugMode;
import com.duy.pascal.interperter.debugable.DebugListener;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.function.MethodDeclaration;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.exceptions.runtime.ScriptTerminatedException;
import com.duy.pascal.interperter.exceptions.runtime.StackOverflowException;
import com.duy.pascal.interperter.linenumber.LineInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public abstract class RuntimeExecutableCodeUnit<parent extends ExecutableCodeUnit>
        extends RuntimeCodeUnit<parent> implements ScriptControl {

    private volatile long MAX_STACK = 45000;
    private volatile ControlMode mStatus = ControlMode.RUNNING;
    private volatile boolean mIsFinished = false;
    private final AtomicLong mStackSize = new AtomicLong(0);
    private volatile boolean mIsDebug = false;


    private DebugMode debugMode;
    private DebugListener debugListener;

    public RuntimeExecutableCodeUnit(parent definition) throws RuntimePascalException {
        super(definition);
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public void setDebug(boolean debug) {
        this.mIsDebug = debug;
    }

    public RuntimeUnitPascal getLibraryContext(PascalUnitDeclaration declaration) {
        HashMap<PascalUnitDeclaration, RuntimeUnitPascal> unitsMap = this.declaration.getContext().getRuntimeUnitMap();
        return unitsMap.get(declaration);
    }

    public RuntimePascalClass getRuntimePascalClassContext(Name identifier) {
        Map<Name, RuntimePascalClass> classMap
                = declaration.getContext().getRuntimePascalClassMap();
        return classMap.get(identifier);
    }

    public void addPascalClassContext(Name id, RuntimePascalClass runtimePascalClass) {
        Map<Name, RuntimePascalClass> classMap
                = declaration.getContext().getRuntimePascalClassMap();
        classMap.put(id, runtimePascalClass);
    }

    public void run() throws RuntimePascalException {
        try {
            runImpl();
        } catch (RuntimePascalException e) {
            this.mIsFinished = true;
            throw e;
        }
        this.mIsFinished = true;
    }

    public abstract void runImpl() throws RuntimePascalException;

    @Override
    public boolean doneExecuting() {
        return mIsFinished;
    }

    @Override
    public void pause() {
        mStatus = ControlMode.PAUSED;
    }

    public void enableDebug() {
        mIsDebug = true;
    }

    public void disableDebug() {
        mIsDebug = false;
    }

    @Override
    public void resume() {
        mStatus = ControlMode.RUNNING;
        synchronized (this) {
            this.notifyAll();
        }
    }

    @Override
    public void terminate() {
        mStatus = ControlMode.TERMINATED;
        synchronized (this) {
            this.notifyAll();
        }
    }

    public void scriptControlCheck(LineInfo line) throws ScriptTerminatedException {
        scriptControlCheck(line, mIsDebug);
    }

    /**
     * check mode program
     *
     * @param debug - is DEBUG enable
     * @throws ScriptTerminatedException - stop program
     */
    public void scriptControlCheck(LineInfo line, boolean debug) throws ScriptTerminatedException {
        do {
            if (mStatus == ControlMode.PAUSED || debug) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            if (mStatus == ControlMode.RUNNING) {
                return;
            }
            if (mStatus == ControlMode.TERMINATED) {
                List<AbstractFunction> shutdown =
                        getDeclaration().getProgram().getCallableFunctionsLocal(Name.create("shutdown"));
                for (AbstractFunction function : shutdown) {
                    if (function instanceof MethodDeclaration) {
                        try {
                            ((MethodDeclaration) function).visit(this, this, new Object[]{});
                        } catch (Exception ignored) {
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
        return mIsFinished;
    }

    public void incStack(LineInfo lineInfo) throws StackOverflowException {
        mStackSize.getAndIncrement();
        if (mStackSize.get() > MAX_STACK) {
            throw new StackOverflowException(lineInfo);
        }
    }

    public void decStack() {
        mStackSize.getAndDecrement();
    }

    public void setMaxStackSize(long maxStackSize) {
        this.MAX_STACK = maxStackSize;
    }

    public DebugMode getDebugMode() {
        return debugMode;
    }

    public void setDebugMode(DebugMode debugMode) {
        this.debugMode = debugMode;
    }

    public enum ControlMode {
        RUNNING, PAUSED, TERMINATED, DEBUG
    }
}
