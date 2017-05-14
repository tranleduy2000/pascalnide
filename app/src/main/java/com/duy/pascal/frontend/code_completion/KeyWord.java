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

package com.duy.pascal.frontend.code_completion;

/**
 * Created by Duy on 30-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class KeyWord {

    public static final String[] KEY_WORDS = new String[]{
            "program", "begin", "end.", "procedure", "function", "uses",
            "integer;", "real;", "string;", "char;", "longint;", "word;", "byte;", "extended;",
            "const", "var", "operator", "array", "record",
            "shl", "shr",
            "if ", "then", "else",
            "for", "to", "do", "downto",
            "while", "repeat", "until", "case", "of",
            "and", "or", "xor", "not", "div", "mod",
    };


    public static final String[] SPINETS = new String[]{
            "if (^) then  ;",
            "if (^) then \n" +
                    "begin\n" +
                    "\t\n" +
                    "end;\n",
            "if (^) then  else  ;\n",
            "if (^) then\n " +
                    "begin\n " +
                    "\t\n" +
                    "end\n" +
                    "else\n" +
                    "begin\n" +
                    "\t\n" +
                    "end;\n",
            "for ^ :=  to  do  \n",
            "for ^ :=  to  do\n" +
                    "begin\n" +
                    "\t\n" +
                    "end;\n",
            "for ^ :=  downto  do  \n",
            "for ^ :=  downto  do\n" +
                    "begin\n" +
                    "\t\n" +
                    "end;\n",
            "while (^) do  ;\n",
            "while (^) do\n" +
                    "begin\n" +
                    "\t\n" +
                    "end;\n",
            "case ^ of \n" +
                    "\t\n" +
                    "end;\n",
            "case ^ of \n" +
                    "\t\n" +
                    "end;\n" +
                    "else  ;\n",
            "repeat\n" +
                    "\t\n" +
                    "until (^ = );\n"

    };

    public static final String[] OPERATOR_BOOLEAN = new String[]{
            "and", "or", "xor", "not", "<", ">", "=", "<>", "<=", ">="
    };
    public static final String[] OPERATOR = new String[]{
            "+", "-", "*", "/", "div", "mod"
    };

    public static final String[] SYMBOL_KEY = new String[]{
            ":=", ";", ".", "[", "]", ":", "'", "(", ")", "<", ">", "=", "<>", "<=", ">=", "{", "}", "+",
            "-", "*", "/", "_"
    };

    public KeyWord() {
        initKeyWord();
    }

    private void initKeyWord() {
    }

}
