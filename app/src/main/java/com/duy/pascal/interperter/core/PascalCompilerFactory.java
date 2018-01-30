package com.duy.pascal.interperter.core;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.ui.runnable.IProgramHandler;

import java.util.List;


public class PascalCompilerFactory {
    public static final boolean android = true;

    private PascalCompilerFactory() {
    }

    public static PascalProgramDeclaration makePascalProgram(@NonNull ScriptSource source,
                                                             List<ScriptSource> searchPath,
                                                             IProgramHandler handler) throws Exception {
        return new PascalProgramDeclaration(source, searchPath, handler, null);
    }


    public static PascalProgramDeclaration makePascalProgram(@NonNull ScriptSource source,
                                                             @Nullable List<ScriptSource> include,
                                                             @Nullable IProgramHandler handler,
                                                             @Nullable DiagnosticCollector collector) throws Exception {
        return new PascalProgramDeclaration(source, include, handler, collector);
    }


    public static PascalUnitDeclaration makePascalLibrary(@NonNull ScriptSource source,
                                                          @Nullable List<ScriptSource> searchPath,
                                                          @Nullable IProgramHandler handler) throws Exception {
        return new PascalUnitDeclaration(source, searchPath, handler);
    }

}
