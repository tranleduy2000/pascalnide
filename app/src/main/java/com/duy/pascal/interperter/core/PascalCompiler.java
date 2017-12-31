package com.duy.pascal.interperter.core;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.ui.runnable.ProgramHandler;

import java.util.List;


public class PascalCompiler {
    public static final String TAG = PascalCompiler.class.getSimpleName();
    public static final boolean android = true;
    public static final boolean DEBUG = false;

    public ProgramHandler handler;

    public PascalCompiler(ProgramHandler handler) {
        this.handler = handler;
    }

    /**
     * @param sourceName - source name
     * @param stream     - Input Reader
     * @param handler    - handler for variable and function, debug, input and output to screen, ....
     */
    public static PascalProgramDeclaration loadPascal(@NonNull ScriptSource source,
                                                      List<ScriptSource> searchPath,
                                                      ProgramHandler handler) throws Exception {
        return new PascalProgramDeclaration(source, searchPath, handler, null);
    }

    /**
     * @param sourcename - file name
     * @param in         - Input Reader
     * @param handler    - handler for variable and function, debug, input and output to screen, ....
     * @param collector  - collect error when parsing
     */
    public static PascalProgramDeclaration loadPascal(@NonNull ScriptSource source,
                                                      @Nullable List<ScriptSource> include,
                                                      @Nullable ProgramHandler handler,
                                                      @Nullable DiagnosticCollector collector) throws Exception {
        return new PascalProgramDeclaration(source, include, handler, collector);
    }


    public static PascalUnitDeclaration loadLibrary(@NonNull ScriptSource source,
                                                    @Nullable List<ScriptSource> searchPath,
                                                    @Nullable ProgramHandler handler) throws Exception {
        return new PascalUnitDeclaration(source, searchPath, handler);
    }

}
