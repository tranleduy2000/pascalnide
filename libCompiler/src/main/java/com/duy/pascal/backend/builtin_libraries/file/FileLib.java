/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.builtin_libraries.file;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.runtime_value.references.PascalReference;
import com.duy.pascal.backend.ast.runtime_value.variables.CustomVariable;
import com.duy.pascal.backend.builtin_libraries.IPascalLibrary;
import com.duy.pascal.backend.builtin_libraries.annotations.PascalMethod;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotAssignException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.backend.builtin_libraries.io.InOutListener;
import com.duy.pascal.backend.builtin_libraries.runtime_exceptions.CanNotReadVariableException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.DLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileLib implements IPascalLibrary {
    private static final String TAG = "FileLib";
    /**
     * map file
     * key is the path of file
     */
    private HashMap<String, FileEntry> filesMap = new HashMap<>();
    private int numberFiles = 0;
    private InOutListener handler;

    public FileLib(InOutListener handler) {
        this.handler = handler;
    }

    /**
     * assign file,
     */
    @PascalMethod(description = "library file")
    public void assign(PascalReference<File> fileVariable, StringBuilder name) throws RuntimePascalException {
        DLog.d(TAG, "assign() called with: fileVariable = [" + fileVariable + "], name = [" + name + "]");
        File file = new File(name.toString());
        if (!file.exists()) {
            file = new File(handler.getCurrentDirectory(), name.toString());
        }
        DLog.d("File " + file);
        fileVariable.set(file);

        //put to map
        FileEntry fileEntry = new FileEntry(file);
        filesMap.put(file.getPath(), fileEntry);
    }

    @PascalMethod(description = "library file")
    public void AssignFile(PascalReference<File> fileVariable, StringBuilder name) throws RuntimePascalException {
        assign(fileVariable, name);
    }

    /**
     * open file for read
     */
    @PascalMethod(description = "library file")
    public void reset(PascalReference<File> fileVariable) throws
            FileNotFoundException, RuntimePascalException {
        assertFileAssigned(fileVariable);
        //throw file not found exception
        filesMap.get(fileVariable.get().getPath()).reset();
    }

    /**
     * rename file
     */
    @PascalMethod(description = "library file")
    public void rename(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);

        // TODO: 07-Apr-17
    }

    /**
     * erase file
     */
    @PascalMethod(description = "library file")
    public void erase(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        if (filesMap.get(fileVariable.get().getPath()).isOpened()) {
            throw new FileNotAssignException(fileVariable.get().getPath());
        }
        boolean delete = fileVariable.get().delete();
    }

    /**
     * open file, clear file for write
     *
     * @throws IOException            - can not assess file
     * @throws RuntimePascalException
     */
    @PascalMethod(description = "library file")
    public void rewrite(PascalReference<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileAssigned(fileVariable);

        filesMap.get(fileVariable.get().getPath()).rewrite();
    }

    @PascalMethod(description = "library file")
    public void rewrite(PascalReference<File> fileVariable, int size) throws IOException, RuntimePascalException {
        rewrite(fileVariable);
    }


    /**
     * 102 File not assigned
     * This is reported by Reset, Rewrite, Append, Rename and Erase,
     * if you call them with an unassigned file as a parameter.
     */
    private void assertFileAssigned(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable.get());
    }

    private void assertFileAssigned(File fileVariable) throws FileNotAssignException {
        if (fileVariable == null) {
            throw new FileNotAssignException("");
        }
        if (filesMap.get(fileVariable.getPath()) == null) {
            throw new FileNotAssignException(fileVariable.getPath());
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
    private void assertFileOpened(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpened(fileVariable.get());
    }

    private void assertFileOpened(File fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        if (!filesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenException(fileVariable.getPath());
        }
    }

    /**
     * close file
     */
    @PascalMethod(description = "library file")
    public void close(PascalReference<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileOpened(fileVariable);
        filesMap.get(fileVariable.get().getPath()).close();
    }

    @PascalMethod(description = "library file")
    public void closeFile(PascalReference<File> fileVariable) throws IOException, RuntimePascalException {
        close(fileVariable);
    }

    /**
     * check end of file
     *
     * @param fileVariable
     * @return <code>true</code> if the cursor position in the end of the fileVariable, otherwise
     * return <code>false</code>
     * @throws RuntimePascalException - file not open for output
     */
    @PascalMethod(description = "Check for end of file")
    public boolean eof(PascalReference<File> fileVariable) throws IOException,
            RuntimePascalException {
        assertFileOpened(fileVariable);
        assertFileOpenForInput(fileVariable);
        return filesMap.get(fileVariable.get().getPath()).isEof();
    }

    @PascalMethod(description = "Check for end of line")
    public boolean eoln(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpened(fileVariable);
        assertFileOpenForInput(fileVariable);
        return filesMap.get(fileVariable.get().getPath()).isEndOfLine();
    }

    private void seekEof(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    private void seekEofLn(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    private void blockRead(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    @PascalMethod(description = "library file")
    public void append(PascalReference<File> fileVariable) throws RuntimePascalException,
            IOException {
        assertFileAssigned(fileVariable);
        filesMap.get(fileVariable.get().getPath()).append();
    }

    @SuppressWarnings("unchecked")
    public void setValueForVariables(File fileVariable, PascalReference... listVariable)
            throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
        for (PascalReference out : listVariable) {
            Object v = getValueForVariable(fileVariable, out.get().getClass(), out.get());
            out.set(v);
        }
    }

    private Object getValueForVariable(@NonNull File zfile, @NonNull Class c, @Nullable Object o)
            throws RuntimePascalException {
        FileEntry file = filesMap.get(zfile.getPath());
        if (c == Character.class) {
            char value = file.readChar();
            return value;
        } else if (c == StringBuilder.class) {
            String value = file.readString();
            filesMap.get(zfile.getPath()).nextLine();
            return new StringBuilder(value);

        } else if (c == String.class) {
            String value = file.readString();
            filesMap.get(zfile.getPath()).nextLine();
            return value;

        } else if (c == Integer.class) {
            Integer integer = file.readInteger();
            return integer;

        } else if (c == Long.class) {
            long value = file.readLong();
            return value;

        } else if (c == Double.class) {
            double value = file.readDouble();
            return value;

        } else if (c == CustomVariable.class) {
            CustomVariable record = (CustomVariable) o;
            ArrayList<VariableDeclaration> variables = record.getVariables();
            for (VariableDeclaration variable : variables) {
                Object v = getValueForVariable(zfile, variable.getType().getStorageClass(),
                        record.getVar(variable.getName()));
                record.setVar(variable.getName(), v);
            }
            return record;
        } else {
            throw new CanNotReadVariableException(c);
        }
    }
   /* *//**
     * move cursor to next line
     *//*
    @PascalMethod(description = "library file")
    public void read(File fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> out)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, out);
    }

    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2);
    }

    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                     PascalReference<Object> o3)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3);
    }

    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                     PascalReference<Object> o3, PascalReference<Object> o4)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4);
    }


    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                     PascalReference<Object> o3, PascalReference<Object> o4, PascalReference<Object> o5)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5);
    }


    @PascalMethod(description = "library file")
    public void read(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                     PascalReference<Object> o3, PascalReference<Object> o4, PascalReference<Object> o5,
                     PascalReference<Object> o6)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5, o6);
    }*/


    /**
     * move cursor to next line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
        filesMap.get(fileVariable.getPath()).nextLine();
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> out)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, out);
        if (!(out.get() instanceof StringBuilder)
                && !(out.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2);
        if (!(o2.get() instanceof StringBuilder)
                && !(o2.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }


    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                       PascalReference<Object> o3)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3);
        if (!(o3.get() instanceof StringBuilder)
                && !(o3.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                       PascalReference<Object> o3, PascalReference<Object> o4)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4);
        if (!(o4.get() instanceof StringBuilder)
                && !(o4.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                       PascalReference<Object> o3, PascalReference<Object> o4, PascalReference<Object> o5)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5);
        if (!(o5.get() instanceof StringBuilder)
                && !(o5.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file")
    public void readln(File fileVariable, PascalReference<Object> o1, PascalReference<Object> o2,
                       PascalReference<Object> o3, PascalReference<Object> o4, PascalReference<Object> o5,
                       PascalReference<Object> o6)
            throws IOException, RuntimePascalException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5, o6);
        if (!(o6.get() instanceof StringBuilder)
                && !(o6.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * 04 File not open for input
     * Reported by Read, BlockRead, Eof, Eoln, SeekEof or SeekEoln if the file is not opened with Reset.
     */
    private void assertFileOpenForInput(PascalReference<File> fileVariable)
            throws RuntimePascalException {
        assertFileOpenForInput(fileVariable.get());
    }

    private void assertFileOpenForInput(File fileVariable)
            throws RuntimePascalException {
        assertFileOpened(fileVariable);
        if (!filesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenForInputException(fileVariable.getPath());
        }
    }

    /**
     * writeS file
     */
    public void writeFile(File fileVariable, Object... objects) throws RuntimePascalException {
        //check error
        assertFileOpened(fileVariable);
        FileEntry file = filesMap.get(fileVariable.getPath());
        file.writeString(objects);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")
    public void shutdown() {
        for (Map.Entry<String, FileEntry> entry : filesMap.entrySet()) {
            try {
                filesMap.get(entry.getKey()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            filesMap.remove(entry.getKey());
        }
    }

    @Override
    public void declareConstants(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareTypes(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareVariables(ExpressionContextMixin parentContext) {

    }

    @Override
    public void declareFunctions(ExpressionContextMixin parentContext) {

    }

    public void readz(File file, PascalReference[] values) throws RuntimePascalException {
        setValueForVariables(file, values);
    }

    /**
     * read file and  move cursor to new line
     */
    public void readlnz(File fileVariable, PascalReference... args)
            throws RuntimePascalException {
        if (args.length == 0) {
            assertFileOpenForInput(fileVariable);
            filesMap.get(fileVariable.getPath()).nextLine();
        } else {
            setValueForVariables(fileVariable, args);
            if (!(args[args.length - 1].get() instanceof StringBuilder)
                    && !(args[args.length - 1].get() instanceof String)) {
                filesMap.get(fileVariable.getPath()).nextLine();
            }
        }
    }
}
