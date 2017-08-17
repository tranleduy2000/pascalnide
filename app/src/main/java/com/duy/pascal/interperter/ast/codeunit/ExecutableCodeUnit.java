package com.duy.pascal.interperter.ast.codeunit;

import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.source.ScriptSource;

import java.io.Reader;
import java.util.List;

public abstract class ExecutableCodeUnit extends CodeUnit {


    public ExecutableCodeUnit(Reader r, String sourceName, List<ScriptSource> includeDirectories,
                              ProgramHandler handler, DiagnosticCollector diagnosticCollector) throws Exception {
        super(r, sourceName, includeDirectories, handler, diagnosticCollector);
    }

    @Override
    public abstract RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate();

}
