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

package com.duy.pascal.lib;

import com.duy.pascal.interpreter.BaseTestCase;

import java.io.File;

/**
 * Created by Duy on 13-Jun-17.
 */

public class SysUtilsTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_libraries" + File.separator + "sysutils";
    }

    public void testTime() {
        run("time.pas");
    }

    public void testDate() {
        run("date.pas");
    }

    public void testDecodeDate() {
        run("date.pas");
    }

    public void testFormatArg() {
        run("test_format_arg.pas");
    }

    public void testFormatArg2() {
        run("Formatter.pas");
    }
}
