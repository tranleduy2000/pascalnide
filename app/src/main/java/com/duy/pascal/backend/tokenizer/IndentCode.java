package com.duy.pascal.backend.tokenizer;

import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.closing.EndToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.js.interpreter.core.ScriptSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Duy on 07-May-17.
 */

public class IndentCode {
    private String source;
    private Stack<Token> stack = new Stack<>();

    public IndentCode(String source) throws IOException {
        this.source = source;
        loadInput();
        parse();
    }

    public static void main(String[] args) {

    }

    private void loadInput() throws IOException {
        Lexer lexer = new Lexer(new StringReader(source), "indent", new ArrayList<ScriptSource>());
        Token token = lexer.yylex();
        while (!(token instanceof EOFToken)) {
            stack.add(token);
            token = lexer.yylex();
        }
    }

    private void parse() {
        StringBuilder result = new StringBuilder();
        while (stack.peek() != null) {
            result.append(processNext(1));
        }
    }

    private StringBuilder processNext(int depth) {
        Token token = stack.pop();
        if (token instanceof EOFToken || token instanceof GroupingExceptionToken) {
            return new StringBuilder(); //end of file
        }

        if (token instanceof BeginEndToken) {
            StringBuilder beginEnd = new StringBuilder();
            beginEnd.append(((BeginEndToken) token).toCode());
            while (!(stack.peek() instanceof EndToken)) {
                beginEnd.append(processNext(depth + 1));
            }
            if (stack.peek() instanceof EndToken) {
                token = stack.pop();
                beginEnd.append(((EndToken) token).toCode());
            }
        }
        return new StringBuilder(token.toString());
    }
}
