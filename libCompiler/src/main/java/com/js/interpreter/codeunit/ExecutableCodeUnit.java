package com.js.interpreter.codeunit;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.source_include.ScriptSource;

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
