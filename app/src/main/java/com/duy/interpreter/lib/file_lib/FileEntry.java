package com.duy.interpreter.lib.file_lib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileEntry {
    public RandomAccessFile getRandomAccessFile() {
        return randomAccessFile;
    }

    public void setRandomAccessFile(RandomAccessFile randomAccessFile) {
        this.randomAccessFile = randomAccessFile;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private RandomAccessFile randomAccessFile;
    private String filePath = "";

    public FileEntry(RandomAccessFile randomAccessFile, String filePath) {
        this.randomAccessFile = randomAccessFile;
        this.filePath = filePath;
    }

    public FileEntry(String filePath) {
        this.filePath = filePath;
    }

    public void reset() throws FileNotFoundException {
        File f = new File(filePath);
        randomAccessFile = new RandomAccessFile(f, "r");
    }

    public void rewrite() throws FileNotFoundException {
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

    public int readChar() throws IOException {
        return randomAccessFile.readChar();
    }

    public void write(String object) throws IOException {
        randomAccessFile.writeUTF(object);
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }

}