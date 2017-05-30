package com.duy.pascal.backend.tokenizer;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.tokens.CommentToken;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.AssignmentToken;
import com.duy.pascal.backend.tokens.basic.ConstToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.FinalizationToken;
import com.duy.pascal.backend.tokens.basic.ForToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.IfToken;
import com.duy.pascal.backend.tokens.basic.ImplementationToken;
import com.duy.pascal.backend.tokens.basic.InitializationToken;
import com.duy.pascal.backend.tokens.basic.InterfaceToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.basic.ProgramToken;
import com.duy.pascal.backend.tokens.basic.RepeatToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.ThenToken;
import com.duy.pascal.backend.tokens.basic.ToToken;
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
import com.duy.pascal.frontend.DLog;
import com.duy.pascal.backend.source_include.ScriptSource;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import static com.duy.pascal.frontend.code_editor.editor_view.AutoIndentEditText.TAB_CHARACTER;

/**
 * This class will be reformat code pascal
 * <p>
 * <p>
 * Created by Duy on 07-May-17.
 */
public class IndentCode {
    private static final String TAG = "IndentCode";
    private Reader source;
    private LinkedList<Token> stack = new LinkedList<>();
    private StringBuilder mResult;

    public IndentCode(Reader source) throws IOException {
        this.source = source;
        loadInput();
        parse();
    }

