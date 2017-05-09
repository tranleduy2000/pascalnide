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

package com.duy.pascal.frontend.data;

/**
 * Created by Duy on 30-Mar-17.
 */

@SuppressWarnings("DefaultFileTemplate")
public class KeyWord {

    public static final String[] LIST_KEY_WORD = new String[]{
            "program  ;\n\nbegin\n\t\nend.", "begin\n\t\nend;", "end", "procedure", "function", "uses",
            "integer;", "real;", "string;", "char;", "longint;", "word;", "byte;", "extended;",
            "const", "var", "operator", "array", "record",
            "shl", "shr",
            "if  then ;", "then", "else",
            "for  to  do ;", "to", "do", "downto",
            "while  do  ;", "repeat", "until", "case", "of",
            "and", "or", "xor", "not", "div", "mod",

           /* //system
            "dec(", "inc(",

            //io
            "writeln()", "readln()", "write()", "read()",

            //convert
            "str(", "val(", "chr(", "ord(",

            //dos
            "getTime(", "getDate(",

            //crt
            "clrscr;", "textBackground(", "textColor(", "gotoXY(", "delay(", "whereX;", "whereY;",
            //math
            "trunc(", "round(", "abs(", "sin(", "cos(", "tan(",
            "sqrt(", "sqr(", "pred(", "succ(", "ln(", "random(", "randomize;",
            "length(",
            "true", "false",
            //string
            "concat(", "insert(", "pos(", "upcase(", "delete(",
            //file
            "assign(", "close(", "reset(", "rewrite",
            "eof("*/

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
