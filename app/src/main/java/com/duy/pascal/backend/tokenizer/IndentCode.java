package com.duy.pascal.backend.tokenizer;


import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.AssignmentToken;
import com.duy.pascal.backend.tokens.basic.ConstToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.ForToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.IfToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.basic.RepeatToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.ThenToken;
import com.duy.pascal.backend.tokens.basic.TypeToken;
import com.duy.pascal.backend.tokens.basic.UntilToken;
import com.duy.pascal.backend.tokens.basic.UsesToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.basic.WhileToken;
import com.duy.pascal.backend.tokens.closing.EndBracketToken;
import com.duy.pascal.backend.tokens.closing.EndParenToken;
import com.duy.pascal.backend.tokens.closing.EndToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.RecordToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.js.interpreter.core.ScriptSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Duy on 07-May-17.
 */

public class IndentCode {
    private Reader source;
    private LinkedList<Token> stack = new LinkedList<>();

    private StringBuilder mResult;

    public IndentCode(Reader source) throws IOException {
        this.source = source;
        loadInput();
        parse();
    }

    public static void main(String[] args) throws IOException {
        File dir = new File("C:\\Users\\Duy\\IdeaProjects\\JSPIIJ\\tests\\basic");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".pas")) {
                IndentCode indentCode = new IndentCode(new FileReader(file));
                System.out.println(indentCode.getResult());
                System.out.println("------------------------");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public StringBuilder getResult() {
        return mResult;
    }

    private void loadInput() throws IOException {
        Lexer lexer = new Lexer((source), "indent", new ArrayList<ScriptSource>());
        Token token = lexer.yylex();
        while (!(token instanceof EOFToken)) {
            stack.add(token);
            token = lexer.yylex();
        }
    }

    private void parse() {
        mResult = new StringBuilder();
        while (stack.peek() != null) {
            mResult.append(processNext(0));
//            mResult.append("\n");
        }
        System.out.println("END OF FILE");
    }


    private StringBuilder processNext(int depth, Token token) {
        if (token instanceof EOFToken || token instanceof GroupingExceptionToken) {
            return new StringBuilder(); //end of file
        }

        if (token instanceof BeginEndToken) {
            return completeBeginToken(depth, token);
        } else if (token instanceof ValueToken) {
            return completeValue((ValueToken) token);
        } else if (token instanceof IfToken) {
            return completeIfToken(depth, token);
        } else if (token instanceof VarToken
                || token instanceof TypeToken
                || token instanceof ConstToken
                || token instanceof UsesToken) {
            StringBuilder result = new StringBuilder();
            result.append(getTab(depth));
            result.append(token.toString());
            result.append("\n");
            while (stack.peek() instanceof WordToken
                    || stack.peek() instanceof OperatorToken) {
                StringBuilder next = getLineCommand(depth + 1, true);
                result.append(next);
            }
            return result;
        } else if (token instanceof ProgramToken) {
            StringBuilder result = new StringBuilder();
            result.append(getTab(depth));
            result.append(token.toString()).append(" ");
            result.append(getLineCommand(depth, false));
            return result;
        } else if (token instanceof RecordToken) {
            return completeRecordToken(depth, token);
        } else if (token instanceof FunctionToken ||
                token instanceof ProcedureToken) {
            return completeFunctionToken(depth, token);
        } else if (token instanceof CaseToken) {
            return completeCaseToken(depth, token);
        } else if (token instanceof GrouperToken) {
            return new StringBuilder(((GrouperToken) token).toCode());
        } else if (token instanceof SemicolonToken) {
            return new StringBuilder(token.toString()).append(" \n");
        } else if (token instanceof ForToken) {
            return completeFor(depth, token);
        } else if (token instanceof WhileToken) {
            return completeWhile(depth, token);
        } else if (token instanceof ElseToken) {
            return processElse(depth, token);
        } else if (token instanceof WordToken) {
            return completeWord((WordToken) token);
        } else if (token instanceof RepeatToken) {
            return completeRepeatUntil(depth, token);
        } else if (isCloseToken(token)) {
            return new StringBuilder(token.toString());
        }
        return new StringBuilder(token.toString()).append(" ");
    }

    private Token peek() {
        return stack.peek();
    }

    private Token take() {
        return stack.pop();
    }

    private boolean isCloseToken(Token token) {
        return token instanceof EndToken ||
                token instanceof EndParenToken ||
                token instanceof EndBracketToken;
    }

    private StringBuilder completeValue(ValueToken token) {
        if (isCloseToken(peek())) {
            return new StringBuilder(token.toCode());
        } else {
            return new StringBuilder(token.toCode()).append(" ");
        }
    }

    private StringBuilder completeWord(WordToken token) {
        if (stack.peek() instanceof WordToken
                || peek() instanceof ValueToken
                || peek() instanceof OperatorToken
                || peek() instanceof AssignmentToken) {
            return new StringBuilder(token.getOriginalName()).append(" ");
        } else {
            return new StringBuilder(token.getOriginalName());
        }
    }

    private StringBuilder completeCaseToken(int depth, Token token) {
        StringBuilder caseStatement = new StringBuilder();
        caseStatement.append(getTab(depth)).append(((CaseToken) token).toCode()).append(" ");

        StringBuilder next = new StringBuilder();
        while (stack.peek() != null
                && !(stack.peek() instanceof OfToken)
                && !(stack.peek() instanceof SemicolonToken)) {
            next.append(processNext(depth, stack.pop()));
        }
        caseStatement.append(next).append(" ");
        if (stack.peek() instanceof OfToken) {
            caseStatement.append(stack.pop()).append("\n");
        }

        StringBuilder body = new StringBuilder();
        while (stack.peek() != null && !(stack.peek() instanceof EndToken)) {
            body.append(getLineCommand(depth + 1, true));
        }
        caseStatement.append(body);
        if (stack.peek() instanceof EndToken) {
            caseStatement.append("\n").append(getTab(depth))
                    .append(completeEnd((EndToken) stack.pop()));
        }
        if (stack.peek() instanceof ElseToken) {
            caseStatement.append(getTab(depth)).append(stack.pop()).append(" ");
            caseStatement.append(getLineCommand(depth, false));
            caseStatement.append("\n");
        }
        return caseStatement;
    }

    private StringBuilder completeRecordToken(int depth, Token token) {
        StringBuilder record = new StringBuilder();
        record.append(((RecordToken) token).toCode());
        record.append("\n");

        while (stack.peek() != null && !(stack.peek() instanceof EndToken)) {
            StringBuilder next = getLineCommand(depth + 1, true);
            record.append(next);
        }

        if (stack.peek() instanceof EndToken) {
            token = stack.pop();
            record.append(completeEnd((EndToken) token));
        }
        return record;
    }

    private StringBuilder completeEnd(EndToken endToken) {
        StringBuilder end = new StringBuilder();
        if (!(stack.peek() instanceof SemicolonToken
                || stack.peek() instanceof PeriodToken)) {
            end.append("\n");
        }
        end.append(endToken.toCode());
        return end;
    }

    private StringBuilder completeTypeToken() {
        return null;
    }

    private StringBuilder completeRepeatUntil(int depth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append(getTab(depth)).append(token).append(" ").append("\n");
        while (stack.peek() != null && !(stack.peek() instanceof UntilToken)) {
            StringBuilder next = new StringBuilder();
            while (stack.peek() != null &&
                    !(stack.peek() instanceof UntilToken) &&
                    !(stack.peek() instanceof SemicolonToken) &&
                    !(stack.peek() instanceof PeriodToken)) {
                next.append(processNext(depth + 1, stack.pop()));
            }
            if (stack.peek() instanceof SemicolonToken) {
                next.append(stack.pop());
                next.append("\n");
            }
            result.append(getTab(depth + 1)).append(next);
        }
        if (stack.peek() instanceof UntilToken) {
            result.append(getTab(depth)).append(stack.pop()).append(" ");
            result.append(getLineCommand(depth, false));
            result.append("\n");
        }
        return result;
    }

    private StringBuilder completeWhile(int depth, Token token) {
        StringBuilder whileStatement = new StringBuilder();
        whileStatement.append(token.toString()).append(" ");

        StringBuilder next = new StringBuilder();
        while (stack.peek() != null
                && !(stack.peek() instanceof DoToken)
                && !(stack.peek() instanceof SemicolonToken)) {
            next.append(processNext(depth, stack.pop()));
        }
        whileStatement.append(next).append(" ");

        //if contain else token
        if (stack.peek() instanceof DoToken) {
            //append else
            whileStatement.append(completeDo(depth, stack.pop()));
        }

        return whileStatement;
    }

    private StringBuilder completeDo(int depth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append(token).append(" ").append("\n");

        StringBuilder next = new StringBuilder();
        if (stack.peek() instanceof BeginEndToken) {
            next.append(completeBeginToken(depth, stack.pop()));
            result.append(next).append(" ");
        } else {
            while (stack.peek() != null &&
                    !(stack.peek() instanceof SemicolonToken) &&
                    !(stack.peek() instanceof EndToken)) {
                next.append(processNext(depth));
            }
            result.append(getTab(depth)).append(next).append(" ");
        }
        return result;
    }

    private StringBuilder processElse(int depth, Token token) {
        return new StringBuilder(token.toString());
    }

    private StringBuilder completeFor(int depth, Token token) {
        StringBuilder forStatement = new StringBuilder();
        forStatement.append(token.toString()).append(" ");

        StringBuilder next = new StringBuilder();
        while (stack.peek() != null
                && !(stack.peek() instanceof DoToken)
                && !(stack.peek() instanceof SemicolonToken)) {
            next.append(processNext(depth, stack.pop()));
        }
        forStatement.append(next).append(" ");

        //if contain else token
        if (stack.peek() instanceof DoToken) {
            //append else
            StringBuilder doStatment = completeDo(depth + 1, stack.pop());
            forStatement.append(doStatment);
        }

        return forStatement;
    }

    private StringBuilder completeBeginToken(int depth, Token token) {
        StringBuilder beginEnd = new StringBuilder();
        beginEnd.append(getTab(depth));
        beginEnd.append(((BeginEndToken) token).toCode());
        beginEnd.append("\n");

        while (stack.peek() != null && !(stack.peek() instanceof EndToken)) {
            StringBuilder next = getLineCommand(depth + 1, true);
            beginEnd.append(next);
        }

        if (stack.peek() instanceof EndToken) {
            token = stack.pop();
            if (!(stack.peek() instanceof SemicolonToken
                    || stack.peek() instanceof PeriodToken)) {
                beginEnd.append("\n");
            }
            beginEnd.append(getTab(depth));
            beginEnd.append(((EndToken) token).toCode());
        }
        return beginEnd;
    }

    private StringBuilder processNext(int depth) {
        if (stack.size() == 0) return new StringBuilder();
        Token token = stack.pop();
        return processNext(depth, token);
    }

    private StringBuilder completeIfToken(int depth, Token token) {
        StringBuilder result = new StringBuilder();
//        result.append(getTab(depth));
        result.append(token.toString()).append(" ");

        //if condition
        while (!(stack.peek() instanceof ThenToken)) {
            StringBuilder next = processNext(depth + 1);
            result.append(next);
        }
        result.append(" ");

        //then expression
        if (stack.peek() instanceof ThenToken) {
            //append else
            result.append(stack.pop());

            result.append("\n");
            StringBuilder next = getLineCommand(depth, true);
            result.append(next).append(" ");
        }
        result.append(" ");

        while (!(stack.peek() instanceof SemicolonToken || stack.peek() instanceof ElseToken)) {
            StringBuilder next = processNext(depth + 1, stack.pop());
            result.append(next);
        }

        //if contain else token
        if (stack.peek() instanceof ElseToken) {
            //append else
            result.append("\n").append(getTab(depth));
            result.append(stack.pop());

            result.append("\n");
            StringBuilder next = new StringBuilder();
            while (stack.peek() != null &&
                    !(stack.peek() instanceof SemicolonToken) &&
                    !(stack.peek() instanceof EndToken)) {
                next.append(processNext(depth, stack.pop()));

            }
            result.append(getTab(depth + 1)).append(next).append(" ");
//            result.append(next).append(" \n");
        }
        return result;
    }

    private StringBuilder completeFunctionToken(int depth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append(getTab(depth));
        result.append(token.toString()).append(" ");
        result.append(getLineCommand(depth, false));

        return result;
    }

    private boolean isGroupToken(Token token) {
        return token instanceof GrouperToken ||
                token instanceof RepeatToken;
    }

    //end of line by ;
    private StringBuilder getLineCommand(int depth, boolean tab) {
        StringBuilder result = new StringBuilder();
        if (tab && !isGroupToken(stack.peek())) {
            result.append(getTab(depth));
        }
        if (isGroupToken(stack.peek())) { //group commnand
            result.append(processNext(depth, stack.pop()));
        } else {
            //single command; end by semicolon token
            while (stack.peek() != null
                    && !(stack.peek() instanceof SemicolonToken)
                    && !(stack.peek() instanceof EndToken)) {
                StringBuilder next = processNext(depth, stack.pop());
                result.append(next);
            }
            if (stack.peek() instanceof SemicolonToken) {
                result.append(stack.pop().toString()).append("\n");
            }
        }
        return result;
    }


    private StringBuilder getTab(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append("\t");
        }
        return stringBuilder;
    }
}
