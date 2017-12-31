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

package com.duy.pascal.asset_data;

import com.duy.pascal.ui.utils.DLog;

import junit.framework.TestCase;

import java.io.File;

import static com.duy.pascal.Interpreter.checkSyntax;

/**
 * Created by Duy on 29-May-17.
 */

public class SyntaxAssetDataTest extends TestCase {
    String dir = new File(System.getProperty("user.dir"), "/app/src/main/assets/CodeSample/").getPath();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
    }

    public void testSystem() throws Exception {
        File parent = new File(dir, "system");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testBasic() {
        File parent = new File(dir, "basic");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testCrt() {
        File parent = new File(dir, "crt");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testDos() {
        File parent = new File(dir, "dos");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testMath() {
        File parent = new File(dir, "math");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testStrutils() {
        File parent = new File(dir, "sysutils");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testandroid() {
        File parent = new File(dir, "android");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void test_generateTone() {
        checkSyntax(new File(dir, "android/generate_tone.pas").getPath());
    }

    public void testandroid_dialog() {
//        File parent = new File(dir,  "android_dialog");
//        for (File file : parent.listFiles()) {
//            if (file.getName().endsWith(".pas")) {
//                assertTrue(checkSyntax(file.getPath()));
//            }
//        }
    }

    public void testandroid_location() {
//        File parent = new File(dir,  "android_location");
//        for (File file : parent.listFiles()) {
//            if (file.getName().endsWith(".pas")) {
//                assertTrue(checkSyntax(file.getPath()));
//            }
//        }
    }

    public void testandroid_zxing() {
//        File parent = new File(dir,  "android_zxing");
//        for (File file : parent.listFiles()) {
//            if (file.getName().endsWith(".pas")) {
//                assertTrue(checkSyntax(file.getPath()));
//            }
//        }
    }

    public void testgraph() {
        File parent = new File(dir, "graph");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testCompleteProgram() {
        File parent = new File(dir, "CompleteProgram");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testsysutils() {
        File parent = new File(dir, "sysutils");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }

    public void testtemp() {
        File parent = new File(dir, "temp");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                assertTrue(checkSyntax(file.getPath()));
            }
        }
    }
}
