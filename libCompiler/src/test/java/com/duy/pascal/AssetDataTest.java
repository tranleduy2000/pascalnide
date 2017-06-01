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

package com.duy.pascal;

import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;
import com.duy.pascal.frontend.DLog;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileNotFoundException;

import static com.duy.pascal.Compiler.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class AssetDataTest extends TestCase {
    String dir = "C:\\github\\pascalnide\\libCompiler\\src\\main\\assets\\code_sample\\";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
    }

    public void testSystem() throws ParsingException, RuntimePascalException, FileNotFoundException {
        File parent = new File(dir + "system");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                runProgram(file.getPath());
            }
        }
    }

    public void testBasic() {
        File parent = new File(dir + "basic");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                } catch (ParsingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testCrt() {
        File parent = new File(dir + "crt");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                } catch (ParsingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testDos() {
        File parent = new File(dir + "dos");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                } catch (ParsingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testMath() {
        File parent = new File(dir + "math");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                } catch (ParsingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testStrutils() {
        File parent = new File(dir + "strutils");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                } catch (RuntimePascalException e) {
                    e.printStackTrace();
                } catch (ParsingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
