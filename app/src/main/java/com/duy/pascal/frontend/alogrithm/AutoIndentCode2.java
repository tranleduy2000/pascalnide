//package com.duy.pascal.frontend.alogrithm;
//
//import com.duy.pascal.backend.tokenizer.Lexer;
//import com.duy.pascal.backend.tokens.CommentToken;
//import com.duy.pascal.backend.tokens.EOFToken;
//import com.duy.pascal.backend.tokens.GroupingExceptionToken;
//import com.duy.pascal.backend.tokens.OperatorToken;
//import com.duy.pascal.backend.tokens.Token;
//import com.duy.pascal.backend.tokens.WordToken;
//import com.duy.pascal.backend.tokens.basic.ColonToken;
//import com.duy.pascal.backend.tokens.basic.CommaToken;
//import com.duy.pascal.backend.tokens.basic.ConstToken;
//import com.duy.pascal.backend.tokens.basic.DoToken;
//import com.duy.pascal.backend.tokens.basic.DotDotToken;
//import com.duy.pascal.backend.tokens.basic.ElseToken;
//import com.duy.pascal.backend.tokens.basic.FunctionToken;
//import com.duy.pascal.backend.tokens.basic.IfToken;
//import com.duy.pascal.backend.tokens.basic.OfToken;
//import com.duy.pascal.backend.tokens.basic.PeriodToken;
//import com.duy.pascal.backend.tokens.basic.ProcedureToken;
//import com.duy.pascal.backend.tokens.basic.RepeatToken;
//import com.duy.pascal.backend.tokens.basic.SemicolonToken;
//import com.duy.pascal.backend.tokens.basic.ThenToken;
//import com.duy.pascal.backend.tokens.basic.TypeToken;
//import com.duy.pascal.backend.tokens.basic.UntilToken;
//import com.duy.pascal.backend.tokens.basic.VarToken;
//import com.duy.pascal.backend.tokens.closing.ClosingToken;
//import com.duy.pascal.backend.tokens.closing.EndParenToken;
//import com.duy.pascal.backend.tokens.closing.EndToken;
//import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
//import com.duy.pascal.backend.tokens.grouping.BracketedToken;
//import com.duy.pascal.backend.tokens.grouping.CaseToken;
//import com.duy.pascal.backend.tokens.grouping.GrouperToken;
//import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
//import com.duy.pascal.backend.tokens.grouping.RecordToken;
//import com.duy.pascal.backend.tokens.value.ValueToken;
//import com.js.interpreter.core.ScriptSource;
//
//import java.io.IOException;
//import java.io.Reader;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.Iterator;
//
///**
// * @author Duy
// */
//public class AutoIndentCode2 {
//
//    private StringBuilder result = new StringBuilder();
//    private Lexer lexer;
//    private int numberTab = 0;
//    private boolean needTab = false;
//    private Token lastToken;
//    private int numVarToken = 0;
//
//    public static void main(String[] args) {
//        String code = "var\n" +
//                "  f: text;\n" +
//                "  n: longint;\n" +
//                "  a, b: array[1..10000] of longint;\n" +
//                "\n" +
//                "procedure readf;\n" +
//                "var\n" +
//                "  i: longint;\n" +
//                "begin\n" +
//                "  assign(f, 'file.inp');\n" +
//                "  reset(f);\n" +
//                "  readln(f, n);\n" +
//                "  for i := 1 to n do read(f, a[i]);\n" +
//                "  for i := 1 to n do read(f, b[i]);\n" +
//                "  close(f);\n" +
//                "end;\n" +
//                "\n" +
//                "procedure sorta(d, c: longint);\n" +
//                "var\n" +
//                "  i, j, key, tmp: longint;\n" +
//                "begin\n" +
//                "  i := d;\n" +
//                "  j := c;\n" +
//                "  key := a[(d + c) div 2];\n" +
//                "  repeat\n" +
//                "    while a[i] < key do inc(i);\n" +
//                "    while a[j] > key do dec(j);\n" +
//                "    if i <= j then\n" +
//                "    begin\n" +
//                "      if i < j then\n" +
//                "      begin\n" +
//                "        tmp := a[i];\n" +
//                "        a[i] := a[j];\n" +
//                "        a[j] := tmp;\n" +
//                "      end;\n" +
//                "      inc(i);\n" +
//                "      dec(j);\n" +
//                "    end;\n" +
//                "  until i > j;\n" +
//                "\n" +
//                "  if (i < c) then sorta(i, c);\n" +
//                "  if (d < j) then sorta(d, j);\n" +
//                "end;\n" +
//                "\n" +
//                "procedure sortb(d, c: longint);\n" +
//                "var\n" +
//                "  i, j, key, tmp: longint;\n" +
//                "begin\n" +
//                "  i := d;\n" +
//                "  j := c;\n" +
//                "  key := b[(d + c) div 2];\n" +
//                "  repeat\n" +
//                "    while b[i] < key do inc(i);\n" +
//                "    while b[j] > key do         dec(j);\n" +
//                "    if i <= j then\n" +
//                "    begin\n" +
//                "      if i < j then\n" +
//                "      begin\n" +
//                "        tmp := b[i];\n" +
//                "       b[i] :=b[j];\n" +
//                "        b[j] := tmp;\n" +
//                "      end;\n" +
//                "      inc(i);\n" +
//                "      dec(j);\n" +
//                "    end;\n" +
//                "  until i > j;\n" +
//                "  if (i < c) then sortb(i, c);\n" +
//                "  if (d < j) then sortb(d, j);\n" +
//                "end;\n" +
//                "\n" +
//                "function getmax(a, b: longint): longint;\n" +
//                "begin\n" +
//                "  if a > b then getmax := a else getmax := b;\n" +
//                "end;\n" +
//                "\n" +
//                "\n" +
//                "procedure process;\n" +
//                "var\n" +
//                "  count , i, j, max: longint;\n" +
//                "begin\n" +
//                "  sorta(1, n);\n" +
//                "  sortb(1, n);\n" +
//                "  i := 1;\n" +
//                "  j := 1;\n" +
//                "  count := 1;\n" +
//                "  max := getmax(a[1], b[1]);\n" +
//                "  while (i <= n) and (j <= n) do\n" +
//                "  begin\n" +
//                "    while (a[i] <= max) and (i <= n) do inc(i);\n" +
//                "    while (b[j] <= max) and (j <= n) do inc(j);\n" +
//                "    if (i > n) or (j > n) then break;\n" +
//                "    inc(count);\n" +
//                "    max := getmax(a[i], b[j]);\n" +
//                "  end;\n" +
//                "  write(count);\n" +
//                "  readln;\n" +
//                "end;\n" +
//                "\n" +
//                "begin\n" +
//                "  readf;\n" +
//                "  process;\n" +
//                "end.\n" +
//                "\n";
//        AutoIndentCode2 autoIndentCode = new AutoIndentCode2();
//        String format = autoIndentCode.format(code);
//        System.out.println(format);
//    }
//
//    public String format(String code) {
//        //reset number tab
//        numberTab = 0;
//        //create new reader
//        Reader reader = new StringReader(code);
//        //create new lexer
//        lexer = new Lexer(reader, "pascal ", new ArrayList<ScriptSource>());
//        return parse();
//    }
//
//    private String parse() {
//        result = new StringBuilder("");
//        while (true) {
//            try {
//                Token t = lexer.yylex();
//                if (t instanceof EOFToken) {
//                    return result.toString();
//                }
//                processToken(t);
//            } catch (IOException e) {
//                return result.toString();
//            }
//        }
//
//    }
//
//    private StringBuilder processToken(Token lastToken, Token currentToken, int depth) throws IOException {
//        if (currentToken instanceof EOFToken || currentToken instanceof GroupingExceptionToken) {
//            return new StringBuilder();
//        }
//        //begin ... end; repeat ... until; case of ... end;
//        if (currentToken instanceof EndToken) {
//            return processEnd(currentToken);
//        } else if (currentToken instanceof UntilToken) {
//            return completeUntil(currentToken);
//
//        }
//
//        //? array of
//        //begin ... end; repeat ... until; case of ... end; record ... end;
//        if (currentToken instanceof BeginEndToken || currentToken instanceof RecordToken) {
//            processBeginToken(currentToken);
//        } else if (currentToken instanceof OfToken) {
//            processOfToken(currentToken);
//        } else if (currentToken instanceof OperatorToken) {
//            processOptToken(currentToken);
//        } else if (currentToken instanceof RepeatToken) {
//            processRepeatToken(currentToken);
//        } else if (currentToken instanceof ParenthesizedToken || currentToken instanceof BracketedToken) {
//            result.append(getTab(numberTab));
//            result.append(((GrouperToken) currentToken).toCode());
//        } else if (currentToken instanceof DoToken || currentToken instanceof CaseToken) {
//            processDoToken(currentToken);
//        } else if (currentToken instanceof ThenToken) {
//            processThenToken((ThenToken) currentToken);
//        } else if (currentToken instanceof PeriodToken) {
//            processPeriodToken(currentToken);
//        } else if (currentToken instanceof SemicolonToken) { //new line
//            processSemicolonToken(currentToken);
//        } //dont add white space, remove last space char
//        else if (currentToken instanceof ElseToken) {
//            processElseToken((ElseToken) currentToken);
//        } else if (currentToken instanceof EndParenToken || currentToken instanceof GrouperToken) {
//            processEndParentToken(currentToken);
//        } else if (currentToken instanceof CommaToken) {
//            result.append(getTab(numberTab));
//            result.append(currentToken.toString()).append(" ");
//        }//don't add white space
//        else if (currentToken instanceof ValueToken) {
//            processValueToken(currentToken);
//        } else if (currentToken instanceof DotDotToken) {
//            processDotToken(currentToken);
//        } else if (currentToken instanceof WordToken) {
//            processWordToken((WordToken) currentToken);
//        } else if (currentToken instanceof ClosingToken) {
//            processClosingToken(currentToken);
//        } else if (currentToken instanceof FunctionToken || currentToken instanceof ProcedureToken) {
//            processFunctionToken(currentToken);
//        } else if (currentToken instanceof VarToken || currentToken instanceof ConstToken ||
//                currentToken instanceof TypeToken) {
//            processVarToken(currentToken);
//        } else if (currentToken instanceof CommentToken) {
//            processCommentToken(currentToken);
//        } else {
//            result.append(getTab(numberTab));
//            lastToken = currentToken;
//            result.append(currentToken.toString()).append(" ");
//        }
//
//    }
//
//    private void checkVarToken() { //include const name
//        if (numVarToken > 0) {
//            numberTab--;
//            numVarToken--;
//        }
//    }
//
//    private StringBuilder processVarToken(Token t) {
//        checkVarToken();
//        result.append(getTab(numberTab));
//        result.append(t.toString()).append("\n");
//        numVarToken++;
//        numberTab++;
//        needTab = true;
//    }
//
//    private StringBuilder processCommentToken(Token t) {
//        result.append(getTab(numberTab));
//        lastToken = t;
//        needTab = true;
//        result.append(t.toString());
//    }
//
//    private StringBuilder processFunctionToken(Token t) {
//        checkVarToken();
//        result.append(getTab(numberTab));
//        result.append("\n");
//        result.append(t.toString()).append(" ");
//    }
//
//    private StringBuilder processClosingToken(Token t) throws IOException {
//        result.append(getTab(numberTab));
//        Token t2 = lexer.yylex();
//        if (t2 instanceof PeriodToken || t2 instanceof CommaToken
//                || t2 instanceof ClosingToken) {
//            result.append(t.toString());
//        } else {
//            result.append(t.toString()).append(" ");
//        }
//        lastToken = t;
//        processToken(t2);
//    }
//
//    private StringBuilder processWordToken(WordToken wordToken) throws IOException {
//        result.append(getTab(numberTab));
//        Token t2 = lexer.yylex();
//        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
//                || t2 instanceof ClosingToken
//                || t2 instanceof DotDotToken || t2 instanceof PeriodToken
//                || t2 instanceof ColonToken)) {
//            result.append(wordToken.getCode()).append(" ");
//        } else {
//            result.append(wordToken.getCode());
//        }
//        lastToken = wordToken;
//        processToken(t2);
//    }
//
//    private StringBuilder processDotToken(Token t) {
//        result.append(getTab(numberTab));
//
//        if (result.length() > 0) if (result.charAt(result.length() - 1) == ' ')
//            result.deleteCharAt(result.length() - 1);
//
//        result.append(t.toString());
//        lastToken = t;
//    }
//
//    private StringBuilder processValueToken(Token t) throws IOException {
//        result.append(getTab(numberTab));
//        Token t2 = lexer.yylex();
//        if (!(t2 instanceof GrouperToken || t2 instanceof CommaToken
//                || t2 instanceof ClosingToken)) {
//            result.append(((ValueToken) t).toCode()).append(" ");
//        } else {
//            result.append(((ValueToken) t).toCode());
//        }
//        lastToken = t;
//        processToken(t2);
//    }
//
//    private StringBuilder processEndParentToken(Token t) {
//        result.append(getTab(numberTab));
//        if (result.length() > 1) {
//            if (result.charAt(result.length() - 1) == ' ') {
//                result.deleteCharAt(result.length() - 1);
//            }
//        }
//        if (t instanceof EndParenToken) {
//            result.append(((EndParenToken) t).toCode()).append(" ");
//        } else {
//            result.append(((GrouperToken) t).toCode()).append(" ");
//        }
//        lastToken = t;
//    }
//
//    private StringBuilder processElseToken(ElseToken t) throws IOException {
//        needTab = true;
//        if (numberTab > 0) {
//            numberTab--;
//        }
//        if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
//            result.append("\n");
//        }
//        result.append(getTab(numberTab));
//        result.append(t.toString()).append(" ");
//        Token child = lexer.yylex();
//        if (child instanceof IfToken) {
//            processToken(child);
//        } else {
//            result.append("\n");//new line
//            needTab = true;
//            numberTab++;
//            processToken(child);
//        }
//    }
//
//    private StringBuilder processThenToken(ThenToken thenToken) throws IOException {
//        result.append(getTab(numberTab));
//        result.append(thenToken.toCode()).append(" ");
//
//        /*
//          if 1 < 2 then
//          begin
//              writeln();
//              write();
//          end
//          else
//          begin
//              writeln();
//              write();
//          end;
//          */
//        Token child = lexer.yylex(); //check begin token
//        if (child instanceof BeginEndToken) {
//            result.append("\n"); //new line
//
//            needTab = true;
//            result.append(getTab(numberTab));
//
//            result.append(((BeginEndToken) child).toCode()); //append begin
//            result.append("\n"); //new line
//
//            needTab = true;
//            numberTab++;
//            lastToken = child;
//        } else {
//            if (child instanceof WordToken) {
//                result.append("\n"); //new line
//
//                needTab = true;
//                numberTab++;
//                result.append(getTab(numberTab));
//
//                result.append(((WordToken) child).orginalName); //append begin
//                lastToken = child;
//            } else {
//                processToken(child);
//            }
//        }
//    }
//
//    private StringBuilder processDoToken(Token t) throws IOException {
//        result.append(getTab(numberTab));
//        if (t instanceof DoToken) result.append(((DoToken) t).toCode()).append(" ");
//        if (t instanceof CaseToken) result.append(((CaseToken) t).toCode()).append(" ");
//        lastToken = t;
//        Token t2 = lexer.yylex();
//        if (t2 instanceof BeginEndToken) {
//            needTab = true;
//            result.append("\n");
//        }
//        processToken(t2);
//    }
//
//    private StringBuilder processSemicolonToken(Token t) {
//        result.append(getTab(numberTab));
//        if (result.length() > 0) {
//            if (result.charAt(result.length() - 1) == ' ') {
//                result.deleteCharAt(result.length() - 1);
//            }
//        }
//        result.append(t.toString()).append("\n");
//        needTab = true;
//        lastToken = t;
//    }
//
//    private StringBuilder processPeriodToken(Token root) throws IOException {
//        if (result.length() > 0) {
//            if (result.charAt(result.length() - 1) == ' ') {
//                result.deleteCharAt(result.length() - 1);
//            }
//        }
//        result.append(root.toString());
//        Token child = lexer.yylex();
//        if (!(child instanceof WordToken)) {
//            result.append("\n");
//        }
//        needTab = true;
//        lastToken = root;
//        processToken(child);
//    }
//
//    private StringBuilder processTypeToken(Token t) {
//        result.append(getTab(numberTab));
//        result.append("\n");
//        result.append(t.toString()).append("\n");
//        lastToken = t;
//    }
//
//    private StringBuilder processRepeatToken(Token t) {
//        result.append(getTab(numberTab));
//        result.append(t.toString()).append("\n");
//        numberTab++;
//        needTab = true;
//        lastToken = t;
//    }
//
//    private StringBuilder processOptToken(Token t) {
//        result.append(getTab(numberTab));
//        String opt = t.toString();
//        if ("+-*/".contains(opt)) {
//            result.append(t.toString());
//        } else {
//            result.append(t.toString()).append(" ");
//        }
//        lastToken = t;
//    }
//
//    private StringBuilder processOfToken(Token t) {
//        result.append(getTab(numberTab));
//        if (lastToken instanceof WordToken) {
//            result.append(t.toString()).append("\n");
//            numberTab++;
//            needTab = true;
//        } else {
//            result.append(t.toString()).append(" ");
//        }
//        lastToken = t;
//    }
//
//    private StringBuilder processBeginToken(Token lastToken, BeginEndToken beginToken, int depth) {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append(beginToken.toCode());
//        stringBuilder.append("\n");
//
//        ArrayList<Token> childList = new ArrayList<>();
//        try {
//            Token next = lexer.yylex();
//            do {
//                childList.add(next);
//                next = lexer.yylex();
//            } while (next instanceof EndToken);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Iterator<Token> iterator = childList.iterator();
//        while (iterator.hasNext()) {
//            stringBuilder.append("");//depth
//            String token = getToken(childList.iterator(), depth + 1);
//            stringBuilder.append(token);
//        }
//        return stringBuilder;
//    }
//
//    private String getToken(Iterator<Token> iterator, int depth) {
//
//        return null;
//    }
//
//    private StringBuilder completeUntil(Token t) {
//        if (numberTab > 0)
//            numberTab--;
//        //new line
//        if (result.length() > 0) {
//            if (result.charAt(result.length() - 1) != '\n')
//                result.append("\n");
//        }
//        //tab
//        result.append(getTab(numberTab) + t.toString() + " ");
//    }
//
//    private StringBuilder processEnd(Token t) throws IOException {
//        if (numberTab > 0)
//            numberTab--;
//        //new line
//        if (result.length() > 0) {
//            if (result.charAt(result.length() - 1) != '\n')
//                result.append("\n");
//        }
//        //tab
//        result.append(getTab(numberTab) + t.toString());
//        //check some name
//        Token t2 = lexer.yylex();
//        if (!(t2 instanceof SemicolonToken || t2 instanceof PeriodToken)) {
//            result.append("\n");
//            needTab = true;
//        }
//        processToken(t2);
//    }
//
//
//    private StringBuilder getTab(int num) {
//        StringBuilder res = new StringBuilder("");
//        if (!needTab) {
//            return res;
//        }
//        for (int i = 0; i < num; i++) {
//            res.append("\t");
//        }
//        needTab = false; //reset tab
//        return res;
//    }
//
//    @Override
//    public String toString() {
//        return this.getClass().getSimpleName();
//    }
//
//}
