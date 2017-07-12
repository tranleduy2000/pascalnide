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

/**
 * Created by Duy on 29-May-17.
 */

public class FileTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_file";
    }

    public void testWriteFile() {
        run("test_write_file.pas");
    }

    public void testRead() {
        run("test_read.pas");
    }

    public void testFileInSubProgram() {
        run("test_file_in_fun.pas");
    }

    public void testTextFile() {
        run("test_text_file.pas");
    }

    public void testAppend() {
        run("test_append.pas");
    }

    public void testEndOfLine() {
        run("test_eoln.pas");
    }
}
