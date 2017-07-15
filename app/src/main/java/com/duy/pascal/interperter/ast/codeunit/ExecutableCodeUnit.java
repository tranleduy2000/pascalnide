package com.duy.pascal.interperter.ast.codeunit;

import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.source_include.ScriptSource;
import com.duy.pascal.frontend.runnable.ProgramHandler;

import java.io.Reader;
import java.util.List;

public abstract class ExecutableCodeUnit extends CodeUnit {

    public ExecutableCodeUnit(Reader r,
                              String sourceName,
                              List<ScriptSource> includeDirectories,
                              ProgramHandler handler)
            throws ParsingException {
        super(r, sourceName, includeDirectories, handler);
    }

    public ExecutableCodeUnit(ProgramHandler handler) {
        super(handler);
    }

    @Override
    public abstract RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate();

}
