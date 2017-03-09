package com.duy.pascal.compiler.data;

import java.util.regex.Pattern;

/**
 * Created by Duy on 11-Feb-17.
 */

public class KeyWordAndPattern {
    public static final String[] LIST_KEY_WORD = new String[]{
            "program", "begin", "end", "procedure", "function",
            "integer", "real", "string", "char", "longint", "word", "byte", "extended",
            "const", "var", "type", "array",/* "record",*/
            "shl", "shr",
            "if", "then", "else",
            "for", "to", "do", "downto",
            "while", "repeat", "until", "case", "of",
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
            "concat(", "insert(", "pos(", "upcase("
    };
    public static final String[] OPERATOR_BOOLEAN = new String[]{
            "and", "or", "xor", "not", "<", ">", "=", "<>", "<=", ">="
    };
    public static final String[] OPERATOR = new String[]{
            "+", "-", "*", "/", "div", "mod"
    };
    //Words
    public static final Pattern line = Pattern.compile(".*\\n");
    public static final Pattern numbers = Pattern.compile(
            "\\b(\\d*[.]?\\d+)\\b");
    public static final Pattern keywords = Pattern.compile(
            "\\b(const|do|for|while|if|else|in|case|" +
                    "and|array|begin|case|div|" +
                    "downto|function|to|mod|not|of|or|" +
                    "procedure|program|repeat|until|shl|shr|string|" +
                    "then|type|var|while|end|" +
                    "function|var|case|array|shl|shr|" +
                    "true|false|boolean)\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern functions = Pattern.compile("\\b(sin|cos|sqrt|abs|floor|ceil|length|new|random|round|" +
            "exp|tan)\\b");
    public static final Pattern comments = Pattern.compile("(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)" +
            "|(\\{(?:.|[\\n\\r])*?\\})");
    public static final Pattern symbols = Pattern.compile("[\\+\\-\\*\\=\\<\\>\\/\\:\\)\\(\\]\\[]");
    public static final Pattern trailingWhiteSpace = Pattern.compile("[\\t ]+$", Pattern.MULTILINE);
    public static final Pattern uses = Pattern.compile(
            "(uses)(.*?);", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern readln = Pattern.compile(
            "(readln)\\s+;|(readln);", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static String[] symbols_key = new String[]{
            ":=", ";", ".", "//", "'", "(", ")", "<", ">", "=", "<>", "<=", ">="
    };

}
