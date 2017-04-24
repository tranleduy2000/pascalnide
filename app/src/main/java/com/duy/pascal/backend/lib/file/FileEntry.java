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

import android.os.Environment;

import com.duy.pascal.backend.lib.file.exceptions.DiskReadErrorException;
import com.duy.pascal.backend.lib.file.exceptions.FileNotOpenException;
import com.js.interpreter.runtime.exception.InvalidNumericFormatException;

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

class FileEntry {
    private final Object lock = new Object();
    private String mFilePath = "";
    private BufferedWriter mWriter;
    private String bufferReader = "";
    private Scanner scanner;
    private boolean opened = false;
    private File file;


    FileEntry(String mFilePath) {
        this.mFilePath = Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/" + mFilePath;
        this.file = new File(this.mFilePath);
//        System.out.println("File: " + this.mFilePath);
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
    public void reset() throws FileNotFoundException {
        scanner = new Scanner(new FileReader(mFilePath));
        scanner.useLocale(Locale.ENGLISH);
        setOpened(true);
    }

    public void append() throws IOException {
        File f = new File(mFilePath);
        if (!f.exists()) {
            f.createNewFile();
        }
        mWriter = new BufferedWriter(new FileWriter(f));
    }

    /**
     * prepare file to writer
     * set file empty
     *
     * @throws IOException
     */
    public void rewrite() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        randomAccessFile.setLength(0);
        randomAccessFile.close();
        mWriter = new BufferedWriter(new FileWriter(file));
        setOpened(true);
    }

    public int readInteger() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        int integer;
        try {
            integer = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
//        System.out.println("readln integer " + integer);
        return integer;
    }

    public long readLong() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        long l;
        try {
            l = scanner.nextLong();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
        return l;
    }

    public double readDouble() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        double d;
        try {
            d = scanner.nextDouble();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException("read file");
        }
//        System.out.println(d);
        return d;
    }

    public String readString() throws DiskReadErrorException {
        assertNotEndOfFile();
        String res = scanner.nextLine();
//        System.out.println(res);
        return res;
    }

    public char readChar() throws DiskReadErrorException {
        assertNotEndOfFile();
        return scanner.next().charAt(0);
    }

    /**
     * An error occurred when reading from disk. Typically happens when you try to
     * read past the end of a file.
     *
     * @throws DiskReadErrorException
     */
    private void assertNotEndOfFile() throws DiskReadErrorException {
        if (!scanner.hasNext()) {
            throw new DiskReadErrorException(mFilePath);
        }
    }

    public synchronized void writeString(Object[] objects) throws IOException {
        synchronized (lock) {
            for (Object o : objects) {
                mWriter.write(o.toString());
            }
        }
    }

    /**
     * close file
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (scanner != null) {
            scanner.close();
        }
        if (mWriter != null) {
            mWriter.close();
        }
        setOpened(false);
    }


    public boolean isEof() {
        return scanner.hasNext();
    }

    public void nextLine() {
        if (scanner.hasNext())
            scanner.nextLine();
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

    public void isEndOfLine() {
    }
}