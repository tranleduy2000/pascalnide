package com.js.interpreter.runtime.codeunit;

import com.duy.pascal.backend.debugable.DebugListener;
import com.js.interpreter.codeunit.PascalProgram;
import com.js.interpreter.codeunit.RunMode;
import com.js.interpreter.codeunit.library.LibraryPascal;
import com.js.interpreter.runtime.VariableContext;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuntimePascalProgram extends RuntimeExecutableCodeUnit<PascalProgram> {

    VariableContext main;

    public RuntimePascalProgram(PascalProgram p) {
        super(p);
    }

    public RuntimePascalProgram(PascalProgram p, DebugListener debugListener) {
        super(p, debugListener);
    }

    @Override
    public void runImpl() throws RuntimePascalException {
        this.mode = RunMode.RUNNING;

        HashMap<String, LibraryPascal> librariesMap = definition.getContext().getLibrariesMap();
        Set<Map.Entry<String, LibraryPascal>> entries = librariesMap.entrySet();
        for (Map.Entry<String, LibraryPascal> entry : entries) {
            getLibrary(entry.getValue()).runInit();
        }

        definition.main.execute(this, this);

        for (Map.Entry<String, LibraryPascal> entry : entries) {
            getLibrary(entry.getValue()).runFinal();
        }
    }

    @Override
    public VariableContext getParentContext() {
        return null;
    }
}
