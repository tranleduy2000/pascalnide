/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.android_scripting;

import com.googlecode.android_scripting.interpreter.InterpreterConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Manages storage and retrieval of scripts on the file system.
 *
 * @author Damon Kohler (damonkohler@gmail.com)
 */
public class ScriptStorageAdapter {

    private ScriptStorageAdapter() {
        // Utility class.
    }

    /**
     * Returns a list of all available script {@link File}s.
     */
    public static List<File> listAllScripts(File dir) {
        if (dir == null) {
            dir = new File(InterpreterConstants.SCRIPTS_ROOT);
        }
        if (dir.exists()) {
            List<File> scripts = Arrays.asList(dir.listFiles());
            Collections.sort(scripts, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    if (file1.isDirectory() && !file2.isDirectory()) {
                        return -1;
                    } else if (!file1.isDirectory() && file2.isDirectory()) {
                        return 1;
                    }
                    return file1.compareTo(file2);
                }
            });
            return scripts;
        }
        return new ArrayList<>();
    }

}