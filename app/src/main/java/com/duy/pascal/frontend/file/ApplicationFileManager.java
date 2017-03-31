package com.duy.pascal.frontend.file;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.duy.pascal.frontend.setting.PascalPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * File Manager
 * Created by Duy on 10-Feb-17.
 */

public class ApplicationFileManager {
    /**
     * storage path for saveFile code
     */
    private static final String EXTERNAL_DIR_CODE = Environment.getExternalStorageDirectory().getPath()
            + "/PascalCompiler/";
    private final String TAG = ApplicationFileManager.class.getSimpleName();
    private final String FILE_TEMP_NAME = "tmp.pas";
    private int mode = SAVE_MODE.EXTERNAL;
    private Context context;
    private Database mDatabase;
    private PascalPreferences mPascalPreferences;

    public ApplicationFileManager(Context context) {
        this.context = context;
        mDatabase = new Database(context);
        mPascalPreferences = new PascalPreferences(context);
    }

    /**
     * @return path of application
     */
    public static String getApplicationPath() {
        File file = new File(EXTERNAL_DIR_CODE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return EXTERNAL_DIR_CODE;
    }

    /**
     * get path from uri
     *
     * @param context
     * @param uri
     * @return
     * @throws URISyntaxException
     */
    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getLineError(int position, String mFileName) {
        File file = new File(mFileName);
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                int index = 0;
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    index += line.length();
                    if (index >= position) break;
                }
                if (index >= position) {
                    return line;
                } else
                    return "";
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return "";
    }

    public boolean createDirectory() {
        File dir = new File(EXTERNAL_DIR_CODE);
        if (dir.exists()) {
            if (dir.isDirectory()) return true;
            if (!dir.delete()) return false;
        }
        return dir.mkdir();
    }

    public String readFileAsString(String name) {
        File file = new File(name);
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line + "\n";
                }
                return result;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return "";
    }

    public String readFileAsString(File file) {
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line + "\n";
                }
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return "";
    }

    /**
     * get all file in folder
     *
     * @param path - folder path
     * @return list file
     */
    public ArrayList<File> getListFile(String path) {
        ArrayList<File> list = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        try {
            if (files == null) return list;
            for (File file : files) {
                if (file.isFile()) list.add(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<File> listFile = mDatabase.getListFile();
        list.addAll(listFile);
        return list;
    }

    /**
     * get all file with filter
     *
     * @param path      - folder path
     * @param extension - extension of file
     * @return - list file
     */
    public ArrayList<File> getListFile(String path, String extension) {
        ArrayList<File> list = new ArrayList<>();
        File f = new File(path);
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            int ind = files[i].getPath().lastIndexOf('.');
            if (ind > 0) {
                String tmp = files[i].getPath().substring(ind + 1);// this is the extension
                if (tmp.equals(extension)) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * saveFile current project
     *
     * @param file
     */
    public void saveFile(File file, String text) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (text.length() > 0) out.write(text.getBytes());
            out.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * saveFile file
     *
     * @param filePath - name of file
     * @param text     - content of file
     */
    public boolean saveFile(String filePath, String text) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            if (text.length() > 0) writer.write(text);
            writer.close();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public String loadInMode(File file) {
        String res = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * read content of file
     *
     * @param filename - file
     * @return - string
     */
    public String loadInMode(String filename) {
        String res = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File(getCurrentPath() + filename)), "UTF8"));

            String line;
            while ((line = in.readLine()) != null) {
                res += line + "\n";
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * create new file in dir of application
     *
     * @param fileName
     * @return - path of file
     */
    public String createNewFileInMode(String fileName) {
        String name = getCurrentPath() + fileName;
        File file = new File(name);
//        Log.i(TAG, "createNewFileInMode: " + name);
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            return file.getPath();
        } catch (IOException e) {
//            Log.e("", "Could not create file.", e);
            return "";
        }
    }

    /**
     * create new file
     *
     * @param path path to file
     * @return
     */
    public String createNewFile(String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            return path;
        } catch (IOException e) {
            return "";
        }
    }

    public boolean deleteFile(File file) {
        try {
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public String removeFile(File file) {
        try {
            file.delete();
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * set content of file pas for run, put it in internal storage
     *
     * @param content - Content of file, string
     */
    public String setContentFileTemp(String content) {
        String name = getCurrentPath(SAVE_MODE.INTERNAL) + FILE_TEMP_NAME;
//        Log.d(TAG, "setContentFileTemp: " + name);
//        Log.d(TAG, "setContentFileTemp: " + content);
        File file = new File(name);
        FileOutputStream outputStream;
        try {
            if (!file.exists()) {
                createNewFile(name);
            }
            outputStream = new FileOutputStream(new File(name));
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    /**
     * return file pas for run program
     *
     * @return - pascal file
     */
    public File getTempFile() {
        String name = getCurrentPath(SAVE_MODE.INTERNAL) + File.separatorChar + FILE_TEMP_NAME;
//        Log.d(TAG, "getTempFile: " + name);
        File file = new File(name);
        if (!file.exists()) {
            createNewFileInMode(name);
        }
        return file;
    }

    public String getCurrentPath() {
        if (mode == SAVE_MODE.INTERNAL) {
            return context.getFilesDir().getPath() + File.separatorChar;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
        }
    }

    private String getCurrentPath(int mode) {
        if (mode == SAVE_MODE.INTERNAL) {
            return context.getFilesDir().getPath() + File.separatorChar;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
        }
    }

    public void addNewPath(String path) {
        mDatabase.addNewFile(new File(path));
    }

    /**
     * set working file path
     *
     * @param path
     */
    public void setWorkingFilePath(String path) {
        mPascalPreferences.put(PascalPreferences.FILE_PATH, path);
    }

    public void removeTabFile(String path) {
        mDatabase.removeFile(path);
    }

    public static class SAVE_MODE {
        static final int INTERNAL = 1;
        static final int EXTERNAL = 2;
    }

}
