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

package com.duy.pascal.backend.lib.file;

import com.duy.pascal.backend.lib.PascalLibrary;
import com.duy.pascal.backend.lib.annotations.PascalMethod;
import com.duy.pascal.backend.lib.file.exceptions.FileNotAssignException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.backend.lib.runtime_exceptions.CanNotReadVariableException;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;
import com.js.interpreter.runtime.exception.WrongArgsException;

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

    /**
     * assign file,
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void assign(VariableBoxer<File> fileVariable, String name) throws RuntimePascalException {
        String path = ApplicationFileManager.getApplicationPath() + name;
        fileVariable.set(new File(path));

        //put to map
        FileEntry fileEntry = new FileEntry(name);
        filesMap.put(path, fileEntry);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void AssignFile(VariableBoxer<File> fileVariable, String name) throws RuntimePascalException {
        assign(fileVariable, name);
    }

    /**
     * open file for read
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void reset(VariableBoxer<File> fileVariable) throws
            FileNotFoundException, RuntimePascalException {
        assertFileAssigned(fileVariable);
        //throw file not found exception
        filesMap.get(fileVariable.get().getPath()).reset();
    }

    /**
     * rename file
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void rename(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);

        // TODO: 07-Apr-17
    }

    /**
     * erase file
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void erase(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileAssigned(fileVariable);
        // TODO: 07-Apr-17
    }

    /**
     * open file, clear file for write
     *
     * @throws IOException            - can not assess file
     * @throws RuntimePascalException
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void rewrite(VariableBoxer<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileAssigned(fileVariable);

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
    private void assertFileOpened(VariableBoxer<File> fileVariable) throws RuntimePascalException {
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
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void close(VariableBoxer<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileOpened(fileVariable);
        filesMap.get(fileVariable.get().getPath()).close();
        filesMap.remove(fileVariable.get().getPath());
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void closeFile(VariableBoxer<File> fileVariable) throws IOException, RuntimePascalException {
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
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public boolean eof(VariableBoxer<File> fileVariable) throws IOException,
            RuntimePascalException {
        assertFileOpened(fileVariable);
        assertFileOpenForInput(fileVariable);
        return filesMap.get(fileVariable.get().getPath()).isEof();
    }

    private void seekEof(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    private void seekEofLn(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    private void blockRead(VariableBoxer<File> fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void append(VariableBoxer<File> fileVariable) throws RuntimePascalException,
            IOException {
        assertFileAssigned(fileVariable);
        filesMap.get(fileVariable.get().getPath()).append();
    }

    @SafeVarargs
    private final void setValueForVariables(File fileVariable, VariableBoxer<Object>... listVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
        FileEntry file = filesMap.get(fileVariable.getPath());
        for (VariableBoxer<Object> out : listVariable) {
            if (out.get() instanceof Character) {
                char value = file.readChar();
                out.set(value);
            } else if (out.get() instanceof StringBuilder) {
                String value = file.readString();
                out.set(new StringBuilder(value));
            } else if (out.get() instanceof String) {
                String value = file.readString();
                out.set(value);
            } else if (out.get() instanceof Integer) {
                Integer integer = file.readInteger();
                out.set(integer);
            } else if (out.get() instanceof Long) {
                long value = file.readLong();
                out.set(value);
            } else if (out.get() instanceof Double) {
                double value = file.readDouble();
                out.set(value);
            } else {
                throw new CanNotReadVariableException(out.get());
            }
        }
    }

    /**
     * move cursor to next line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> out)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, out);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                     VariableBoxer<Object> o3)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                     VariableBoxer<Object> o3, VariableBoxer<Object> o4)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3, o4);
    }


    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                     VariableBoxer<Object> o3, VariableBoxer<Object> o4, VariableBoxer<Object> o5)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5);
    }


    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void read(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                     VariableBoxer<Object> o3, VariableBoxer<Object> o4, VariableBoxer<Object> o5,
                     VariableBoxer<Object> o6)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5, o6);
    }


    /**
     * move cursor to next line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable) throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
        filesMap.get(fileVariable.getPath()).nextLine();
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> out)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, out);
        if (!(out.get() instanceof StringBuilder)
                && !(out.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2);
        if (!(o2.get() instanceof StringBuilder)
                && !(o2.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                       VariableBoxer<Object> o3)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3);
        if (!(o3.get() instanceof StringBuilder)
                && !(o3.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                       VariableBoxer<Object> o3, VariableBoxer<Object> o4)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3, o4);
        if (!(o4.get() instanceof StringBuilder)
                && !(o4.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                       VariableBoxer<Object> o3, VariableBoxer<Object> o4, VariableBoxer<Object> o5)
            throws IOException, RuntimePascalException, WrongArgsException {
        setValueForVariables(fileVariable, o1, o2, o3, o4, o5);
        if (!(o5.get() instanceof StringBuilder)
                && !(o5.get() instanceof String)) {
            filesMap.get(fileVariable.getPath()).nextLine();
        }
    }

    /**
     * read file and  move cursor to new line
     */
    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void readln(File fileVariable, VariableBoxer<Object> o1, VariableBoxer<Object> o2,
                       VariableBoxer<Object> o3, VariableBoxer<Object> o4, VariableBoxer<Object> o5,
                       VariableBoxer<Object> o6)
            throws IOException, RuntimePascalException, WrongArgsException {
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
    private void assertFileOpenForInput(VariableBoxer<File> fileVariable)
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
    private void writeFile(File fileVariable, Object... objects) throws IOException, RuntimePascalException {
        //check error
        assertFileOpened(fileVariable);
        FileEntry file = filesMap.get(fileVariable.getPath());
        file.writeString(objects);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File file) throws IOException, RuntimePascalException {
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3, Object o4) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                      Object o6) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                      Object o6, Object o7) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6, o7);
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void write(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                      Object o6, Object o7, Object o8) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6, o7, o8);

    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File file) throws IOException, RuntimePascalException {
        writeFile(file, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3, Object o4) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                        Object o6) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                        Object o6, Object o7) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6, o7);
        writeFile(fileVariable, "\n");
    }

    @PascalMethod(description = "library file", returns = "null")
    @SuppressWarnings("unused")
    public void writeln(File fileVariable, Object o1, Object o2, Object o3, Object o4, Object o5,
                        Object o6, Object o7, Object o8) throws
            IOException, RuntimePascalException {
        writeFile(fileVariable, o1, o2, o3, o4, o5, o6, o7, o8);
        writeFile(fileVariable, "\n");
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }
}
