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

package com.duy.pascal.interperter.libraries.file;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.ui.utils.DLog;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.interperter.ast.runtime.references.PascalReference;
import com.duy.pascal.interperter.ast.runtime.value.RecordValue;
import com.duy.pascal.interperter.libraries.PascalLibrary;
import com.duy.pascal.interperter.libraries.annotations.PascalMethod;
import com.duy.pascal.interperter.libraries.exceptions.CanNotReadVariableException;
import com.duy.pascal.interperter.libraries.file.exceptions.FileNotAssignException;
import com.duy.pascal.interperter.libraries.file.exceptions.FileNotOpenException;
import com.duy.pascal.interperter.libraries.file.exceptions.FileNotOpenForInputException;
import com.duy.pascal.interperter.libraries.io.InOutListener;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class FileLib extends PascalLibrary {
    private static final String TAG = "FileLib";
    /**
     * map file
     * key is the path of file
     */
    private HashMap<String, FileEntry> mFilesMap = new HashMap<>();
    private InOutListener inOutListener;

    public FileLib() {

    }

    public FileLib(InOutListener handler) {
        this.inOutListener = handler;
    }

    /**
     * assign file,
     */
    @PascalMethod(description = "library file")
    public void assign(PascalReference<File> fileVariable, StringBuilder name) throws RuntimePascalException {
        DLog.d(TAG, "assign() called with: fileVariable = [" + fileVariable + "], name = [" + name + "]");
        File file = new File(name.toString());
        if (!file.exists()) {
            file = new File(inOutListener.getCurrentDirectory(), name.toString());
        }
        DLog.d("File " + file);
        fileVariable.set(file);

        //put to map
        FileEntry fileEntry = new FileEntry(file);
        mFilesMap.put(file.getPath(), fileEntry);
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
        mFilesMap.get(fileVariable.get().getPath()).reset();
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
        if (mFilesMap.get(fileVariable.get().getPath()).isOpened()) {
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

        mFilesMap.get(fileVariable.get().getPath()).rewrite();
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
        if (mFilesMap.get(fileVariable.getPath()) == null) {
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
        if (!mFilesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenException(fileVariable.getPath());
        }
    }

    /**
     * close file
     */
    @PascalMethod(description = "library file")
    public void close(PascalReference<File> fileVariable) throws IOException, RuntimePascalException {
        assertFileOpened(fileVariable);
        mFilesMap.get(fileVariable.get().getPath()).close();
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
        return mFilesMap.get(fileVariable.get().getPath()).isEof();
    }

    @PascalMethod(description = "Check for end of line")
    public boolean eoln(PascalReference<File> fileVariable) throws RuntimePascalException {
        assertFileOpened(fileVariable);
        assertFileOpenForInput(fileVariable);
        return mFilesMap.get(fileVariable.get().getPath()).isEndOfLine();
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
        mFilesMap.get(fileVariable.get().getPath()).append();
    }

    @SuppressWarnings("unchecked")
    private void setValueForVariables(File fileVariable, PascalReference... listVariable)
            throws RuntimePascalException {
        assertFileOpenForInput(fileVariable);
        for (PascalReference out : listVariable) {
            Object v = getValueForVariable(fileVariable, out.get().getClass(), out.get());
            out.set(v);
        }
    }

    private Object getValueForVariable(@NonNull File zfile, @NonNull Class c, @Nullable Object o)
            throws RuntimePascalException {
        FileEntry file = mFilesMap.get(zfile.getPath());
        if (c == Character.class) {
            return file.readChar();
        } else if (c == StringBuilder.class) {
            String value = file.readString();
            mFilesMap.get(zfile.getPath()).nextLine();
            return new StringBuilder(value);

        } else if (c == String.class) {
            String value = file.readString();
            mFilesMap.get(zfile.getPath()).nextLine();
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

        } else if (c == RecordValue.class) {
            RecordValue record = (RecordValue) o;
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
        if (!mFilesMap.get(fileVariable.getPath()).isOpened()) {
            throw new FileNotOpenForInputException(fileVariable.getPath());
        }
    }

    /**
     * writeS file
     */
    public void writeFile(File fileVariable, Object... objects) throws RuntimePascalException {
        //check error
        assertFileOpened(fileVariable);
        FileEntry file = mFilesMap.get(fileVariable.getPath());
        file.writeString(objects);
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    @Override
    @PascalMethod(description = "stop")
    public void shutdown() {
        for (Map.Entry<String, FileEntry> entry : mFilesMap.entrySet()) {
            try {
                mFilesMap.get(entry.getKey()).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mFilesMap.remove(entry.getKey());
        }
    }

    @Override
    public String getName() {
        return null;
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
            mFilesMap.get(fileVariable.getPath()).nextLine();
        } else {
            setValueForVariables(fileVariable, args);
            if (!(args[args.length - 1].get() instanceof StringBuilder)
                    && !(args[args.length - 1].get() instanceof String)) {
                mFilesMap.get(fileVariable.getPath()).nextLine();
            }
        }
    }
}
