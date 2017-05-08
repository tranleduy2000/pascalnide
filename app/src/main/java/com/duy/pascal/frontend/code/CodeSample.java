/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.frontend.code;

/**
 * Created by Duy on 12-Feb-17.
 */
@SuppressWarnings("DefaultFileTemplate")
public class CodeSample {
    public static final String MAIN =
            "program abc;\n" +
                    "uses crt; \n" +
                    "begin\n" +
                    "\t" +
                    "\treadln;\n" +
                    "end.";

    public static final int DEFAULT_POSITION = 33;

    public static final String DEMO_THEME = "program array_example;\n" +
            "var\n" +
            "    simpleArray: array[1..3] of integer;\n" +
            "    complexArray: array[1..3, 1..3] of integer;\n" +
            "    arrayOfString: array[-3..-1] of string;\n" +
            "    i, j: integer;\n" +
            "begin\n" +
            "    {set text for simpleArray}\n" +
            "    for i:=2 to 3 do simpleArray[i] := i * i;\n" +
            "    {write to console}\n" +
            "    for i:=1 to 3 do writeln(simpleArray[i]);\n" +
            "    {set text for complexArray}\n" +
            "    for i:=2 to 3 do\n" +
            "        for j:=1 to 2 do complexArray[i][j] := i * j;\n" +
            "    writeln('complex array:');\n" +
            "    {write to console}\n" +
            "    for i:=1 to 3 do for j:=1 to 3 do writeln('[',i,',',j,']=',complexArray[i][j]);\n" +
            "\n" +
            "    arrayOfString[-2] := 'hello pascal';\n" +
            "    writeln(arrayOfString[-3],arrayOfString[-2]);\n" +
            "    readln;\n" +
            "end.";
}
