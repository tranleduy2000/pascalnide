package com.duy.interpreter.lib.file_lib;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Scanner;

public class FileEntry {
    private String filePath = "";
    private BufferedWriter mWriter;
    private Scanner mReader;


    public FileEntry(String filePath) {
        this.filePath = Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/" + filePath;
        System.out.println("File: " + this.filePath);
    }


    public String getFileName(String fileName) {
        return filePath;
    }

    public void setFileName(String filePath) {
        this.filePath = filePath;
    }

    public void reset() throws IOException {
        mReader = new Scanner(new FileReader(filePath));
        mReader.useLocale(Locale.ENGLISH);
    }

    public void rewrite() throws IOException {
        File f = new File(filePath);
        if (!f.exists()) {
            f.createNewFile();
        }
        RandomAccessFile randomAccessFile     = new RandomAccessFile(f, "rw");
        randomAccessFile.setLength(0);
        randomAccessFile.close();
        mWriter = new BufferedWriter(new FileWriter(f));
    }

    public int readInt() throws IOException {
//        int integer = randomAccessFile.readInt();
        int integer = mReader.nextInt();
        System.out.println("readln integer " + integer);
        return integer;
    }

    public long readLong() throws IOException {
        long l = mReader.nextLong();
        System.out.println("readLong " + l);
        return l;
    }

    public double readDouble() throws IOException {
        double d = mReader.nextDouble();
        System.out.println(d);
        return d;
    }

    public String readString() throws IOException {
        String res = mReader.nextLine();
        System.out.println(res);
        return res;
    }

    public char readChar() throws IOException {
        Character character = mReader.next().charAt(0);
        return character;
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
}