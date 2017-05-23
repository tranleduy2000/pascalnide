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

package com.duy.pascal.frontend.code_editor.completion;

/**
 * Created by Duy on 22-May-17.
 */

public class Template {
    public static final String PROGRAM_TEMPLATE =
            "program {name};\n" +
                    "uses crt;" +
                    "begin\n" +
                    "    " +
                    "    readln;\n" +
                    "end.";

    public static final String UNIT_TEMPlATE =
            "unit {name};\n" +
                    "interface\n" +
                    "    \n" +
                    "implementation\n" +
                    "    \n" +
                    "initialization\n" +
                    "    (* here may be placed code that is *)\n" +
                    "    (* executed as the unit gets loaded *)\n" +
                    "begin\n" +
                    "    \n" +
                    "end;\n" +
                    "finalization\n" +
                    "    (* code executed at program end *)\n" +
                    "begin\n" +
                    "    \n" +
                    "end;\n" +
                    "end.";

    public static String createProgramTemplate(String name) {
        return PROGRAM_TEMPLATE.replace("{name}", name);
    }

    public static String createUnitTemplate(String name) {
        return UNIT_TEMPlATE.replace("{name}", name);
    }
}
