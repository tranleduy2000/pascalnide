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

public class RecordTest extends BaseTestCase {
    @Override
    public String getDirTest() {
        return "test_record";
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

    public void testField() {
        run("test_field.pas");
    }

    public void testFieldPointer() {
        run("test_field_pointer.pas");
    }

    public void testPointerInPointer() {
        run("test_pointer_in_pointer.pas");
    }

    public void testConst() {
        run("test_const.pas");
    }

}
