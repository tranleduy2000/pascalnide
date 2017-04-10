package com.duy.pascal.backend.core;


import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.lib.ConversionLib;
import com.duy.pascal.backend.lib.CrtLib;
import com.duy.pascal.backend.lib.DosLib;
import com.duy.pascal.backend.lib.graph.GraphLib;
import com.duy.pascal.backend.lib.MathLib;
import com.duy.pascal.backend.lib.StringLib;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.MethodDeclaration;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.core.ScriptSource;

import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;


public class PascalCompiler {
    public static final String TAG = PascalCompiler.class.getSimpleName();
    public static final boolean android = true;
    public static final boolean DEBUG = false;

    public ExecuteActivity activity;

    private SystemLib systemLib = new SystemLib(null);
    private IOLib ioLib = new IOLib(null);
    private CrtLib crtLib = new CrtLib(null);
    private GraphLib graphLib = new GraphLib(null);
    private FileLib fileLib = new FileLib();

    public PascalCompiler(ExecuteActivity activity) {
        this.activity = activity;
    }

    public ListMultimap<String, AbstractFunction> loadFunctionTable(
            List<ScriptSource> includeSearchPath,
            List<ScriptSource> librarySearchPath) throws ParsingException {

        ListMultimap<String, AbstractFunction> functionTable = ArrayListMultimap.create();
//        loadPluginsPascal(functionTable);
        loadLibraries(functionTable, librarySearchPath, includeSearchPath);
        return functionTable;
    }

    /**
     * constructor
     *
     * @param sourcename      - file name
     * @param in              - Input Reader
     * @param executeActivity - handler for variable and function
     */
    public PascalProgram loadPascal(String sourcename, Reader in,
                                    List<ScriptSource> includeSearchPath,
                                    List<ScriptSource> librarySearchPath,
                                    ExecuteActivity executeActivity) throws ParsingException {

        ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(
                includeSearchPath, librarySearchPath);
        return new PascalProgram(in, functiontable, sourcename, includeSearchPath, executeActivity);
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
//        classes.add(MiscLib.class);
        classes.add(StringLib.class);
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
//                    functionTable.put(tmp.name().toLowerCase(), tmp);
                }
                if (DEBUG)
                    System.out.println("#method " + m.getName());
            }

        }
    }
}
