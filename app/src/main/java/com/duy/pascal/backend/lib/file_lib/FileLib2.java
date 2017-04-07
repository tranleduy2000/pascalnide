package com.duy.pascal.backend.lib.file_lib;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotAssignException;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotOpenException;
import com.duy.pascal.backend.lib.file_lib.exceptions.FileNotOpenForInputException;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileLib2 implements PascalLibrary {

    private HashMap<Integer, FileEntry> filesMap = new HashMap<>();

    private int numberFiles = 0;

//    /**
//     * add new file to map file
//     *
//     * @param fileID - id to store address of file
//     * @param name   - file path
//     */
//    public void assign(VariableBoxer<Integer> fileID, String name) {
//        //assign(f, 'file.inp');
//        numberFiles++;
//        fileID.set(numberFiles);
//        FileEntry fileEntry = new FileEntry(name);
//        filesMap.put(numberFiles, fileEntry);
//
//    }

    /**
     * add new file to map file
     *
     * @param fileID - id for store address of file
     * @param name   - file path
     */
    public void assign(VariableBoxer<File> fileID, String name) throws RuntimePascalException {
        //assign(f, 'file.inp');
//        numberFiles++;
//        fileID.set(numberFiles);
//        FileEntry fileEntry = new FileEntry(name);
//        filesMap.put(numberFiles, fileEntry);

    }

    public void reset(int fileID) throws FileNotFoundException, FileNotAssignException {
        assertFileAssigned(fileID);
        //throw file not found exception
        filesMap.get(fileID).reset();
    }

    public void rename(int fileID) throws FileNotAssignException {
        assertFileAssigned(fileID);
        // TODO: 07-Apr-17
    }

    public void erase(int fileID) throws FileNotAssignException {
        assertFileAssigned(fileID);
        // TODO: 07-Apr-17
    }


    public void rewrite(int fileID) throws IOException, FileNotAssignException {
        assertFileAssigned(fileID);
        filesMap.get(fileID).rewrite();
    }


    /**
     * 102 File not assigned
     * This is reported by Reset, Rewrite, Append, Rename and Erase,
     * if you call them with an unassigned file as a parameter.
     */
    private void assertFileAssigned(int fileID) throws FileNotAssignException {
        if (filesMap.get(fileID) == null) {
            throw new FileNotAssignException();
        }
    }

    /**
     * 103 File not open
     * Reported by the following functions : Close, Read, Write,
     * Seek, EOf, FilePos, FileSize, Flush, BlockRead, and BlockWrite
     * if the file is not open.
     *
     * @param fileID - file id in map file
     * @throws FileNotOpenException
     * @throws FileNotAssignException
     */
    private void assertFileOpened(int fileID) throws FileNotOpenException, FileNotAssignException {
        assertFileAssigned(fileID);
        if (!filesMap.get(fileID).isOpened()) {
            throw new FileNotOpenException();
        }
    }

    /**
     * close file
     *
     * @param fileID
     */
    public void close(int fileID) throws IOException, FileNotOpenException, FileNotAssignException {
        assertFileOpened(fileID);
        filesMap.get(fileID).close();
    }

    public boolean eof(int fileID) throws IOException,
            FileNotAssignException, FileNotOpenForInputException, FileNotOpenException {
        assertFileOpened(fileID);
        assertFileOpenForInput(fileID);
        return filesMap.get(fileID).isEof();
    }

    public void seekEof(int fileID) throws FileNotAssignException,
            FileNotOpenForInputException, FileNotOpenException {
        assertFileOpenForInput(fileID);
    }

    public void seekEofLn(int fileID) throws FileNotAssignException,
            FileNotOpenForInputException, FileNotOpenException {
        assertFileOpenForInput(fileID);
    }

    public void blockRead(int fileID) throws FileNotAssignException,
            FileNotOpenForInputException, FileNotOpenException {
        assertFileOpenForInput(fileID);
    }

    public void append(int fileID) throws FileNotAssignException {
        assertFileAssigned(fileID);
        // TODO: 07-Apr-17
    }


    /**
     * read file
     *
     * @param fileID
     * @param out
     * @return
     */
    public void readf(int fileID, VariableBoxer<Object> out) throws RuntimePascalException {
        System.out.println("readf " + fileID + " " + out.get().getClass().getSimpleName());
        //check error
        assertFileOpenForInput(fileID);

        FileEntry file = filesMap.get(fileID);
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
    private void assertFileOpenForInput(int fileID) throws FileNotOpenException,
            FileNotAssignException, FileNotOpenForInputException {
        assertFileOpened(fileID);
        if (!filesMap.get(fileID).isOpened()) {
            throw new FileNotOpenForInputException();
        }
    }

    /**
     * read file and  move cursor to new line
     *
     * @param fileID
     * @param out
     */
    public void readlnF(int fileID, VariableBoxer<Object> out) throws IOException, RuntimePascalException {
        //check error
        assertFileOpened(fileID);

        FileEntry f = filesMap.get(fileID);
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
     * @param fileID
     * @param objects
     * @return
     */
    public void writeF(int fileID, Object... objects) throws IOException, FileNotOpenException,
            FileNotAssignException {
        //check error
        assertFileOpened(fileID);
        FileEntry f = filesMap.get(fileID);
        f.write(objects);
    }

    /**
     * write file and append new line
     *
     * @param fileID
     * @param objects
     */
    public void writelnF(int fileID, Object objects) throws
            IOException, FileNotOpenException, FileNotAssignException {
        //check error
        assertFileOpened(fileID);

        writeF(fileID, objects);
        writeF(fileID, "\n");
    }

    private void writeToFile(FileEntry fileEntry, Object... arg) throws IOException {
        fileEntry.write(arg);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    /////////////////////////////////////////////////
    //////////// NON_FUNCTION_PASCAL ////////////////
    /////////////////////////////////////////////////

    public boolean fileExists(String s) {
        File f = new File(s);
        return f.exists() && !f.isDirectory();
    }

    public boolean directoryExists(String DirectoryName) {
        File f = new File(DirectoryName);
        return f.exists() && f.isDirectory();
    }

    private void checkFile(int fileID) throws IOException {
        FileEntry fileEntry = filesMap.get(fileID);
        if (fileEntry == null) {
            throw new IOException("File not found");
        }
    }


}
