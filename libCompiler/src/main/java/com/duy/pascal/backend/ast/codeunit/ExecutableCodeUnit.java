package com.duy.pascal.backend.ast.codeunit;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.frontend.activities.IRunnablePascal;

import java.io.Reader;
import java.util.List;

public abstract class ExecutableCodeUnit extends CodeUnit {

    public ExecutableCodeUnit(Reader r,
                              String sourceName,
                              List<ScriptSource> includeDirectories,
                              IRunnablePascal handler)
            throws ParsingException {
        super(r, sourceName, includeDirectories, handler);
    }

    public ExecutableCodeUnit(IRunnablePascal handler) {
        super(handler);
    }


    @Override
    public abstract RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> generate();

}
