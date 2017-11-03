package com.duy.pascal.interperter.core;


import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.declaration.program.PascalProgramDeclaration;
import com.duy.pascal.interperter.exceptions.DiagnosticCollector;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.ui.runnable.ProgramHandler;

import java.io.Reader;
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
    public static PascalProgramDeclaration loadPascal(String sourceName, Reader stream,
                                                      List<ScriptSource> searchPath,
                                                      ProgramHandler handler) throws Exception {
        return new PascalProgramDeclaration(stream, sourceName, searchPath, handler, null);
    }

    /**
     * @param sourcename - file name
     * @param in         - Input Reader
     * @param handler    - handler for variable and function, debug, input and output to screen, ....
     * @param collector  - collect error when parsing
     */
    public static PascalProgramDeclaration loadPascal(String sourcename, Reader in,
                                                      List<ScriptSource> includeSearchPath,
                                                      ProgramHandler handler, DiagnosticCollector collector) throws Exception {
        return new PascalProgramDeclaration(in, sourcename, includeSearchPath, handler, collector);
    }


    public static PascalUnitDeclaration loadLibrary(String sourcename, Reader in,
                                                    List<ScriptSource> searchPath,
                                                    ProgramHandler handler) throws Exception {
        return new PascalUnitDeclaration(in, sourcename, searchPath, handler);
    }

}
