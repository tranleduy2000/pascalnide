package com.duy.pascal.backend.core;


import com.duy.pascal.backend.debugable.DebugListener;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.lib.ConversionLib;
import com.duy.pascal.backend.lib.CrtLib;
import com.duy.pascal.backend.lib.DosLib;
import com.duy.pascal.backend.lib.GraphLib;
import com.duy.pascal.backend.lib.IOLib;
import com.duy.pascal.backend.lib.MathLib;
import com.duy.pascal.backend.lib.MiscLib;
import com.duy.pascal.backend.lib.StringLib;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file_lib.FileLib;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.MethodDeclaration;
import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class PascalCompiler {
    public static final String TAG = PascalCompiler.class.getSimpleName();
    public static final boolean android = true;
    public static final boolean DEBUG = true;

    public ExecuteActivity activity;

    private SystemLib systemLib = new SystemLib(null);
    private IOLib ioLib = new IOLib(null);
    private CrtLib crtLib = new CrtLib(null);
    private GraphLib graphLib = new GraphLib(null);
    private FileLib fileLib = new FileLib();

    public PascalCompiler(ExecuteActivity activity) {
        this.activity = activity;
    }

    public static void main(String[] args) {
        new PascalCompiler(null).amain();
    }

    public void amain() {
        System.out.println("-------------------");
        try {
            List<ScriptSource> libraries = new ArrayList<ScriptSource>();
            List<ScriptSource> includes = new ArrayList<ScriptSource>();
            String fileName = "C:\\Users\\Duy\\Downloads\\tests\\compare\\test_function.pas";
            String program = "var a, b: integer; begin a := 1; b := 2; write(a); write(b); end.";
            Reader reader = new FileReader(new File(fileName));
            debug(fileName, new StringReader(program), includes, libraries);
        } catch (ParsingException e) {
            if (DEBUG) System.err.println(e.line + ":" + e.getMessage());
        } catch (RuntimePascalException e) {
            if (DEBUG) System.err.println(e.line + ":runtime error:" + e.getMessage());
        } catch (FileNotFoundException e) {
            if (DEBUG) e.printStackTrace();
        }
    }

    public ListMultimap<String, AbstractFunction> loadFunctionTable(
            List<ScriptSource> includeSearchPath,
            List<ScriptSource> librarySearchPath) throws ParsingException {

        ListMultimap<String, AbstractFunction> functionTable = ArrayListMultimap.create();
        loadPluginsPascal(functionTable);
        loadLibraries(functionTable, librarySearchPath, includeSearchPath);
        return functionTable;
    }

    /**
     * constructor
     *
     * @param sourcename    - file name
     * @param in            - Input Reader
     * @param debugListener - handler for variable and function
     */
    public PascalProgram loadPascal(String sourcename, Reader in,
                                    List<ScriptSource> includeSearchPath,
                                    List<ScriptSource> librarySearchPath,
                                    DebugListener debugListener) throws ParsingException {

        ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(
                includeSearchPath, librarySearchPath);
        return new PascalProgram(in, functiontable, sourcename, includeSearchPath, debugListener);
    }

    public PascalProgram loadPascal(String sourcename, Reader in,
                                    List<ScriptSource> includeSearchPath,
                                    List<ScriptSource> librarySearchPath) throws ParsingException {

        ListMultimap<String, AbstractFunction> functionTable
                = loadFunctionTable(includeSearchPath, librarySearchPath);
        return new PascalProgram(in, functionTable, sourcename, includeSearchPath);
    }

    public void executeScript(String sourceName, Reader in,
                              List<ScriptSource> includeSearchPath,
                              List<ScriptSource> librarySearchPath)
            throws ParsingException, RuntimePascalException {
        ListMultimap<String, AbstractFunction> functionTable = loadFunctionTable(
                includeSearchPath, librarySearchPath);
        ExecutableCodeUnit code;
//        long beforetime = System.currentTimeMillis();
        code = new PascalProgram(in, functionTable, sourceName, includeSearchPath);
//        System.out.println("Parse time = " + (System.currentTimeMillis() - beforetime) + " ms");
        RuntimeExecutable<?> runtime = code.run();
        runtime.run();
    }

    public void debug(String sourcename, Reader in,
                      List<ScriptSource> includeSearchPath,
                      List<ScriptSource> librarySearchPath)
            throws ParsingException, RuntimePascalException {
//        System.out.print("debug");
        ListMultimap<String, AbstractFunction> functionTable = loadFunctionTable(includeSearchPath, librarySearchPath);
        new PascalProgram(in, functionTable, sourcename, includeSearchPath);
    }

    public void loadLibraries(ListMultimap<String, AbstractFunction> functionTable,
                              List<ScriptSource> librarySearchPath,
                              List<ScriptSource> includeSearchPath) throws ParsingException {
        for (ScriptSource directory : librarySearchPath) {
            for (String sourceFile : directory.list()) {
                Reader in = directory.read(sourceFile);
                if (in != null) {
                    // Automatically adds its definitions to the function table.
                    new Library(in, functionTable, sourceFile, includeSearchPath);
                } else {
                    System.err.println("Warning, unable to read library " + sourceFile);
                }

            }
        }
    }

    private void loadPluginsPascal(ListMultimap<String, AbstractFunction> functionTable) {

        ArrayList<Class> classes = new ArrayList<>();
        classes.add(ConversionLib.class);
        classes.add(MathLib.class);
        classes.add(MiscLib.class);
        classes.add(StringLib.class);
//        classes.add(SetArrayLengthLib.class);
//        classes.add(SetLengthLib.class);
        classes.add(systemLib.getClass());
        classes.add(crtLib.getClass());
        classes.add(DosLib.class);
        classes.add(graphLib.getClass());

        /**
         * Important: load file library before io lib. Because
         * method readln(file, ...) in {@link FileLib} will be override method readln(object...) in {@link IOLib}
         */
        classes.add(fileLib.getClass());
        classes.add(ioLib.getClass());


        for (Class pascalPlugin : classes) {
            Object o;
            try {
                Constructor constructor = pascalPlugin.getConstructor(ExecuteActivity.class);
                o = constructor.newInstance(activity);
            } catch (NoSuchMethodException
                    | IllegalArgumentException |
                    IllegalAccessException | InvocationTargetException |
                    InstantiationException e) {
                o = null;
                try {
                    Constructor constructor = pascalPlugin.getConstructor();
                    o = constructor.newInstance();
                } catch (NoSuchMethodException
                        | IllegalArgumentException |
                        IllegalAccessException | InvocationTargetException |
                        InstantiationException e1) {
                    e1.printStackTrace();
                }
            }

            for (Method m : pascalPlugin.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers())) {
                    MethodDeclaration tmp = new MethodDeclaration(o, m);
                    functionTable.put(tmp.name().toLowerCase(), tmp);
                }
                System.out.println("#method " + m.getName());
            }

        }
    }
}