    public IndentCode() {
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

    public void setSource(String code) throws IOException {
        this.source = new StringReader(code);
        loadInput();
        parse();
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
        while (peek() != null) {
            mResult.append(processNext(0));
        }
        DLog.d(TAG, "parse: eof");
    }


    private StringBuilder processNext(int depth, @Nullable Token token) {
        if (token instanceof EOFToken || token instanceof GroupingExceptionToken
                || token == null) {
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
                || token instanceof UsesToken
                || token instanceof InterfaceToken
                || token instanceof ImplementationToken
                || token instanceof FinalizationToken
                || token instanceof InitializationToken) {

            StringBuilder result = new StringBuilder();
            result.append(getTab(depth));
            result.append(token.toString());
            result.append("\n");
            while (peek() instanceof WordToken
                    || peek() instanceof OperatorToken) {
                StringBuilder next = getLineCommand(depth + 1, true);
                result.append(next);
            }
            result.append("\n");
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
        } else if (token instanceof RepeatToken) {
            return completeRepeatUntil(depth, token);

        } else if (token instanceof GrouperToken) {
            return new StringBuilder(((GrouperToken) token).toString());

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
        } else if (isCloseToken(token)) {

            return completeCloseToken(token);

        } else if (token instanceof CommentToken) {
            return completeCommentToken(depth, token);
//            return new StringBuilder();
        } else if (token instanceof PeriodToken) {
            return new StringBuilder(token.toString());

        }
        return new StringBuilder(token.toString()).append(" ");
    }

    private StringBuilder completeCommentToken(int depth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(getTab(depth - 1));
        result.append(token.toString());
        result.append("\n");
        result.append(getTab(depth - 1));
        DLog.d(TAG, "completeCommentToken() returned: " + result);
        return result;
    }

    private StringBuilder completeCloseToken(Token token) {
        DLog.d(TAG, "completeCloseToken: next = " + peek());
        if (peek() instanceof WordToken) {
            return new StringBuilder(token.toString()).append(" ");
        } else {
            return new StringBuilder(token.toString());
        }
    }

    @Nullable
    private Token peek() {
        if (stack.size() == 0) {
            return null;
        }
        return stack.peek();
    }

    @Nullable
    private Token take() {
        if (stack.size() == 0) {
            return null;
        }
        return stack.pop();
    }

    private boolean isCloseToken(Token token) {
        return token instanceof EndToken ||
                token instanceof EndParenToken ||
                token instanceof EndBracketToken;
    }

    private StringBuilder completeValue(ValueToken token) {
        if (!(peek() instanceof WordToken)
                && !isStatement(peek())) {
            return new StringBuilder(token.toString());
        } else {
            return new StringBuilder(token.toString()).append(" ");
        }
    }

    private StringBuilder completeWord(WordToken token) {
        if (peek() instanceof WordToken
                || peek() instanceof ValueToken
                || peek() instanceof OperatorToken
                || peek() instanceof AssignmentToken
                || isStatement(peek())) {
            return new StringBuilder(token.getOriginalName()).append(" ");
        } else {
            return new StringBuilder(token.getOriginalName());
        }
    }

    private StringBuilder completeCaseToken(int depth, Token token) {
        StringBuilder caseStatement = new StringBuilder();

        //append "case .. of .."
        caseStatement.append(getTab(depth)).append(((CaseToken) token).toString()).append(" ");

        StringBuilder next = new StringBuilder();
        while (peek() != null && !(peek() instanceof OfToken)
                && !(peek() instanceof SemicolonToken))
            next.append(processNext(depth, take()));

        caseStatement.append(next).append(" ");

        if (peek() instanceof OfToken) caseStatement.append(take()).append("\n");

        StringBuilder body = new StringBuilder();
        while (peek() != null && !(peek() instanceof EndToken))
            body.append(getLineCommand(depth + 1, true));

        caseStatement.append(body);

        if (peek() instanceof EndToken) {
            caseStatement.append("\n").append(getTab(depth))
                    .append(completeEnd((EndToken) take()));
        }

        if (peek() instanceof ElseToken) {
            caseStatement.append(getTab(depth)).append(take()).append(" ");
            caseStatement.append(getLineCommand(depth, false));
            caseStatement.append("\n");
        }
        return caseStatement;
    }

    private StringBuilder completeRecordToken(int depth, Token token) {
        StringBuilder record = new StringBuilder();
        record.append(token.toString());
        record.append("\n");

        while (peek() != null && !(peek() instanceof EndToken)) {
            StringBuilder next = getLineCommand(depth + 1, true);
            record.append(next);
        }

        if (peek() instanceof EndToken) {
            token = take();
            record.append(getTab(depth)).append(completeEnd((EndToken) token));
        }
        return record;
    }

    private StringBuilder completeEnd(EndToken endToken) {
        StringBuilder end = new StringBuilder();
        if (!(peek() instanceof SemicolonToken
                || peek() instanceof PeriodToken)) {
            end.append("\n");
        }
        end.append(endToken.toString());
        return end;
    }

    private StringBuilder completeTypeToken() {
        return null;
    }

    private StringBuilder completeRepeatUntil(int depth, Token token) {
        StringBuilder result = new StringBuilder();

        result.append(getTab(depth)).append(token).append(" ").append("\n"); //repeat

        while (peek() != null && !(peek() instanceof UntilToken)) {
            StringBuilder next = new StringBuilder();
            while (!(peek() == null || peek() instanceof UntilToken
                    || peek() instanceof SemicolonToken || peek() instanceof PeriodToken)) {
                next.append(processNext(depth + 1, take()));
            }

            if (peek() instanceof SemicolonToken) {
                next.append(take());
                next.append("\n");
            }
            result.append(getTab(depth + 1)).append(next);
        }

        if (peek() instanceof UntilToken) {
            result.append("\n").append(getTab(depth)).append(take()).append(" "); //until
            result.append(getLineCommand(depth, false)).append("\n");
        }
        return result;
    }

    private StringBuilder completeWhile(int depth, @NonNull Token token) {
        StringBuilder whileStatement = new StringBuilder();
        whileStatement.append(token.toString()).append(" ");

        StringBuilder next = new StringBuilder();
        while (peek() != null
                && !(peek() instanceof DoToken)
                && !(peek() instanceof SemicolonToken)) {
            next.append(processNext(depth, take()));
        }
        whileStatement.append(next).append(" ");

        //if contain else token
        if (peek() instanceof DoToken) {
            //append else
            whileStatement.append(completeDo(depth, take()));
        }

        return whileStatement;
    }

    private StringBuilder completeDo(int currentDepth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append(token).append(" ").append("\n"); //append "do"

        //add command
        StringBuilder next = new StringBuilder();
        next.append(getLineCommand(currentDepth + 1, true));
        result.append(next);

        DLog.d(TAG, "completeDo() returned: \n" + next);
        return result;
    }

    private StringBuilder processElse(int depth, Token token) {
        return new StringBuilder(token.toString());
    }

    private StringBuilder completeFor(int depth, Token token) {
        StringBuilder forStatement = new StringBuilder();
        forStatement.append(token.toString()).append(" "); //append "for"

        StringBuilder next = new StringBuilder();

        while (peek() != null
                && !(peek() instanceof DoToken)
                && !(peek() instanceof SemicolonToken)) {
            next.append(processNext(depth, take()));
        }
        forStatement.append(next);

        if (peek() instanceof DoToken) {
            //append else
            StringBuilder doStatement = completeDo(depth, take());
            forStatement.append(doStatement);
        }
        DLog.d(TAG, "completeFor() returned: \n" + forStatement);
        return forStatement;
    }

    private StringBuilder completeBeginToken(int depth, @NonNull Token token) {
        //append "begin"
        StringBuilder beginEnd = new StringBuilder();
        beginEnd.append(getTab(depth)).append(((BeginEndToken) token).toString()).append("\n");

        StringBuilder body = new StringBuilder();
        while (peek() != null && !(peek() instanceof EndToken))
            body.append(getLineCommand(depth + 1, true));

        if (!body.toString().trim().isEmpty()) beginEnd.append(body);
        else beginEnd.append("\n");

        if (peek() instanceof EndToken) {
            token = take();

            //check end by dot or comma symbol
            if (!(peek() instanceof SemicolonToken
                    || peek() instanceof PeriodToken)) {
                beginEnd.append("\n");
            }

            beginEnd.append(getTab(depth));
            beginEnd.append(((EndToken) token).toString());
        }

        return beginEnd;
    }

    private StringBuilder processNext(int depth) {
        if (stack.size() == 0) return new StringBuilder();
        Token token = take();
        return processNext(depth, token);
    }

    private StringBuilder completeIfToken(int depth, Token token) {
        StringBuilder result = new StringBuilder();
        result.append(token.toString()).append(" ");

        //if condition
        while (!(peek() instanceof ThenToken)) {
            StringBuilder next = processNext(depth + 1);
            result.append(next);
        }
        result.append(" ");

        //then expression
        if (peek() instanceof ThenToken) {
            //append else
            result.append(take());
            result.append("\n");
            StringBuilder next = new StringBuilder();
            next.append(getLineCommand(depth + 1, true));
            result.append(next).append(" ");
        }
        result.append(" ");

        while (peek() instanceof ElseToken) {
            //append else
            result.append("\n").append(getTab(depth));
            result.append(take()).append("\n");

            StringBuilder next = getLineCommand(depth + 1, true);
            result.append(next);
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
        return token instanceof GrouperToken;
    }

    private boolean isStatement(Token token) {
        return token instanceof IfToken ||
                token instanceof ElseToken ||
                token instanceof ThenToken ||
                token instanceof DoToken ||
                token instanceof WhileToken ||
                token instanceof ForToken ||
                token instanceof ToToken ||
                token instanceof BeginEndToken
                || token instanceof EndToken ||
                token instanceof RepeatToken
                || token instanceof CaseToken;
    }

    //end of lineInfo by ;
    private StringBuilder getLineCommand(int depth, boolean tab) {
        StringBuilder result = new StringBuilder();
        if (tab && !isGroupToken(peek())) {
            result.append(getTab(depth));
        }

        if (isGroupToken(peek())) { //group command
            DLog.d(TAG, "getLineCommand: group " + peek());
            result.append(processNext(depth, take()));
            if (peek() instanceof SemicolonToken) {
                result.append(take()).append("\n");
            }
        } else if (isStatement(peek())) {
            DLog.d(TAG, "getLineCommand: statement " + peek());
            result.append(processNext(depth, take()));
            if (peek() instanceof SemicolonToken) {
                result.append(take()).append("\n");
            }
        } else {
            //single command;
            //end by semicolon token
            while (peek() != null
                    && !(peek() instanceof SemicolonToken)
                    && !(peek() instanceof EndToken)
                    && !(peek() instanceof ElseToken)) {
                StringBuilder next = processNext(depth + 1, take());
                result.append(next);
            }
            if (peek() instanceof SemicolonToken) {
                result.append(take()).append("\n");
            }
        }
        DLog.d(TAG, "getLineCommand() returned: \n" + result);
        return result;
    }

    private String getWord(Token token) {
        return token.toString() + " ";
    }

    private StringBuilder getTab(int depth) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            stringBuilder.append(TAB_CHARACTER);
        }
        return stringBuilder;
    }
}
