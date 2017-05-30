package com.duy.pascal.backend.ast.codeunit;

import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.source_include.ScriptSource;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.google.common.collect.ListMultimap;

import java.io.Reader;
import java.util.List;

public abstract class ExecutableCodeUnit extends CodeUnit {

    public ExecutableCodeUnit(Reader r,
                              ListMultimap<String, AbstractFunction> functionTable,
                              String sourceName, //for DEBUG
                              List<ScriptSource> includeDirectories,
                              IRunnablePascal handler)
            throws ParsingException {
        super(r, functionTable, sourceName, includeDirectories, handler);
    }

    @Override
    public abstract RuntimeExecutableCodeUnit<? extends ExecutableCodeUnit> run();

}
