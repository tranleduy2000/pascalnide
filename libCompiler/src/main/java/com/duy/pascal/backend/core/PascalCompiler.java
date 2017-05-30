package com.duy.pascal.backend.core;


import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.ast.AbstractFunction;
import com.duy.pascal.frontend.activities.IRunnablePascal;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.duy.pascal.backend.ast.codeunit.library.UnitPascal;
import com.duy.pascal.backend.ast.codeunit.program.PascalProgram;
import com.duy.pascal.backend.source_include.ScriptSource;

import java.io.Reader;
import java.util.List;


public class PascalCompiler {
    public static final String TAG = PascalCompiler.class.getSimpleName();
    public static final boolean android = true;
    public static final boolean DEBUG = false;

    public IRunnablePascal handler;

    public PascalCompiler(IRunnablePascal handler) {
        this.handler = handler;
    }

    public static ListMultimap<String, AbstractFunction> loadFunctionTable(
            List<ScriptSource> includeSearchPath) throws ParsingException {
        return ArrayListMultimap.create();
    }

    /**
     * constructor
     *
     * @param sourcename - file name
     * @param in         - Input Reader
     * @param handler    - handler for variable and function, debug, input and output to screen, ....
     */
    public static PascalProgram loadPascal(String sourcename, Reader in,
                                           List<ScriptSource> includeSearchPath,
                                           IRunnablePascal handler) throws ParsingException {
        ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(includeSearchPath);
        return new PascalProgram(in, functiontable, sourcename, includeSearchPath, handler);
    }


    public static UnitPascal loadLibrary(String sourcename, Reader in,
                                         List<ScriptSource> searchPath,
                                         IRunnablePascal handler) throws ParsingException {
        ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(searchPath);
        return new UnitPascal(in, sourcename, functiontable, searchPath, handler);
    }

}
