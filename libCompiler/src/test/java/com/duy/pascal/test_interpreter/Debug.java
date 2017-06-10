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

import static com.duy.pascal.Interperter.runProgram;

/**
 * Created by Duy on 29-May-17.
 */

public class Debug {

    public static void main(String[] args) {
        testIndexEnum();
    }

    public static void testIndexEnum() {
        try {
            runProgram("C:\\github\\pascalnide\\test_pascal\\declare\\test_index_enum.pas");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
