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

import com.duy.pascal.backend.builtin_libraries.file.exceptions.DiskReadErrorException;
import com.duy.pascal.backend.builtin_libraries.file.exceptions.FileNotOpenException;
import com.duy.pascal.backend.runtime_exception.InvalidNumericFormatException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

class FileEntry {
    private static final String TAG = "FileEntry";
    // A pattern for java whitespace
    private static Pattern WHITESPACE_PATTERN = Pattern.compile(
            "\\p{javaWhitespace}+");
    private String mFilePath = "";
    private BufferedWriter mWriter;
    private Scanner fileScanner;
    private Scanner lineScanner;
    private boolean opened = false;
    private File file;


    FileEntry(String filePath) {
        this.mFilePath = filePath;
        this.file = new File(this.mFilePath);
    }

    public FileEntry(File file) {
        this.mFilePath = file.getPath();
        this.file = file;
    }

    public String getFileName(String fileName) {
        return mFilePath;
    }

    public void setFileName(String filePath) {
        this.mFilePath = filePath;
    }

    /**
     * open file
     *
     * @throws FileNotFoundException
     */
    public synchronized void reset() throws FileNotFoundException {
        fileScanner = new Scanner(new FileReader(mFilePath));

        //uses dot symbol for floating number
        fileScanner.useLocale(Locale.ENGLISH);

        setOpened(true);
    }

    public synchronized void append() throws IOException {
        File f = new File(mFilePath);
        if (!f.exists()) {
            f.createNewFile();
        }
        mWriter = new BufferedWriter(new FileWriter(f));
        setOpened(true);
    }

    /**
     * prepare file to writer
     * set file empty
     *
     * @throws IOException
     */
    public synchronized void rewrite() throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.setLength(0);
        randomAccessFile.close();
        mWriter = new BufferedWriter(new FileWriter(file));
        setOpened(true);
    }

    public synchronized int readInteger() throws InvalidNumericFormatException, DiskReadErrorException {
        checkEndOfLine();
        int integer;
        try {
            integer = lineScanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
        return integer;
    }

    /**
     * Check whether the end of a lineInfo or not? If the end of the lineInfo,
     * check if the end of the file or not?, if end of file then give an error,
     * else re-create lineScanner
     *
     * @throws DiskReadErrorException - End of file
     */
    public synchronized void checkEndOfLine() throws DiskReadErrorException {
        try {
            if (lineScanner != null && lineScanner.hasNext()) {
                return;
            }
        } catch (Exception e) {
            //if lineScanner is closed or no data
        }
        assertNotEndOfFile();
        closeLine();
        String source = fileScanner.nextLine();
        lineScanner = new Scanner(source);
        lineScanner.useLocale(Locale.ENGLISH);
    }

    private void closeLine() {
        try {
            if (lineScanner != null) {
                lineScanner.close();
            }
        } catch (Exception ignored) {
        }

    }

    public synchronized long readLong() throws InvalidNumericFormatException,
            DiskReadErrorException {
        checkEndOfLine();
        long l;
        try {
            l = lineScanner.nextLong();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
        return l;
    }

    public synchronized double readDouble() throws InvalidNumericFormatException,
            DiskReadErrorException {
        checkEndOfLine();
        double d;
        try {
            d = lineScanner.nextDouble();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
        return d;
    }

    public synchronized String readString() throws DiskReadErrorException {
        checkEndOfLine();
        return lineScanner.nextLine();
    }

    public char readChar() throws DiskReadErrorException {
        checkEndOfLine();
       /* int c = 0;
        if (lineReader != null) {
            c = lineReader.read();
            char[] chars = new char[1];
            lineReader.read(chars);
            if (c != 0) {
                return (char) c;
            } else {
                throw new DiskReadErrorException(mFilePath);
            }
        }
        return 0;*/
        return lineScanner.findInLine(".").charAt(0);
    }

    /**
     * An error occurred when reading from disk. Typically happens when you try to
     * read past the end of a file.
     *
     * @throws DiskReadErrorException
     */
    private void assertNotEndOfFile() throws DiskReadErrorException {
        if (!fileScanner.hasNext()) {
            throw new DiskReadErrorException(mFilePath);
        }
    }

    public synchronized void writeString(Object[] objects) throws IOException {
        for (Object o : objects) {
            mWriter.write(o.toString());
        }
    }

    /**
     * close file
     */
    public synchronized void close() throws IOException {
        if (fileScanner != null) {
            fileScanner.close();
            if (lineScanner != null) {
                lineScanner.close();
            }
        }
        try {
            if (mWriter != null) {
                mWriter.flush();
                mWriter.close();
            }
        } catch (IOException ignored) {
        }
        setOpened(false);
    }

    public boolean isEof() {
        return !fileScanner.hasNext();
    }

    public synchronized void nextLine() {
        if (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            if (lineScanner != null) {
                lineScanner.close();
            }
            lineScanner = new Scanner(line);
            lineScanner.useLocale(Locale.ENGLISH);
        }
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    private void assertFileOpen() throws FileNotOpenException {
        if (!isOpened()) {
            throw new FileNotOpenException(mFilePath);
        }
    }

    public boolean isEndOfLine() throws DiskReadErrorException {
        try {
            if (lineScanner != null && lineScanner.hasNext()) {
                return false;
            }
        } catch (Exception e) {
            //if lineScanner is closed or no data
        }
        return isEof();
    }
}