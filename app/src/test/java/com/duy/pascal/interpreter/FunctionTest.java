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

package com.duy.pascal.interpreter;

import java.io.File;

import static com.duy.pascal.Interperter.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class FunctionTest extends BaseTestCase {

    public void testAllFunction() throws Exception {
        File parent = new File(dir);
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                try {
                    runProgram(file.getPath());
                    assertTrue(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    assertTrue(false);
                }
            }
        }
    }

    public void testNested(){
        run("test_nested.pas");
    }


    @Override
    public String getDirTest() {
        return "test_function";
    }
}
