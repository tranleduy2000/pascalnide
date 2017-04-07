package com.duy.pascal.backend.lib.file_lib;

import android.os.Environment;

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

public class FileEntry {
    private String filePath = "";
    private BufferedWriter mWriter;
    private Scanner mReader;
    private boolean opened = false;
    private File file;

    public FileEntry(String filePath) {
        this.filePath = Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/" + filePath;
        this.file = new File(filePath);
//        System.out.println("File: " + this.filePath);
    }


    public String getFileName(String fileName) {
        return filePath;
    }

    public void setFileName(String filePath) {
        this.filePath = filePath;
    }

    /**
     * open file
     *
     * @throws IOException
     */
    public void reset() throws FileNotFoundException {
        mReader = new Scanner(new FileReader(filePath));
        mReader.useLocale(Locale.ENGLISH);
        setOpened(true);
    }

    public void append() throws IOException {
        File f = new File(filePath);
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

    }

    public int readInt() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        int integer;
        try {
            integer = mReader.nextInt();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException();
        }
//        System.out.println("readln integer " + integer);
        return integer;
    }

    public long readLong() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        long l;
        try {
            l = mReader.nextLong();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException();
        }
//        System.out.println("readLong " + l);
        return l;
    }

    public double readDouble() throws InvalidNumericFormatException, DiskReadErrorException {
        assertNotEndOfFile();

        double d;
        try {
            d = mReader.nextDouble();
        } catch (InputMismatchException e) {
            throw new InvalidNumericFormatException();
        }
//        System.out.println(d);
        return d;
    }

    public String readString() throws DiskReadErrorException {
        assertNotEndOfFile();
        String res = mReader.nextLine();
//        System.out.println(res);
        return res;
    }

    public char readChar() throws DiskReadErrorException {
        assertNotEndOfFile();
        return mReader.next().charAt(0);
    }

    /**
     * An error occurred when reading from disk. Typically happens when you try to
     * read past the end of a file.
     * @throws DiskReadErrorException
     */
    private void assertNotEndOfFile() throws DiskReadErrorException {
        if (!mReader.hasNext()) {
            throw new DiskReadErrorException();
        }
    }

    public void write(Object[] objects) throws IOException {
        for (Object o : objects) {
            mWriter.write(o.toString());
        }
    }

    /**
     * close file
     *
     * @throws IOException
     */
    public void close() throws IOException {
        if (mReader != null) {
            mReader.close();
        }
        if (mWriter != null) {
            mWriter.close();
        }
    }


    public boolean isEof() {
        return mReader.hasNext();
    }

    public void nextLine() {
        mReader.nextLine();
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    private void assertFileOpen() throws FileNotOpenException {
        if (!isOpened()) {
            throw new FileNotOpenException();
        }
    }
}