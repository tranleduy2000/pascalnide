/*
 *  Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.tokenizer;

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
import com.duy.pascal.backend.tokens.basic.ForToken;
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
 * uses {@link IndentCode}
 */
@Deprecated
public class AutoIndentCode {

    private StringBuilder result = new StringBuilder();
    private Lexer lexer;
    private int numberTab = 0;
    private boolean needTab = false;
    private Token lastToken;
    private int numVarToken = 0;
    private int increase = 1;
    private int decrease = 1;
    private int lastThenElse = 0;
    private int inCaseInstruction = 0;

    public boolean isLastThenElse() {
        return lastThenElse > 0;
    }

    public int increaseThenElseToken() {
        lastThenElse++;
        return lastThenElse;
    }

    public int decreaseThenElseToken() {
        if (lastThenElse > 0) {
            this.lastThenElse--;
        }
        return lastThenElse;
    }

    public String format(String code) {
        //reset number tab
        numberTab = 0;
        //create new reader
        Reader reader = new StringReader(code);
        //create new lexer
        lexer = new Lexer(reader, "pascal", new ArrayList<ScriptSource>());
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
            processEndToken(token);
            return;
        } else if (token instanceof UntilToken) {
            completeUntil(token);
            return;
        }

        //? array of
        //begin ... end; repeat ... until; case of ... end; record ... end;
        if (token instanceof BeginEndToken) {
            processBeginToken(token);
        } else if (token instanceof RecordToken) {
            processRecordToken((RecordToken) token);
        } else if (token instanceof OfToken) {
            processOfToken(token);
        } else if (token instanceof OperatorToken) {
            processOptToken(token);
        } else if (token instanceof RepeatToken) {
            processRepeatToken(token);
        } else if (token instanceof ParenthesizedToken
                || token instanceof BracketedToken) {
            result.append(getTab(numberTab));
            result.append(((GrouperToken) token).toCode());
        } else if (token instanceof DoToken) {
            processThenToken(token);
        } else if (token instanceof CaseToken) {
            processCaseToken(token);
        } else if (token instanceof ThenToken) {
            processThenToken(token);
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
        } else if (token instanceof ForToken) {
            result.append(getTab(numberTab));
            lastToken = token;
            result.append(token.toString()).append(" ");
        } else {
            result.append(getTab(numberTab));
            lastToken = token;
            result.append(token.toString()).append(" ");
        }

    }

    private void processRecordToken(RecordToken token) {
        result.append(token.toCode());
        result.append("\n");
        needTab = true;
        increaseTab();
    }


    private void checkVarToken() { //include const name
        if (numVarToken > 0) {
            decreaseTab();
            numVarToken--;
        }
    }

    private void processVarToken(Token t) {
        checkVarToken();
        result.append(getTab(numberTab));
        result.append(t.toString()).append("\n");
        numVarToken++;
        increaseTab();
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
        if ((t2 instanceof GrouperToken
                || t2 instanceof CommaToken
                || t2 instanceof ClosingToken
                || t2 instanceof DotDotToken
                || t2 instanceof PeriodToken
                || t2 instanceof ColonToken)) {
            result.append(wordToken.getCode());
        } else {
            result.append(wordToken.getCode()).append(" ");
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
        decreaseTab();

        if (result.length() > 0 && result.charAt(result.length() - 1) != '\n') {
            result.append("\n");
        }
        result.append(getTab(numberTab));
        result.append(t.toString()).append(" ");


        Token child = lexer.yylex();

        if (child instanceof IfToken) {
            processToken(child);
        } else {
            if (child instanceof BeginEndToken) {
                result.append("\n");//new line
                needTab = true;
                increaseTab();
                //decrease = 2;
                processToken(child);
            } else {
                increaseThenElseToken();

                result.append("\n");//new line
                needTab = true;
                increaseTab();
                //decrease = 2;
                processToken(child);
            }
        }
    }

    private void processThenToken(Token thenToken) throws IOException {
        result.append(getTab(numberTab));
        result.append(thenToken.toString()).append(" ");

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
            increaseTab();
            lastToken = child;
        } else {
//            if (child instanceof WordToken) {
            result.append("\n"); //new line

            needTab = true;
            increaseTab();
            result.append(getTab(numberTab));

            result.append(child.toString()).append(" "); //append begin
            lastToken = child;

            increaseThenElseToken();
//            } else {
//                processToken(child);
//            }
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

    private void processCaseToken(Token t) throws IOException {
        result.append(getTab(numberTab));
        result.append(((CaseToken) t).toCode()).append(" ");

        lastToken = t;
        increaseCaseInstruction();
        Token child = lexer.yylex();
        if (child instanceof BeginEndToken) {
            needTab = true;
            result.append("\n");
        }
        processToken(child);
    }

    private void increaseCaseInstruction() {
        inCaseInstruction++;
    }

    private void processSemicolonToken(Token t) {
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) == ' ') {
                result.deleteCharAt(result.length() - 1);
            }
        }
        result.append(getTab(numberTab));
        result.append(t.toString()).append("\n");
        needTab = true;
        if (isLastThenElse()) {
            decreaseTab();
            decreaseThenElseToken();
        }
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
            needTab = true;
            lastToken = root;
        }
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
        increaseTab();
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
            increaseTab();
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
        increaseTab();
        needTab = true;
        lastToken = t;
    }

    private void completeUntil(Token t) {
        decreaseTab();

        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result.append("\n");
        }
        //tab
        result.append(getTab(numberTab)).append(t.toString()).append(" ");
    }

    private void processEndToken(Token t) throws IOException {
        //new line
        if (result.length() > 0) {
            if (result.charAt(result.length() - 1) != '\n')
                result.append("\n");
        }
        if (getInCaseInstruction()) {
            decreaseTab();
            decreaseCaseInstruction();
        } else {
            decreaseTab();
        }

        //tab
        result.append(getTab(numberTab)).append(t.toString());
        //check some name
        Token t2 = lexer.yylex();
        if (!(t2 instanceof SemicolonToken || t2 instanceof PeriodToken)) {
            result.append("\n");
            needTab = true;
        }
        processToken(t2);
    }

    private void decreaseCaseInstruction() {
        if (inCaseInstruction > 0) inCaseInstruction--;
    }

    private void decreaseTab() {
        if (numberTab - increase >= 0) {
            numberTab -= decrease;
            decrease = 1; //reset if need
        }
    }

    private void increaseTab() {
        numberTab += increase;
        increase = 1;
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public boolean getInCaseInstruction() {
        return inCaseInstruction > 0;
    }
}
