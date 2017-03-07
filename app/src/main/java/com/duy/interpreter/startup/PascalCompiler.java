package com.duy.interpreter.startup;


import android.util.Log;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.lib.ConversionLib;
import com.duy.interpreter.lib.CrtLib;
import com.duy.interpreter.lib.DosLib;
import com.duy.interpreter.lib.GraphLib;
import com.duy.interpreter.lib.IOLib;
import com.duy.interpreter.lib.MathLib;
import com.duy.interpreter.lib.MiscLib;
import com.duy.interpreter.lib.ScriptControlLib;
import com.duy.interpreter.lib.SetArrayLengthLib;
import com.duy.interpreter.lib.SetLengthLib;
import com.duy.interpreter.lib.StringLib;
import com.duy.pascal.compiler.activities.ExecuteActivity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.PluginDeclaration;
import com.js.interpreter.ast.codeunit.ExecutableCodeUnit;
import com.js.interpreter.ast.codeunit.Library;
import com.js.interpreter.ast.codeunit.PascalProgram;
import com.js.interpreter.runtime.codeunit.RuntimeExecutable;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class PascalCompiler {
    public static final String TAG = PascalCompiler.class.getSimpleName();
    public static final boolean android = false;
    public static final boolean DEBUG = true;
    public ExecuteActivity activity;
    private IOLib ioLib = new IOLib();
    private CrtLib crtLib = new CrtLib();
    private GraphLib graphLib = new GraphLib();

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
            Reader reader = new FileReader(new File(fileName));
            debug(fileName, reader, includes, libraries);
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
        if (android) {
            Log.d(TAG, "loadFunctionTable: ");
        }
        ListMultimap<String, AbstractFunction> functionTable = ArrayListMultimap.create();
        loadPluginsPascal(functionTable);
        loadLibraries(functionTable, librarySearchPath, includeSearchPath);
        return functionTable;
    }

    public PascalProgram loadPascal(String sourcename, Reader in,
                                    List<ScriptSource> includeSearchPath,
                                    List<ScriptSource> librarySearchPath) throws ParsingException {
        ListMultimap<String, AbstractFunction> functiontable = loadFunctionTable(
                includeSearchPath, librarySearchPath);
        if (android) {
            Log.d(TAG, "loadPascal: " + sourcename);
        }
        return new PascalProgram(in, functiontable, sourcename, includeSearchPath);
    }

    public void executeScript(String sourcename, Reader in,
                              List<ScriptSource> includeSearchPath,
                              List<ScriptSource> librarySearchPath)
            throws ParsingException, RuntimePascalException {
        ListMultimap<String, AbstractFunction> functionTable = loadFunctionTable(
                includeSearchPath, librarySearchPath);
        ExecutableCodeUnit code;
//        long beforetime = System.currentTimeMillis();
        code = new PascalProgram(in, functionTable, sourcename, includeSearchPath);
//        System.out.println("Parse time = " + (System.currentTimeMillis() - beforetime) + " ms");
        RuntimeExecutable<?> runtime = code.run();
        runtime.run();
    }

    public void debug(String sourcename, Reader in,
                      List<ScriptSource> includeSearchPath,
                      List<ScriptSource> librarySearchPath)
            throws ParsingException, RuntimePascalException {
        System.out.print("debug");
        ListMultimap<String, AbstractFunction> functionTable = loadFunctionTable(includeSearchPath, librarySearchPath);
        new PascalProgram(in, functionTable, sourcename, includeSearchPath);
    }

    public void loadLibraries(
            ListMultimap<String, AbstractFunction> functionTable,
            List<ScriptSource> librarySearchPath,
            List<ScriptSource> includeSearchPath) throws ParsingException {
        for (ScriptSource directory : librarySearchPath) {
            for (String sourcefile : directory.list()) {
                Reader in = directory.read(sourcefile);
                if (in != null) {
                    // Automatically adds its definitions to the function table.
                    new Library(in, functionTable, sourcefile,
                            includeSearchPath);
                } else {
                    System.err.println("Warning, unable to read library "
                            + sourcefile);
                }

            }
        }
    }

    private void loadPluginsPascal(ListMultimap<String, AbstractFunction> functionTable) {
        ArrayList<Class> classes = new ArrayList<>();
        classes.add(ConversionLib.class);
        classes.add(MathLib.class);
        classes.add(MiscLib.class);
        classes.add(ScriptControlLib.class);
        classes.add(StringLib.class);
        classes.add(SetArrayLengthLib.class);
        classes.add(SetLengthLib.class);
        classes.add(ioLib.getClass());
        classes.add(crtLib.getClass());
        classes.add(DosLib.class);
        classes.add(graphLib.getClass());

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
            }
            for (Method m : pascalPlugin.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers())) {
                    PluginDeclaration tmp = new PluginDeclaration(o, m);
                    functionTable.put(tmp.name().toLowerCase(), tmp);
                }
//                System.out.println("#method " + m.getName());
            }
        }

    }


}
