package com.duy.pascal.backend.tokenizer;

import com.duy.pascal.backend.tokens.EOF_Token;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ParserTest {
    static Lexer lexer;

    public static void main(String[] args) {
        try {
            List<ScriptSource> includes = new ArrayList<ScriptSource>();
            String fileName = "C:\\Users\\Duy\\Downloads\\tests\\compare\\test_function.pas";
            Reader reader = new FileReader(new File(fileName));
            lexer = new Lexer(reader, "pascal ", includes);
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void parse() {
        while (true) {
            try {
                Object t = lexer.yylex();
                System.out.println(t.getClass().getSimpleName());
                if (t instanceof EOF_Token) {
                    return;
                }
            } catch (IOException e) {

                return;
            }
        }
    }


}
