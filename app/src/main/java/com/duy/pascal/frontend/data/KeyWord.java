package com.duy.pascal.frontend.data;

/**
 * Created by Duy on 30-Mar-17.
 */

public class KeyWord {

    public static final String[] LIST_KEY_WORD = new String[]{
            "program  ;\n\nbegin\n\t\nend.", "begin\n\t\nend;", "end", "procedure", "function", "uses",
            "integer;", "real;", "string;", "char;", "longint;", "word;", "byte;", "extended;",
            "const", "var", "type", "array",/* "record",*/
            "shl", "shr",
            "if  then ;", "then", "else",
            "for  to  do ;", "to", "do", "downto",
            "while  do  ;", "repeat", "until", "case", "of",
            "and", "or", "xor", "not", "div", "mod",

            //system
            "dec(", "inc(",

            //io
            "writeln", "readln", "write(", "read",

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
            "writelnf(", "readlnf(", "readf(", "writef(", "assign(", "close(", "reset(", "rewrite",
            "eof("

    };

    public static final String[] OPERATOR_BOOLEAN = new String[]{
            "and", "or", "xor", "not", "<", ">", "=", "<>", "<=", ">="
    };
    public static final String[] OPERATOR = new String[]{
            "+", "-", "*", "/", "div", "mod"
    };

    public static final String[] SYMBOL_KEY = new String[]{
            ":=", ";", ".", "[", "]", "'", "(", ")", "<", ">", "=", "<>", "<=", ">=", "{", "}"
    };

}
