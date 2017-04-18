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
import com.duy.pascal.backend.tokens.basic.IfToken;
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

    public static void main(String[] args) {
        String code = "var\n" +
                "  f: text;\n" +
                "  n: longint;\n" +
                "  a, b: array[1..10000] of longint;\n" +
                "\n" +
                "procedure readf;\n" +
                "var\n" +
                "  i: longint;\n" +
                "begin\n" +
                "  assign(f, 'file.inp');\n" +
                "  reset(f);\n" +
                "  readln(f, n);\n" +
                "  for i := 1 to n do read(f, a[i]);\n" +
                "  for i := 1 to n do read(f, b[i]);\n" +
                "  close(f);\n" +
                "end;\n" +
                "\n" +
                "procedure sorta(d, c: longint);\n" +
                "var\n" +
                "  i, j, key, tmp: longint;\n" +
                "begin\n" +
                "  i := d;\n" +
                "  j := c;\n" +
                "  key := a[(d + c) div 2];\n" +
                "  repeat\n" +
                "    while a[i] < key do inc(i);\n" +
                "    while a[j] > key do dec(j);\n" +
                "    if i <= j then\n" +
                "    begin\n" +
                "      if i < j then\n" +
                "      begin\n" +
                "        tmp := a[i];\n" +
                "        a[i] := a[j];\n" +
                "        a[j] := tmp;\n" +
                "      end;\n" +
                "      inc(i);\n" +
                "      dec(j);\n" +
                "    end;\n" +
                "  until i > j;\n" +
                "\n" +
                "  if (i < c) then sorta(i, c);\n" +
                "  if (d < j) then sorta(d, j);\n" +
                "end;\n" +
                "\n" +
                "procedure sortb(d, c: longint);\n" +
                "var\n" +
                "  i, j, key, tmp: longint;\n" +
                "begin\n" +
                "  i := d;\n" +
                "  j := c;\n" +
                "  key := b[(d + c) div 2];\n" +
                "  repeat\n" +
                "    while b[i] < key do inc(i);\n" +
                "    while b[j] > key do         dec(j);\n" +
                "    if i <= j then\n" +
                "    begin\n" +
                "      if i < j then\n" +
                "      begin\n" +
                "        tmp := b[i];\n" +
                "       b[i] :=b[j];\n" +
                "        b[j] := tmp;\n" +
                "      end;\n" +
                "      inc(i);\n" +
                "      dec(j);\n" +
                "    end;\n" +
                "  until i > j;\n" +
                "  if (i < c) then sortb(i, c);\n" +
                "  if (d < j) then sortb(d, j);\n" +
                "end;\n" +
                "\n" +
                "function getmax(a, b: longint): longint;\n" +
                "begin\n" +
                "  if a > b then getmax := a else getmax := b;\n" +
                "end;\n" +
                "\n" +
                "\n" +
                "procedure process;\n" +
                "var\n" +
                "  count , i, j, max: longint;\n" +
                "begin\n" +
                "  sorta(1, n);\n" +
                "  sortb(1, n);\n" +
                "  i := 1;\n" +
                "  j := 1;\n" +
                "  count := 1;\n" +
                "  max := getmax(a[1], b[1]);\n" +
                "  while (i <= n) and (j <= n) do\n" +
                "  begin\n" +
                "    while (a[i] <= max) and (i <= n) do inc(i);\n" +
                "    while (b[j] <= max) and (j <= n) do inc(j);\n" +
                "    if (i > n) or (j > n) then break;\n" +
                "    inc(count);\n" +
                "    max := getmax(a[i], b[j]);\n" +
                "  end;\n" +
                "  write(count);\n" +
                "  readln;\n" +
                "end;\n" +
                "\n" +
                "begin\n" +
                "  readf;\n" +
                "  process;\n" +
                "end.\n" +
                "\n";
        AutoIndentCode autoIndentCode = new AutoIndentCode();
        String format = autoIndentCode.format(code);
        System.out.println(format);
    }

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
            } catch (IOException e) {
                return result.toString();
            }
        }

    }

    private void processToken(Token token) throws IOException {
        if (token instanceof EOFToken || token instanceof GroupingExceptionToken) {
            return;
        }
        //begin ... end; repeat ... until; case of ... end;
        if (token instanceof EndToken) {
            processEnd(token);
            return;
        } else if (token instanceof UntilToken) {
            completeUntil(token);
            return;
        }

        //? array of
        //begin ... end; repeat ... until; case of ... end; record ... end;
        if (token instanceof BeginEndToken || token instanceof RecordToken) {
            processBeginToken(token);
        } else if (token instanceof OfToken) {
            processOfToken(token);
        } else if (token instanceof OperatorToken) {
            processOptToken(token);
        } else if (token instanceof RepeatToken) {
            processRepeatToken(token);
        } else if (token instanceof ParenthesizedToken || token instanceof BracketedToken) {
            result.append(getTab(numberTab));
            result.append(((GrouperToken) token).toCode());
        } else if (token instanceof DoToken || token instanceof CaseToken) {
            processDoToken(token);
        } else if (token instanceof ThenToken) {
            processThenToken((ThenToken) token);
        } else if (token instanceof PeriodToken) {
            processPeriodToken(token);
        } else if (token instanceof SemicolonToken) { //new line
            processSemicolonToken(token);
        } //dont add white space, remove last space char
        else if (token instanceof ElseToken) {
            processElseToken((ElseToken) token);
        } else if (token instanceof EndParenToken || token instanceof GrouperToken) {
            processEndParentToken(token);
        } else if (token instanceof CommaToken) {
            result.append(getTab(numberTab));
            result.append(token.toString()).append(" ");
        }//don't add white space
        else if (token instanceof ValueToken) {
            processValueToken(token);
        } else if (token instanceof DotDotToken) {
            processDotToken(token);
        } else if (token instanceof WordToken) {
            processWordToken((WordToken) token);
        } else if (token instanceof ClosingToken) {
            processClosingToken(token);
        } else if (token instanceof FunctionToken || token instanceof ProcedureToken) {
            processFunctionToken(token);
        } else if (token instanceof VarToken || token instanceof ConstToken ||
                token instanceof TypeToken) {
            processVarToken(token);
        } else if (token instanceof CommentToken) {
            processCommentToken(token);
        } else {
            result.append(getTab(numberTab));
            lastToken = token;
            result.append(token.toString()).append(" ");
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
        result.append(getTab(numberTab));
        result.append(t.toString()).append("\n");
        numVarToken++;
        numberTab++;
        needTab = true;
    }

    private void processCommentToken(Token t) {
        result.append(getTab(numberTab));
        lastToken = t;
        needTab = true;
        result.append(t.toString());
    }

    private void processFunctionToken(Token t) {
        checkVarToken();
        result.append(getTab(numberTab));
        result.append("\n");
        result.append(t.toString()).append(" ");
    }

    private void processClosingToken(Token t) throws IOException {
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab));

        if (result.length() > 0) if (result.charAt(result.length() - 1) == ' ')
            result.deleteCharAt(result.length() - 1);

        result.append(t.toString());
        lastToken = t;
    }

    private void processValueToken(Token t) throws IOException {
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab));
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

    private void processElseToken(ElseToken t) throws IOException {
        needTab = true;
        if (numberTab > 0) {
            numberTab--;
        }
        if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
            result.append("\n");
        }
        result.append(getTab(numberTab));
        result.append(t.toString()).append(" ");
        Token child = lexer.yylex();
        if (child instanceof IfToken) {
            processToken(child);
        } else {
            result.append("\n");//new line
            needTab = true;
            numberTab++;
            processToken(child);
        }
    }

    private void processThenToken(ThenToken thenToken) throws IOException {
        result.append(getTab(numberTab));
        result.append(thenToken.toCode()).append(" ");

        /*
          if 1 < 2 then
          begin
              writeln();
              write();
          end
          else
          begin
              writeln();
              write();
          end;
          */
        Token child = lexer.yylex(); //check begin token
        if (child instanceof BeginEndToken) {
            result.append("\n"); //new line

            needTab = true;
            result.append(getTab(numberTab));

            result.append(((BeginEndToken) child).toCode()); //append begin
            result.append("\n"); //new line

            needTab = true;
            numberTab++;
            lastToken = child;
        } else {
            if (child instanceof WordToken) {
                result.append("\n"); //new line

                needTab = true;
                numberTab++;
                result.append(getTab(numberTab));

                result.append(((WordToken) child).orginalName); //append begin
                lastToken = child;
            } else {
                processToken(child);
            }
        }
    }

    private void processDoToken(Token t) throws IOException {
        result.append(getTab(numberTab));
        if (t instanceof DoToken) result.append(((DoToken) t).toCode()).append(" ");
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
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab));
        result.append("\n");
        result.append(t.toString()).append("\n");
        lastToken = t;
    }

    private void processRepeatToken(Token t) {
        result.append(getTab(numberTab));
        result.append(t.toString()).append("\n");
        numberTab++;
        needTab = true;
        lastToken = t;
    }

    private void processOptToken(Token t) {
        result.append(getTab(numberTab));
        String opt = t.toString();
        if ("+-*/".contains(opt)) {
            result.append(t.toString());
        } else {
            result.append(t.toString()).append(" ");
        }
        lastToken = t;
    }

    private void processOfToken(Token t) {
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab));
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
        result.append(getTab(numberTab) + t.toString() + " ");
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
        result.append(getTab(numberTab) + t.toString());
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

    private StringBuilder getTab(int num) {
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
