/*
 * Copyright (C) 2014 Vlad Mihalachi
 *
 * This file is part of Turbo Editor.
 *
 * Turbo Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Turbo Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.duy.pascal.frontend.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import java.io.File;

public final class PreferenceHelper {


    private PreferenceHelper() {
    }

    // Getter Methods

    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String defaultFolder(Context context) {
        String folder;
        File externalFolder = context.getExternalFilesDir(null);

        if (externalFolder != null && Device.isKitKatApi()) {
            folder = externalFolder.getAbsolutePath();
        } else {
            folder = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        //folder = context.getExternalFilesDir(null).getAbsolutePath();
        //folder = Environment.getExternalStorageDirectory().getAbsolutePath();
        return folder;
    }

    public static String getWorkingFolder(Context context) {
        return getPrefs(context).getString("working_folder2", defaultFolder(context));
    }


}
