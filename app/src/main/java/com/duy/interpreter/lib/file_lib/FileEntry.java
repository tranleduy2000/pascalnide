package com.duy.interpreter.lib.file_lib;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileEntry {
    private RandomAccessFile randomAccessFile;
    private String filePath = "";

    public FileEntry(RandomAccessFile randomAccessFile, String filePath) {
        this.randomAccessFile = randomAccessFile;
        this.filePath = filePath;
    }

    public FileEntry(String filePath) {
        this.filePath = Environment.getExternalStorageDirectory().getPath()
                + "/PascalCompiler/" + filePath;
        System.out.println("File: " + this.filePath);
    }

    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public String getFileName(String fileName) {
        return filePath;
    }

    public void setFileName(String filePath) {
        this.filePath = filePath;
    }

    public void reset() throws IOException {
        File f = new File(filePath);
        randomAccessFile = new RandomAccessFile(f, "r");
    }

    public void rewrite() throws IOException {
        File f = new File(filePath);
        randomAccessFile = new RandomAccessFile(f, "w");
    }

    public int readInt() throws IOException {
        return randomAccessFile.readInt();
    }

    public long readLong() throws IOException {
        return randomAccessFile.readLong();
    }

    public double readDouble() throws IOException {
        return randomAccessFile.readDouble();
    }

    public String readString() throws IOException {
        return randomAccessFile.readLine();
    }

    public char readChar() throws IOException {
        return randomAccessFile.readChar();
    }

    public void write(String object) throws IOException {
        randomAccessFile.writeUTF(object);
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }


    public boolean isEof() {
        try {
            return randomAccessFile.getFilePointer() >= randomAccessFile.length();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void nextLine() {
        try {
            randomAccessFile.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}