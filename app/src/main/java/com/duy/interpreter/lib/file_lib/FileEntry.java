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

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    private RandomAccessFile randomAccessFile;
    private String name = "";

    public FileEntry(RandomAccessFile randomAccessFile, String name) {
        this.randomAccessFile = randomAccessFile;
        this.name = name;
    }

    public FileEntry(String name) {
        this.name = name;
    }

    public void reset() throws FileNotFoundException {
        File f = new File(name);
        randomAccessFile = new RandomAccessFile(f, "r");
    }

    public void rewrite() throws FileNotFoundException {
        File f = new File(name);
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