package com.duy.pascal.frontend.alogrithm;

import com.duy.pascal.backend.tokenizer.Lexer;
import com.duy.pascal.backend.tokens.CommentToken;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.ConstToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.basic.RepeatToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.ThenToken;
import com.duy.pascal.backend.tokens.basic.TypeToken;
import com.duy.pascal.backend.tokens.basic.UntilToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.closing.ClosingToken;
import com.duy.pascal.backend.tokens.closing.EndParenToken;
import com.duy.pascal.backend.tokens.closing.EndToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.CaseToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
import com.duy.pascal.backend.tokens.grouping.RecordToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.js.interpreter.core.ScriptSource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * @author Duy
 */
public class AutoIndentCode {

    private StringBuilder result = new StringBuilder();
    private Lexer lexer;
    private int numberTab = 0;
    private boolean needTab = false;
    private Token lastToken;
    private int numVarToken = 0;

    public String format(String code) {
        //reset number tab
        numberTab = 0;
        //create new reader
        Reader reader = new StringReader(code);
        //create new lexer
        lexer = new Lexer(reader, "pascal ", new ArrayList<ScriptSource>());
        return parse();
    }

    private String parse() {
        result = new StringBuilder("");
        while (true) {
            try {
                Token t = lexer.yylex();
                if (t instanceof EOFToken) {
                    return result.toString();
                }
                processToken(t);
            } catch (Exception e) {
                return result.toString();
            }
        }

    }

    private void processToken(Token t) throws IOException {
        if (t instanceof EOFToken || t instanceof GroupingExceptionToken) {
            return;
        }
        //begin ... end; repeat ... until; case of ... end;
        if (t instanceof EndToken) {
            processEnd(t);
            return;
        } else if (t instanceof UntilToken) {
            completeUntil(t);
            return;
        }

        //? array of
        //begin ... end; repeat ... until; case of ... end; record ... end;
        if (t instanceof BeginEndToken || t instanceof RecordToken) {
            processBeginToken(t);
        } else if (t instanceof OfToken) {
            processOfToken(t);
        } else if (t instanceof OperatorToken) {
            processOptToken(t);
        } else if (t instanceof RepeatToken) {
            processRepeatToken(t);
        } else if (t instanceof ParenthesizedToken || t instanceof BracketedToken) {
            result.append(getStringTab(numberTab));
            result.append(((GrouperToken) t).toCode());
        } else if (t instanceof DoToken || t instanceof ThenToken || t instanceof CaseToken) {
            processDoToken(t);
        } else if (t instanceof PeriodToken) {
            processPeriodToken(t);
        } else if (t instanceof SemicolonToken) { //new line
            processSemicolonToken(t);
        } //dont add white space, remove last space char
        else if (t instanceof ElseToken) {
            processElseToken(t);
        } else if (t instanceof EndParenToken || t instanceof GrouperToken) {
            processEndParentToken(t);
        } else if (t instanceof CommaToken) {
            result.append(getStringTab(numberTab));
            result.append(t.toString()).append(" ");
        }//dont add white space
        else if (t instanceof ValueToken) {
            processValueToken(t);
        } else if (t instanceof DotDotToken) {
            processDotToken(t);
        } else if (t instanceof WordToken) {
            processWordToken((WordToken) t);
        } else if (t instanceof ClosingToken) {
            processClosingToken(t);
        } else if (t instanceof FunctionToken || t instanceof ProcedureToken) {
            processFunctionToken(t);
        } else if (t instanceof VarToken || t instanceof ConstToken ||
                t instanceof TypeToken) {
            processVarToken(t);
        } else if (t instanceof CommentToken) {
            processCommentToken(t);
        } else {
            result.append(getStringTab(numberTab));
            lastToken = t;
            result.append(t.toString()).append(" ");
        }

    }

    private void checkVarToken() { //include const name
        if (numVarToken > 0) {
            numberTab--;
            numVarToken--;
        }
    }

    private void processVarToken(Token t) {
        checkVarToken();
        result.append(getStringTab(numberTab));
        result.append(t.toString()).append("\n");
        numVarToken++;
        numberTab++;
        needTab = true;
    }

    private void processCommentToken(Token t) {
        result.append(getStringTab(numberTab));
        lastToken = t;
        needTab = true;
        result.append(t.toString());
    }

    private void processFunctionToken(Token t) {
        result.append(getStringTab(numberTab));
        checkVarToken();
        result.append("\n");
        result.append(t.toString()).append(" ");
    }

    private void processClosingToken(Token t) throws IOException {
        result.append(getStringTab(numberTab));
        Token t2 = lexer.yylex();
        if (t2 instanceof PeriodToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken) {
            result.append(t.toString());
        } else {
            result.append(t.toString()).append(" ");
        }
        lastToken = t;
        processToken(t2);
    }

