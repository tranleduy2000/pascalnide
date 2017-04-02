package com.duy.pascal.frontend.alogrithm;

import com.duy.pascal.backend.tokenizer.Lexer;
import com.duy.pascal.backend.tokens.CommentToken;
import com.duy.pascal.backend.tokens.EOF_Token;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
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

    private static String result = "";
    private static Lexer lexer;
    private static int numberTab = 0;
    private static boolean needTab = false;
    private static Token lastToken;
    private static int numVarToken = 0;

    public static String format(String code) {
        //reset number tab
        numberTab = 0;
        //create new reader
        Reader reader = new StringReader(code);
        //create new lexer
        lexer = new Lexer(reader, "pascal ", new ArrayList<ScriptSource>());
        String res = parse();
        return res;
    }

    private static String parse() {
        result = "";
        while (true) {
            try {
                Token t = lexer.yylex();
                if (t instanceof EOF_Token) {
                    return result;
                }
                processToken(t);
            } catch (Exception e) {
                return result;
            }
        }

    }

    private static void processToken(Token t) throws IOException {
        if (t instanceof EOF_Token || t instanceof GroupingExceptionToken) {
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
        } else if (t instanceof TypeToken) {
            processTypeToken(t);
        } else if (t instanceof ParenthesizedToken || t instanceof BracketedToken) {
            result += getStringTab(numberTab);
            result += ((GrouperToken) t).toCode();
        } else if (t instanceof DoToken || t instanceof ThenToken || t instanceof CaseToken) {
            processDoToken(t);
        } else if (t instanceof SemicolonToken || t instanceof PeriodToken) { //new line
            processSemicolonToken(t);
        } //dont add white space, remove last space char
        else if (t instanceof ElseToken) {
            processElseToken(t);
        } else if (t instanceof EndParenToken || t instanceof GrouperToken) {
            processEndParentToken(t);
        } else if (t instanceof CommaToken) {
            result += getStringTab(numberTab);
            result += t.toString() + " ";
        }//dont add white space
        else if (t instanceof ValueToken) {
            processValueToken(t);
        } else if (t instanceof DotDotToken) {
            processDotToken(t);
        } else if (t instanceof WordToken) {
            processWordToken(t);
        } else if (t instanceof ClosingToken) {
            processClosingToken(t);
        } else if (t instanceof FunctionToken || t instanceof ProcedureToken) {
            processFunctionToken(t);
        }
        // TODO: 04-Mar-17 Uses, var, const every new line
       /* else if (t instanceof VarToken || t instanceof ConstToken) {
            processVarToken(t);
        }*/ else if (t instanceof CommentToken) {
            processCommentToken(t);
        } else {
            result += getStringTab(numberTab);
            lastToken = t;
            result += t.toString() + " ";
        }

    }

    private static void checkVarToken() { //include const token
        if (numVarToken > 0) {
            numberTab--;
            numVarToken--;
        }
    }

    private static void processVarToken(Token t) {
        checkVarToken();
        result += getStringTab(numberTab);
        result += t.toString() + "\n";
        numVarToken++;
        numberTab++;
        needTab = true;
    }

    private static void processCommentToken(Token t) {
        result += getStringTab(numberTab);
        lastToken = t;
        needTab = true;
        result += t.toString();
    }

    private static void processFunctionToken(Token t) {
        result += getStringTab(numberTab);
        checkVarToken();
        result += "\n";
        result += t.toString() + " ";
    }

    private static void processClosingToken(Token t) throws IOException {
        result += getStringTab(numberTab);
        Token t2 = lexer.yylex();
        if (t2 instanceof PeriodToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken) {
            result += t.toString();
        } else result += t.toString() + " ";
        lastToken = t;
        processToken(t2);
    }

    private static void processWordToken(Token t) throws IOException {
        result += getStringTab(numberTab);
        Token t2 = lexer.yylex();
        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken
                || t2 instanceof DotDotToken || t2 instanceof PeriodToken
                || t2 instanceof ColonToken)) {
            result += t.toString() + " ";
        } else result += t.toString();
        lastToken = t;
        processToken(t2);
    }

    private static void processDotToken(Token t) {
        result += getStringTab(numberTab);

        if (result.length() > 0) if (result.charAt(result.length() - 1) == ' ')
            result = result.substring(0, result.length() - 1);

        result += t.toString();
        lastToken = t;
    }

    private static void processValueToken(Token t) throws IOException {
        result += getStringTab(numberTab);
        Token t2 = lexer.yylex();
        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                || t2 instanceof ClosingToken)) {
            result += ((ValueToken) t).toCode() + " ";
        } else result += ((ValueToken) t).toCode();
        lastToken = t;
        processToken(t2);
    }

    private static void processEndParentToken(Token t) {
        result += getStringTab(numberTab);
        if (result.length() > 1) {
            if (result.charAt(result.length() - 1) == ' ') {
                result = result.substring(0, result.length() - 1);
            }
        }
        if (t instanceof EndParenToken) {
            result += ((EndParenToken) t).toCode() + " ";
        } else {
            result += ((GrouperToken) t).toCode() + " ";
        }
        lastToken = t;
    }

    private static void processElseToken(Token t) {
        result += getStringTab(numberTab);
        result += t.toString() + " ";
        lastToken = t;
    }

    private static void processDoToken(Token t) throws IOException {
        result += getStringTab(numberTab);
        if (t instanceof DoToken) result += ((DoToken) t).toCode() + " ";
        if (t instanceof ThenToken) result += ((ThenToken) t).toCode() + " ";
        if (t instanceof CaseToken) result += ((CaseToken) t).toCode() + " ";
        lastToken = t;
        Token t2 = lexer.yylex();
        if (t2 instanceof BeginEndToken) {
            needTab = true;
            result += "\n";
        }
        processToken(t2);
    }

    private static void processSemicolonToken(Token t) {
        result += getStringTab(numberTab);
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) == ' ') {
                result = result.substring(0, result.length() - 1);
            }
        }
        result += t.toString() + "\n";
        needTab = true;
        lastToken = t;
    }

    private static void processPeriodToken(Token t) {
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) == ' ') {
                result = result.substring(0, result.length() - 1);
            }
        }
        result += t.toString() + "\n";
        needTab = true;
        lastToken = t;
    }

    private static void processTypeToken(Token t) {
        result += getStringTab(numberTab);
        result += "\n";
        result += t.toString() + "\n";
        lastToken = t;
    }

    private static void processRepeatToken(Token t) {
        result += getStringTab(numberTab);
        result += t.toString() + "\n";
        numberTab++;
        needTab = true;
        lastToken = t;
    }

    private static void processOptToken(Token t) {
        result += getStringTab(numberTab);
        String opt = t.toString();
        if ("+-*/".contains(opt)) {
            result += t.toString();
        } else {
            result += t.toString() + " ";
        }
        lastToken = t;
    }

    private static void processOfToken(Token t) {
        result += getStringTab(numberTab);
        if (lastToken instanceof WordToken) {
            result += t.toString() + "\n";
            numberTab++;
            needTab = true;
        } else {
            result += t.toString() + " ";
        }
        lastToken = t;
    }

    private static void processBeginToken(Token t) {
        if (numVarToken > 0) result += "\n";
        checkVarToken();
        result += getStringTab(numberTab);
        result += ((GrouperToken) t).toCode() + "\n";
        numberTab++;
        needTab = true;
        lastToken = t;
    }

    private static void completeUntil(Token t) {
        if (numberTab > 0)
            numberTab--;
        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result += "\n";
        }
        //tab
        result += getStringTab(numberTab) + t.toString() + " ";
    }

    private static void processEnd(Token t) throws IOException {
        if (numberTab > 0)
            numberTab--;
        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result += "\n";
        }
        //tab
        result += getStringTab(numberTab) + t.toString();
        //check some token
        Token t2 = lexer.yylex();
        if (!(t2 instanceof SemicolonToken || t2 instanceof PeriodToken)) {
            result += "\n";
            needTab = true;
        }
        processToken(t2);
    }

    private static void newLineAndTab(Token token) {
        result += token.toString() + "\n";
        numberTab++;
        needTab = true;
    }

    private static void newLine(Token t) {
        lastToken = t;
        result += t.toString() + "\n";
    }

    private static void newLineAndNewLineAndTab(Token token) {
        result += "\n";
        result += token.toString() + "\n";
        numberTab++;
        needTab = true;
    }

    private static void newSpace(Token token) {
        result += token.toString() + " ";
    }

    private static String getStringTab(int num) {
        String res = "";
        if (!needTab) {
            return ""; //dont need tab
        }
        for (int i = 0; i < num; i++) {
            res += "\t";
        }
        needTab = false; //reset tab
        return res;
    }

    private static void completeStatement(Token token) {

    }

    private static void completeWhile() {
    }

    private static void completeIf() {
    }

    private static void completeRepeat() {
    }

    private static void completeFor() {
    }

    private static void completeParent() {

    }

    private static void insertNewLine() {
        result += "\n";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
