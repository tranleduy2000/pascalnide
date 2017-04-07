package com.duy.pascal.backend.lib.file_lib;

import android.util.Log;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotAssignException;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotOpenException;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotOpenForInputException;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLib implements PascalLibrary {

    public static final String TAG = FileLib.class.getSimpleName();

    /**
     * map file
     * key is the path of file
     */
    private HashMap<String, FileEntry> filesMap = new HashMap<>();
    private int numberFiles = 0;


    public void assign(VariableBoxer<File> fileVariable, String name) throws RuntimePascalException {
        Log.d(TAG, "assign: " + name);
        String path = ApplicationFileManager.getApplicationPath() + name;
        fileVariable.set(new File(path));

        //put to map
        FileEntry fileEntry = new FileEntry(name);
        filesMap.put(path, fileEntry);
    }

    public void reset(VariableBoxer<File> fileVariable) throws
            FileNotFoundException, RuntimePascalException {
        Log.d(TAG, "reset: " + fileVariable.get().getName());
        assertFileAssigned(fileVariable);
        //throw file not found exception
        filesMap.get(fileVariable.get().getPath()).reset();
    }

    public void rename(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        Log.d(TAG, "rename: " + fileVariable.get().getName());
        // TODO: 07-Apr-17
    }

    public void erase(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        Log.d(TAG, "erase: " + fileVariable.get().getName());
        // TODO: 07-Apr-17
    }


    public void rewrite(VariableBoxer<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileAssigned(fileVariable);
        Log.d(TAG, "rewrite: " + fileVariable.get().getName());
        filesMap.get(fileVariable.get().getPath()).rewrite();
    }


    /**
     * 102 File not assigned
     * This is reported by Reset, Rewrite, Append, Rename and Erase,
     * if you call them with an unassigned file as a parameter.
     */
    private void assertFileAssigned(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable.get());
    }

    private void assertFileAssigned(File fileVariable) throws RuntimePascalException {
        if (fileVariable == null) {
            throw new FileNotAssignException();
        }
        if (filesMap.get(fileVariable.getPath()) == null) {
            throw new FileNotAssignException();
        }
    }

    /**
     * 103 File not open
     * Reported by the following functions : Close, Read, Write,
     * Seek, EOf, FilePos, FileSize, Flush, BlockRead, and BlockWrite
     * if the file is not open.
     *
     * @throws FileNotOpenException
     * @throws FileNotAssignException
     */
    private void assertFileOpened(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpened(fileVariable.get());
    }

    private void assertFileOpened(File fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        if (!filesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenException();
        }
    }

    /**
     * close file
     */
    public void close(VariableBoxer<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileOpened(fileVariable);
        filesMap.get(fileVariable.get().getPath()).close();
    }

    public boolean eof(VariableBoxer<File> fileVariable) throws IOException,
            RuntimePascalException {
        assertFileOpened(fileVariable);
        assertFileOpenForInput(fileVariable);
        return filesMap.get(fileVariable.get().getPath()).isEof();
    }

    public void seekEof(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    public void seekEofLn(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    public void blockRead(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    public void append(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        // TODO: 07-Apr-17
    }


    public void read(File fileVariable,
                     VariableBoxer<Object> out) throws RuntimePascalException {
        System.out.println("readf " + fileVariable + " " + out.get().getClass().getSimpleName());
        //check error
        assertFileOpenForInput(fileVariable);

        FileEntry file = filesMap.get(fileVariable.getPath());
        if (out.get() instanceof Integer) {
            Integer integer = file.readInteger();
            out.set(integer);
        } else if (out.get() instanceof Long) {
            long value = file.readLong();
            out.set(value);
        } else if (out.get() instanceof Double) {
            double value = file.readDouble();
            out.set(value);
        } else if (out.get() instanceof Character) {
            char value = file.readChar();
            out.set(value);
        } else if (out.get() instanceof String) {
            String value = file.readString();
            out.set(value);
        }
    }

    /**
     * 04 File not open for input
     * Reported by Read, BlockRead, Eof, Eoln, SeekEof or SeekEoln if the file is not opened with Reset.
     */
    private void assertFileOpenForInput(VariableBoxer<File> fileVariable)
            throws RuntimePascalException {
        assertFileOpenForInput(fileVariable.get());
    }

    private void assertFileOpenForInput(File fileVariable)
            throws RuntimePascalException {
        assertFileOpened(fileVariable);
        if (!filesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenForInputException();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    public void readln(File fileVariable, VariableBoxer<Object> out) throws IOException, RuntimePascalException {
        //check error
        assertFileOpened(fileVariable);

        FileEntry f = filesMap.get(fileVariable.getPath());
        Object value = null;
        if (out.get() instanceof Integer) {
            value = f.readInteger();
            out.set(value);
            f.nextLine();
        } else if (out.get() instanceof Long) {
            value = f.readLong();
            out.set(value);
            f.nextLine();
        } else if (out.get() instanceof Double) {
            value = f.readDouble();
            f.nextLine();
            out.set(value);
        } else if (out.get() instanceof Character) {
            value = f.readChar();
            f.nextLine();
            out.set(value);
        } else if (out.get() instanceof StringBuilder) {
            value = f.readString();
            out.set(new StringBuilder((String) value));
        }
        System.out.println("readlnF 2 " + value.toString());
    }

    /**
     * write file
     *
     * @param objects
     * @return
     */
    public void writeF(VariableBoxer<File> fileVariable, Object... objects) throws IOException, RuntimePascalException {
        //check error
        assertFileOpened(fileVariable);
        FileEntry f = filesMap.get(fileVariable.get().getPath());
        f.write(objects);
    }

    /**
     * write file and append new line
     *
     * @param objects
     */
    public void writelnF(VariableBoxer<File> fileVariable, Object objects) throws
            IOException, RuntimePascalException {
        //check error
//        assertFileOpened(file);

        writeF(fileVariable, objects);
        writeF(fileVariable, "\n");
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

}
