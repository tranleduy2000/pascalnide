package com.duy.pascal.compiler.alogrithm;

import com.duy.interpreter.tokenizer.Lexer;
import com.duy.interpreter.tokens.EOF_Token;
import com.duy.interpreter.tokens.OperatorToken;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.WordToken;
import com.duy.interpreter.tokens.basic.ColonToken;
import com.duy.interpreter.tokens.basic.CommaToken;
import com.duy.interpreter.tokens.basic.ConstToken;
import com.duy.interpreter.tokens.basic.DoToken;
import com.duy.interpreter.tokens.basic.DotDotToken;
import com.duy.interpreter.tokens.basic.ElseToken;
import com.duy.interpreter.tokens.basic.FunctionToken;
import com.duy.interpreter.tokens.basic.OfToken;
import com.duy.interpreter.tokens.basic.PeriodToken;
import com.duy.interpreter.tokens.basic.ProcedureToken;
import com.duy.interpreter.tokens.basic.RepeatToken;
import com.duy.interpreter.tokens.basic.SemicolonToken;
import com.duy.interpreter.tokens.basic.ThenToken;
import com.duy.interpreter.tokens.basic.TypeToken;
import com.duy.interpreter.tokens.basic.UntilToken;
import com.duy.interpreter.tokens.basic.UsesToken;
import com.duy.interpreter.tokens.basic.VarToken;
import com.duy.interpreter.tokens.closing.ClosingToken;
import com.duy.interpreter.tokens.closing.EndParenToken;
import com.duy.interpreter.tokens.closing.EndToken;
import com.duy.interpreter.tokens.grouping.BeginEndToken;
import com.duy.interpreter.tokens.grouping.BracketedToken;
import com.duy.interpreter.tokens.grouping.CaseToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.ParenthesizedToken;
import com.duy.interpreter.tokens.grouping.RecordToken;
import com.duy.interpreter.tokens.value.ValueToken;
import com.js.interpreter.core.ScriptSource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * @author Duy
 */
public class AutoIndentCode {

    static String result = "";
    static Lexer lexer;
    private static int numberTab = 0;
    private static boolean needTab = false;
    private static Token lastToken;

    public static String format(String code) {
        //reset number tab
        numberTab = 0;
        //create new reader
        Reader reader = new StringReader(code);
        //create new lexer
//        long time = System.currentTimeMillis();
        lexer = new Lexer(reader, "pascal ", new ArrayList<ScriptSource>());
        String res = parse();
//        System.out.println("time parse = " + (System.currentTimeMillis() - time));
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
            } catch (IOException e) {
                return result;
            }
        }

    }

    private static void processToken(Token t) throws IOException {

        System.out.println(t.getClass().getSimpleName());
        if (t instanceof EOF_Token) {
            return;
        }
        //begin ... end; repeat ... until; case of ... end;
        if (t instanceof EndToken) {
            processEnd(t);
            return;
        } else if (t instanceof UntilToken) {
            processUntil(t);
            return;
        }

        result += getStringTab(numberTab);

        //? array of
        //begin ... end; repeat ... until; case of ... end; record ... end;
        if (t instanceof BeginEndToken || t instanceof RecordToken) {
            result += ((GrouperToken) t).toCode() + "\n";
            numberTab++;
            needTab = true;
            lastToken = t;
        } else if (t instanceof OfToken) {
            if (lastToken instanceof WordToken) {
                result += t.toString() + "\n";
                numberTab++;
                needTab = true;
            } else {
                result += t.toString() + " ";
            }
            lastToken = t;
        } else if (t instanceof OperatorToken) {
            String opt = t.toString();
            if ("+-*/".contains(opt)) {
                result += t.toString();
            } else {
                result += t.toString() + " ";
            }
            lastToken = t;
        } else if (t instanceof RepeatToken) {
            result += t.toString() + "\n";
            numberTab++;
            needTab = true;
            lastToken = t;
        } else if (t instanceof TypeToken) {
            result += "\n";
            result += t.toString() + "\n";
            lastToken = t;
        } else if (t instanceof ParenthesizedToken || t instanceof BracketedToken) {
            result += ((GrouperToken) t).toCode();
        } else if (t instanceof DoToken || t instanceof ThenToken || t instanceof CaseToken) {
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
        } else if (t instanceof SemicolonToken || t instanceof PeriodToken) { //new line
            if (result.length() > 0) {
                if (result.charAt(result.length() - 1) == ' ') {
                    result = result.substring(0, result.length() - 1);
                }
            }
            result += t.toString() + "\n";
            needTab = true;
            lastToken = t;
        } //dont add white space, remove last space char
        else if (t instanceof ElseToken) {
//            newLineAndNewLineAndTab(t);
            result += t.toString() + " ";
            lastToken = t;
        } else if (t instanceof EndParenToken || t instanceof GrouperToken) {
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
        } else if (t instanceof CommaToken) {
            result += t.toString() + " ";
        }//dont add white space
        else if (t instanceof ValueToken) {
            Token t2 = lexer.yylex();
            if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                    || t2 instanceof ClosingToken)) {
                result += ((ValueToken) t).toCode() + " ";
            } else result += ((ValueToken) t).toCode();
            lastToken = t;
            processToken(t2);
        } else if (t instanceof DotDotToken) {
            if (result.length() > 0) if (result.charAt(result.length() - 1) == ' ')
                result = result.substring(0, result.length() - 1);

            result += t.toString();
            lastToken = t;
        } else if (t instanceof PeriodToken) {
            result += t.toString();
        } else if (t instanceof WordToken) {
            Token t2 = lexer.yylex();
            if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
                    || t2 instanceof ClosingToken
                    || t2 instanceof DotDotToken || t2 instanceof PeriodToken
                    || t2 instanceof ColonToken)) {
                result += t.toString() + " ";
            } else result += t.toString();
            lastToken = t;
            processToken(t2);
        } else if (t instanceof ClosingToken) {
            Token t2 = lexer.yylex();
            if (t2 instanceof PeriodToken || t2 instanceof CommaToken
                    || t2 instanceof ClosingToken) {
                result += t.toString();
            } else result += t.toString() + " ";
            lastToken = t;
            processToken(t2);
        } else if (t instanceof FunctionToken || t instanceof ProcedureToken) {
            result += "\n";
            result += t.toString() + " ";
        }
        // TODO: 04-Mar-17 Uses, var, const every new line
        else if (t instanceof VarToken || t instanceof ConstToken || t instanceof UsesToken) {
            newLine(t);
        } else {
            lastToken = t;
            result += t.toString() + " ";
        }

    }

    private static void processUntil(Token t) {
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
            res += "  ";
        }
        needTab = false; //reset tab
        return res;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
