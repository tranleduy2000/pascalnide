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

package com.duy.pascal.interperter.javaunderpascal.classpath;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by Duy on 16-May-17.
 */
public class JavaClassLoader {
    private DexClassLoader classLoader;

    public JavaClassLoader(@NonNull Context context) {
        String classpath = Environment.getExternalStorageDirectory().getPath()
                + "/PascalCompiler/rt.jar";

        File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
        classLoader = new DexClassLoader(classpath,
                dexOutputDir.getAbsolutePath(),
                null,
                ClassLoader.getSystemClassLoader());
    }


    public JavaClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath,
                           ClassLoader parent) {
    }

    @Nullable
    public Object loadClass(String name) {
        try {
            return classLoader.loadClass(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
