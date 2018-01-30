package com.duy.pascal.interperter.ast.codeunit;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.ui.runnable.IProgramHandler;

import java.util.List;

public abstract class ExecutableCodeUnit extends CodeUnit {


    public ExecutableCodeUnit(@NonNull ScriptSource source,
                              @Nullable List<ScriptSource> include,
                              @Nullable IProgramHandler handler,
                              @Nullable DiagnosticCollector diagnosticCollector) throws Exception {
        super(source, include, handler);
    }

    @Override
    public abstract RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate() throws RuntimePascalException;

}
