package com.duy.interpreter.lib.file_lib;

import com.duy.interpreter.lib.PascalLibrary;
import com.js.interpreter.runtime.VariableBoxer;
import com.js.interpreter.runtime.exception.RuntimePascalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class FileLib implements PascalLibrary {

    private HashMap<Integer, FileEntry> filesMap = new HashMap<>();

    private int numberFiles = 0;

    /**
     * add new file to map file
     *
     * @param fileID - id to store address of file
     * @param name   - file path
     */
    public void assign(VariableBoxer<Integer> fileID, String name) {
        numberFiles++;
        fileID.set(numberFiles);
        FileEntry fileEntry = new FileEntry(name);
        filesMap.put(numberFiles, fileEntry);
    }

    public void rewrite(int fileID) throws IOException {
        checkFile(fileID);
        filesMap.get(fileID).rewrite();
    }

    public void reset(int fileID) throws IOException {
        checkFile(fileID);
        filesMap.get(fileID).reset();
    }

    /**
     * close file
     *
     * @param fileID
     */
    public void close(int fileID) throws IOException {
        checkFile(fileID);
        filesMap.get(fileID).close();
    }

    public boolean eof(int fileID) throws IOException {
        checkFile(fileID);
        return filesMap.get(fileID).isEof();
    }


    /**
     * read file
     *
     * @param fileID
     * @param out
     * @return
     */
    public void readf(int fileID, VariableBoxer<Object> out) throws IOException, RuntimePascalException {
        checkFile(fileID);
        FileEntry f = filesMap.get(fileID);
        if (out.get() instanceof Integer) {
            Integer integer = f.readInt();
            out.set(integer);
        } else if (out.get() instanceof Long) {
            long value = f.readLong();
            out.set(value);
        } else if (out.get() instanceof Double) {
            double value = f.readDouble();
            out.set(value);
        } else if (out.get() instanceof Character) {
            char value = f.readChar();
            out.set(value);
        } else if (out.get() instanceof String) {
            String value = f.readString();
            out.set(value);
        }
    }

    /**
     * read file and  move cursor to new line
     *
     * @param fileID
     * @param out
     */
    public void readlnF(int fileID, VariableBoxer<Object> out) throws IOException, RuntimePascalException {
        checkFile(fileID);
        FileEntry f = filesMap.get(fileID);
        if (out.get() instanceof Integer) {
            Integer integer = f.readInt();
            out.set(integer);
            f.nextLine();
        } else if (out.get() instanceof Long) {
            long value = f.readLong();
            out.set(value);
            f.nextLine();
        } else if (out.get() instanceof Double) {
            double value = f.readDouble();
            f.nextLine();
            out.set(value);
        } else if (out.get() instanceof Character) {
            char value = f.readChar();
            f.nextLine();
            out.set(value);
        } else if (out.get() instanceof String) {
            String value = f.readString();
            out.set(value);
        }
    }

    /**
     * write file
     *
     * @param fileID
     * @param objects
     * @return
     */
    public boolean writeF(int fileID, Object... objects) {
        RandomAccessFile f = filesMap.get(fileID).getRandomAccessFile();
        try {
            for (Object o : objects) {
                f.writeUTF(o.toString());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * write file and append new line
     *
     * @param fileID
     * @param objects
     */
    public void writelnF(int fileID, Object... objects) {
        writeF(fileID, objects);
        writeF(fileID, "\n");
    }

    @Override
    public boolean instantiate(Map<String, Object> pluginargs) {
        return false;
    }

    /////////////////////////////////////////////////
    //////////// NON_FUNCTION_PASCAL ////////////////
    /////////////////////////////////////////////////

    public boolean fileExists(String s) {
        File f = new File(s);
        return f.exists() && !f.isDirectory();
    }

    public boolean directoryExists(String DirectoryName) {
        File f = new File(DirectoryName);
        return f.exists() && f.isDirectory();
    }

    private void checkFile(int fileID) throws IOException {
        if (filesMap.get(fileID) == null) {
            throw new IOException("File not found");
        }
    }

    public String extractFileName(String name) {
        return new File(name).getName();
    }

    public String extractPathName(String name) {
        return new File(name).getParent();
    }

    public String extractFileExt(String name) {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public String MD5FromFile(String filename) {
        MessageDigest digest;
        DigestInputStream is = null;
        try {
            try {
                digest = MessageDigest.getInstance("MD5");
                try {
                    is = new DigestInputStream(new FileInputStream(filename), digest);
                    while (is.available() > 0) {
                        is.read();
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new BigInteger(1, digest.digest()).toString(16);
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("Some sort of error trying to md5sum a file");
        return null;
    }

    public long getFileSize(int fileID) {
        RandomAccessFile f = filesMap.get(fileID).getRandomAccessFile();
        try {
            return f.length();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


}
