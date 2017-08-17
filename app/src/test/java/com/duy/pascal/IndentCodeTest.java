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

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.editor.indention.PascalFormatCode;
import com.duy.pascal.interpreter.BaseTestCase;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Duy on 30-May-17.
 */

public class IndentCodeTest extends BaseTestCase {
    private static final String dir = "C:\\github\\pascalnide-public\\test_pascal\\test_indent\\";

    @Override
    public String getDirTest() {
        return "test_indent";
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        DLog.ANDROID = false;
        DLog.DEBUG = false;

    }


    public void testIfElse() {
        run("test_ifelse.pas");
    }

    public void testIfelseIfelse() {
        run("test_ifelse_ifelse.pas");
    }

    public void testNestedIfelse() {
        run("test_nested_ifelse.pas");
    }

    public void testFor() {
        run("test_for_in_array.pas");

    }

    public void testFor1() {
        run("test_for_in_enum.pas");

    }

    public void testFor2() {
        run("test_for_in_set.pas");

    }

    public void testCase() {
        run("test_case.pas");
    }

    public void testCaseNested() {
        run("test_case_nested.pas");
    }

    public void testCaseElse() {
        run("test_case_else.pas");
    }

    public void testAccess() {
        run("test_access_field.pas");
    }

    public void testParseRecordInFun() {
        run("test_record_function.pas");
    }

    public void testRecordPointer() {
        run("test_record_pointer.pas");
    }

    public void testInitRecord() {
        run("test_init_record.pas");
    }

    public void testUnit() {
        run("test_unit.pas");
    }

    public void testFunction() {
        run("test_function.pas");
    }

    public void testAllDeclare() throws InterruptedException {
        File parent = new File("C:\\github\\pascalnide-public\\test_pascal\\test_declare");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                PascalFormatCode indentCode = null;
                try {
                    indentCode = new PascalFormatCode(new FileReader(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------");
                System.out.println(indentCode.getResult());
                System.out.println("------------------------------");
                Thread.sleep(10);
            }
        }
    }

    public void testAll2() throws InterruptedException {
        File parent = new File("C:\\github\\pascalnide-public\\app\\src\\main\\assets\\code_sample\\android");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                PascalFormatCode indentCode = null;
                try {
                    indentCode = new PascalFormatCode(new FileReader(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------");
                System.out.println(indentCode.getResult());
                System.out.println("------------------------------");
                Thread.sleep(10);
            }
        }
    }

    public void testAll3() throws InterruptedException {
        File parent = new File("C:\\github\\pascalnide-public\\test_pascal\\data");
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                PascalFormatCode indentCode = null;
                try {
                    indentCode = new PascalFormatCode(new FileReader(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("------------------------------");
                System.out.println(indentCode.getResult());
                System.out.println("------------------------------");
                Thread.sleep(10);
            }
        }
    }

    public void test5() {
        run("C:\\github\\pascalnide-public\\test_pascal\\autofix\\grouping\\missing_end.pas");
    }

    public void atestAll() throws IOException, InterruptedException {
        File parent = new File(dir);
        for (File file : parent.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                run(file.getName());
                Thread.sleep(10);
            }
        }
    }

    public void run(String fileName) {
        PascalFormatCode indentCode = null;
        try {
            File file = new File(fileName);
            if (file.exists()) {
                indentCode = new PascalFormatCode(new FileReader(fileName));
            } else {
                indentCode = new PascalFormatCode(new FileReader(new File(dir, fileName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        System.out.println("------------------------------");
        System.out.println(indentCode.getResult());
        System.out.println("------------------------------");
        assertTrue(true);
    }

    public void testRepeat() {
        run("test_repeat.pas");
    }

    public void testRepeatElse() {
        run("test_repeat_else.pas");
    }

    public void testFormatComment() {
        run("test_comment.pas");
    }
}