    private void processWordToken(WordToken wordToken) throws IOException {
        result.append(getStringTab(numberTab));
        Token t2 = lexer.yylex();
        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken
                || t2 instanceof DotDotToken || t2 instanceof PeriodToken
                || t2 instanceof ColonToken)) {
            result.append(wordToken.getCode()).append(" ");
        } else {
            result.append(wordToken.getCode());
        }
        lastToken = wordToken;
        processToken(t2);
    }

    private void processDotToken(Token t) {
        result.append(getStringTab(numberTab));

        if (result.length() > 0) if (result.charAt(result.length() - 1) == ' ')
            result.deleteCharAt(result.length() - 1);

        result.append(t.toString());
        lastToken = t;
    }

    private void processValueToken(Token t) throws IOException {
        result.append(getStringTab(numberTab));
        Token t2 = lexer.yylex();
        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken)) {
            result.append(((ValueToken) t).toCode()).append(" ");
        } else {
            result.append(((ValueToken) t).toCode());
        }
        lastToken = t;
        processToken(t2);
    }

    private void processEndParentToken(Token t) {
        result.append(getStringTab(numberTab));
        if (result.length() > 1) {
            if (result.charAt(result.length() - 1) == ' ') {
                result.deleteCharAt(result.length() - 1);
            }
        }
        if (t instanceof EndParenToken) {
            result.append(((EndParenToken) t).toCode()).append(" ");
        } else {
            result.append(((GrouperToken) t).toCode()).append(" ");
        }
        lastToken = t;
    }

    private void processElseToken(Token t) {
        result.append(getStringTab(numberTab));
        result.append(t.toString()).append(" ");
        lastToken = t;
    }

    private void processDoToken(Token t) throws IOException {
        result.append(getStringTab(numberTab));
        if (t instanceof DoToken) result.append(((DoToken) t).toCode()).append(" ");
        if (t instanceof ThenToken) result.append(((ThenToken) t).toCode()).append(" ");
        if (t instanceof CaseToken) result.append(((CaseToken) t).toCode()).append(" ");
        lastToken = t;
        Token t2 = lexer.yylex();
        if (t2 instanceof BeginEndToken) {
            needTab = true;
            result.append("\n");
        }
        processToken(t2);
    }

    private void processSemicolonToken(Token t) {
        result.append(getStringTab(numberTab));
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) == ' ') {
                result.deleteCharAt(result.length() - 1);
            }
        }
        result.append(t.toString()).append("\n");
        needTab = true;
        lastToken = t;
    }

    private void processPeriodToken(Token root) throws IOException {
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) == ' ') {
                result.deleteCharAt(result.length() - 1);
            }
        }
        result.append(root.toString());
        Token child = lexer.yylex();
        if (!(child instanceof WordToken)) {
            result.append("\n");
        }
        needTab = true;
        lastToken = root;
        processToken(child);
    }

    private void processTypeToken(Token t) {
        result.append(getStringTab(numberTab));
        result.append("\n");
        result.append(t.toString()).append("\n");
        lastToken = t;
    }

    private void processRepeatToken(Token t) {
        result.append(getStringTab(numberTab));
        result.append(t.toString()).append("\n");
        numberTab++;
        needTab = true;
        lastToken = t;
    }

    private void processOptToken(Token t) {
        result.append(getStringTab(numberTab));
        String opt = t.toString();
        if ("+-*/".contains(opt)) {
            result.append(t.toString());
        } else {
            result.append(t.toString()).append(" ");
        }
        lastToken = t;
    }

    private void processOfToken(Token t) {
        result.append(getStringTab(numberTab));
        if (lastToken instanceof WordToken) {
            result.append(t.toString()).append("\n");
            numberTab++;
            needTab = true;
        } else {
            result.append(t.toString()).append(" ");
        }
        lastToken = t;
    }

    private void processBeginToken(Token t) {
        if (numVarToken > 0) result.append("\n");
        checkVarToken();
        result.append(getStringTab(numberTab));
        result.append(((GrouperToken) t).toCode()).append("\n");
        numberTab++;
        needTab = true;
        lastToken = t;
    }

    private void completeUntil(Token t) {
        if (numberTab > 0)
            numberTab--;
        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result.append("\n");
        }
        //tab
        result.append(getStringTab(numberTab) + t.toString() + " ");
    }

    private void processEnd(Token t) throws IOException {
        if (numberTab > 0)
            numberTab--;
        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result.append("\n");
        }
        //tab
        result.append(getStringTab(numberTab) + t.toString());
        //check some name
        Token t2 = lexer.yylex();
        if (!(t2 instanceof SemicolonToken || t2 instanceof PeriodToken)) {
            result.append("\n");
            needTab = true;
        }
        processToken(t2);
    }

    private void newLineAndTab(Token token) {
        result.append(token.toString() + "\n");
        numberTab++;
        needTab = true;
    }

    private void newLine(Token t) {
        lastToken = t;
        result.append(t.toString() + "\n");
    }

    private void newLineAndNewLineAndTab(Token token) {
        result.append("\n");
        result.append(token.toString() + "\n");
        numberTab++;
        needTab = true;
    }

    private void newSpace(Token token) {
        result.append(token.toString() + " ");
    }

    private StringBuilder getStringTab(int num) {
        StringBuilder res = new StringBuilder("");
        if (!needTab) {
            return res;
        }
        for (int i = 0; i < num; i++) {
            res.append("\t");
        }
        needTab = false; //reset tab
        return res;
    }

    private void completeStatement(Token token) {

    }

    private void completeWhile() {
    }

    private void completeIf() {
    }

    private void completeRepeat() {
    }

    private void completeFor() {
    }

    private void completeParent() {

    }

    private void insertNewLine() {
        result.append("\n");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
