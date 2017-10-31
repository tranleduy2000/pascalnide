/*
 *  Copyright (c) 2017 Tran Le Duy
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

package com.duy.pascal.ui.file;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.duy.pascal.ui.R;
import com.duy.pascal.ui.activities.ActivitySplashScreen;
import com.duy.pascal.ui.code.CompileManager;
import com.duy.pascal.ui.file.localdata.Database;
import com.duy.pascal.ui.setting.PascalPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * File Manager
 * Created by Duy on 10-Feb-17.
 */

public class FileManager {
    /*storage path for save code*/
    private static final String EXTERNAL_DIR_CODE = Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
    private static final String FILE_TEMP_NAME = "tmp.pas";

    private Context mContext;
    private Database mDatabase;
    private PascalPreferences mPascalPreferences;

    public FileManager(Context context) {
        mContext = context;
        mDatabase = new Database(context);
        mPascalPreferences = new PascalPreferences(context);
    }


    /**
     * Read input stream
     */
    public static StringBuilder streamToString(InputStream inputStream) {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                result.append(mLine).append("\n");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return result;
    }

    @Nullable
    public static File getSrcPath(Context context) {
        final String dirName = "PascalCompiler";
        int i = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            i = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (i == PackageManager.PERMISSION_GRANTED) {
                File file = new File(Environment.getExternalStorageDirectory(), dirName);
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file;
            }
        } else {
            return new File(context.getFilesDir(), dirName);
        }
        return null;
    }

    /**
     * get path from uri
     */
    @Nullable
    public static String getPathFromUri(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor;
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

    public StringBuilder fileToString(String path) {
        File file = new File(path);
        if (file.canRead()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder;
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
        return new StringBuilder();
    }

    public String fileToString(File file) {
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
        for (File file : files) {
            int ind = file.getPath().lastIndexOf('.');
            if (ind > 0) {
                String tmp = file.getPath().substring(ind + 1);// this is the extension
                if (tmp.equals(extension)) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * save current project
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
     * Save file
     *
     * @param filePath - name of file
     * @param text     - content of file
     */
    public boolean saveFile(@NonNull String filePath, String text) {
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
            if (text.length() > 0) writer.write(text);
            writer.close();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * create new file in dir of application
     *
     * @param fileName - name of file to create
     * @return - path of file
     */
    public File createNewFileInMode(String fileName) {
        File file = new File(getCurrentPath(), fileName);
        return createNewFile(file.getPath());
    }

    /**
     * Create new file
     *
     * @param path path to file
     * @return file path
     */
    @Nullable
    public File createNewFile(@NonNull String path) {
        File file = new File(path);
        try {
            if (!file.exists()) {
                if (file.getParentFile().mkdirs()) {
                    return null;
                }
                if (file.createNewFile()) {
                    return null;
                }
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * set content of file pas for generate, put it in internal storage
     *
     * @param content - Content of file, string
     */
    @Nullable
    public String setContentFileTemp(String content) {
        File file = new File(getCurrentPath(), FILE_TEMP_NAME);
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file.getPath();
    }

    /**
     * return file pas for run program
     *
     * @return - pascal file
     */
    public File getTempFile() {
        String name = getCurrentPath() + File.separatorChar + FILE_TEMP_NAME;
        File file = new File(name);
        if (!file.exists()) {
            return createNewFileInMode(name);
        }
        return file;
    }

    private String getCurrentPath() {
        if (!permissionGranted()) {
            return mContext.getFilesDir().getPath() + File.separatorChar;
        } else {
            return Environment.getExternalStorageDirectory().getPath() + "/PascalCompiler/";
        }
    }

    private boolean permissionGranted() {
        return ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void addNewPath(String path) {
        mDatabase.addNewFile(new File(path));
    }

    /**
     * set working file path
     */
    public void setWorkingFilePath(String path) {
        mPascalPreferences.put(PascalPreferences.FILE_PATH, path);
    }

    public void removeTabFile(String path) {
        mDatabase.removeFile(path);
    }

    public File createRandomFile(Context context) {
        File f = new File(getSrcPath(context), Integer.toHexString((int) System.currentTimeMillis()) + ".pas");
        return createNewFile(f.getPath());
    }

    /**
     * copy data from in to inType
     */
    public void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        out.flush();
        out.close();

    }

    /**
     * copy data from file in to file inType
     */
    public void copy(String pathIn, String pathOut) throws IOException {
        InputStream in = new FileInputStream(new File(pathIn));
        OutputStream out = new FileOutputStream(new File(pathOut));
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        out.flush();
        out.close();

    }

    public Intent createShortcutIntent(Context context, File file) {
        // create shortcut if requested
        Intent.ShortcutIconResource icon =
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);

        Intent intent = new Intent();

        Intent launchIntent = new Intent(context, ActivitySplashScreen.class);
        launchIntent.putExtra(CompileManager.FILE_PATH, file.getPath());
        launchIntent.setAction("run_from_shortcut");

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launchIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, file.getName());
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        return intent;
    }
}
