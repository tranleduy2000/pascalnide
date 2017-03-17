package com.duy.pascal.compiler.data;

import android.content.Context;

import com.duy.pascal.compiler.file_manager.Database;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Duy on 17-Mar-17.
 */

public class TabFileUtils {
    public static ArrayList<File> getTabFiles(Context context) {
        ArrayList<File> files = new ArrayList<>();
        Database database = new Database(context);
        files.addAll(database.getListFile());
        return files;
    }
}
