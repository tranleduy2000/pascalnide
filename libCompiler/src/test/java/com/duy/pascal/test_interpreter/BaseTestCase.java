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

package com.duy.pascal.test_interpreter;

import com.duy.pascal.frontend.DLog;

import junit.framework.TestCase;

import java.io.File;

import static com.duy.pascal.Interperter.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public abstract class BaseTestCase extends TestCase {

    String dir;

    public abstract String getDirTest();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;

        dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);
        dir += File.separator + "test_pascal" + File.separator + getDirTest() + File.separator;
    }

    /**
     * generate a pascal file
     *
     * @param file - dir of program file
     */
    protected void run(String file) {
        try {
            runProgram(dir + file);
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * generate a pascal file
     *
     * @param file - dir of program file
     * @param in   - dir of input file for read input if need
     */
    protected void run(String file, String in) {
        try {
            runProgram(dir + file);
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void runAll() {
        boolean success = true;
        File parent = new File(dir);
        for (File file : parent.listFiles()) {
            try {
                if (file.getName().endsWith(".pas")) {
                    run(file.getName());
                    System.out.println("complete test " + file.getName());
                }
            } catch (Exception e) {
                success = false;
            }
        }
        assertTrue("state ", success);
    }
}
