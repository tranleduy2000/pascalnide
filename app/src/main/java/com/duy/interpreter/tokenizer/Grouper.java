package com.duy.interpreter.tokenizer;


import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException;
import com.duy.interpreter.exceptions.grouping.EnumeratedGroupingException.GroupingExceptionTypes;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.tokens.EOF_Token;
import com.duy.interpreter.tokens.GroupingExceptionToken;
import com.duy.interpreter.tokens.OperatorToken;
import com.duy.interpreter.tokens.OperatorTypes;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.WordToken;
import com.duy.interpreter.tokens.basic.ArrayToken;
import com.duy.interpreter.tokens.basic.AssignmentToken;
import com.duy.interpreter.tokens.basic.ColonToken;
import com.duy.interpreter.tokens.basic.CommaToken;
import com.duy.interpreter.tokens.basic.ConstToken;
import com.duy.interpreter.tokens.basic.DoToken;
import com.duy.interpreter.tokens.basic.DowntoToken;
import com.duy.interpreter.tokens.basic.ElseToken;
import com.duy.interpreter.tokens.basic.ForToken;
import com.duy.interpreter.tokens.basic.ForwardToken;
import com.duy.interpreter.tokens.basic.FunctionToken;
import com.duy.interpreter.tokens.basic.IfToken;
import com.duy.interpreter.tokens.basic.OfToken;
import com.duy.interpreter.tokens.basic.PeriodToken;
import com.duy.interpreter.tokens.basic.ProcedureToken;
import com.duy.interpreter.tokens.basic.ProgramToken;
import com.duy.interpreter.tokens.basic.RepeatToken;
import com.duy.interpreter.tokens.basic.SemicolonToken;
import com.duy.interpreter.tokens.basic.ThenToken;
import com.duy.interpreter.tokens.basic.ToToken;
import com.duy.interpreter.tokens.basic.TypeToken;
import com.duy.interpreter.tokens.basic.UntilToken;
import com.duy.interpreter.tokens.basic.VarToken;
import com.duy.interpreter.tokens.basic.WhileToken;
import com.duy.interpreter.tokens.grouping.BaseGrouperToken;
import com.duy.interpreter.tokens.grouping.BeginEndToken;
import com.duy.interpreter.tokens.grouping.BracketedToken;
import com.duy.interpreter.tokens.grouping.CaseToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.duy.interpreter.tokens.grouping.ParenthesizedToken;
import com.duy.interpreter.tokens.grouping.RecordToken;
import com.duy.interpreter.tokens.value.BooleanToken;
import com.duy.interpreter.tokens.value.CharacterToken;
import com.duy.interpreter.tokens.value.DoubleToken;
import com.duy.interpreter.tokens.value.IntegerToken;
import com.duy.interpreter.tokens.value.StringToken;
import com.js.interpreter.core.ScriptSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.List;
import java.util.Stack;

public class Grouper implements Runnable {
    public BaseGrouperToken token_queue;
    String sourcename;
    Stack<Reader> readers;
    Stack<GrouperToken> groupers;
    Stack<StreamTokenizer> tokenizers;
    List<ScriptSource> searchDirectories;

    public Grouper(Reader reader, String sourcename,
                   List<ScriptSource> searchDirectories) throws GroupingException {
        this.sourcename = sourcename;
        token_queue = new BaseGrouperToken(new LineInfo(0, sourcename));
        groupers = new Stack<GrouperToken>();
        tokenizers = new Stack<StreamTokenizer>();
        readers = new Stack<Reader>();
        groupers.push(token_queue);
        readers.push(reader);
        tokenizers.push(getTokenizer(reader));
        this.searchDirectories = searchDirectories;
    }

    private StreamTokenizer getTokenizer(Reader r) {
        StreamTokenizer tokenizer = new StreamTokenizer(r);
        tokenizer.slashStarComments(true);
        tokenizer.slashSlashComments(true);
        tokenizer.ordinaryChar('\"');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('.');
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('+');
        tokenizer.ordinaryChar('{');
        tokenizer.ordinaryChar('}');
        tokenizer.wordChars('_', '_');
        tokenizer.eolIsSignificant(false);
        tokenizer.lowerCaseMode(true);
        return tokenizer;
    }

    void TossException(GroupingExceptionToken t) {
        for (GrouperToken g : groupers) {
            g.put(t);
        }
    }

    public void parse() {
        OperatorTypes temp_type = null;
        do_loop_break:
        do {
            GrouperToken top_of_stack = groupers.peek();
            StreamTokenizer tokenizer = tokenizers.peek();
            LineInfo line = new LineInfo(tokenizer.lineno(), sourcename);
            Token next_token = null;
            try {
                int nextToken = tokenizer.nextToken();
                switch (nextToken) {
                    case StreamTokenizer.TT_EOF:
                        readers.pop().close();
                        tokenizers.pop();
                        if (tokenizers.size() == 0) {
                            Token end = null;
                            if (groupers.size() != 1) {
                                if (top_of_stack instanceof ParenthesizedToken) {
                                    TossException(new GroupingExceptionToken(
                                            top_of_stack.lineInfo,
                                            GroupingExceptionTypes.UNFINISHED_PARENS));
                                } else if (top_of_stack instanceof BeginEndToken) {
                                    TossException(new GroupingExceptionToken(
                                            top_of_stack.lineInfo,
                                           GroupingExceptionTypes.UNFINISHED_BEGIN_END));
                                } else {
                                    TossException(new GroupingExceptionToken(
                                            top_of_stack.lineInfo,
                                            GroupingExceptionTypes.UNFINISHED_CONSTRUCT));
                                }
                                return;
                            } else {
                                top_of_stack.put(new EOF_Token(line));
                            }
                            return;
                        } else {
                            continue do_loop_break;
                        }
                    case StreamTokenizer.TT_WORD:
                        tokenizer.sval = tokenizer.sval.intern();
                        if (tokenizer.sval == "begin") {
                            BeginEndToken tmp = new BeginEndToken(line);
                            top_of_stack.put(tmp);
                            groupers.push(tmp);
                            continue do_loop_break;
                        } else if (tokenizer.sval == "record") {
                            RecordToken tmp = new RecordToken(line);
                            top_of_stack.put(tmp);
                            groupers.push(tmp);
                            continue do_loop_break;
                        } else if (tokenizer.sval == "end") {
                            if (groupers.peek() instanceof BeginEndToken
                                    || groupers.peek() instanceof RecordToken
                                    || groupers.peek() instanceof CaseToken) {
                                top_of_stack.put(new EOF_Token(line));
                                groupers.pop();
                                continue do_loop_break;
                            } else {
                                TossException(new GroupingExceptionToken(line,
                                        GroupingExceptionTypes.MISMATCHED_BEGIN_END));
                                return;
                            }
                        } else if (tokenizer.sval == "if") {
                            next_token = new IfToken(line);
                        } else if (tokenizer.sval == "then") {
                            next_token = new ThenToken(line);
                        } else if (tokenizer.sval == "while") {
                            next_token = new WhileToken(line);
                        } else if (tokenizer.sval == "do") {
                            next_token = new DoToken(line);
                        } else if (tokenizer.sval == "and") {
                            temp_type = OperatorTypes.AND;
                        } else if (tokenizer.sval == "not") {
                            temp_type = OperatorTypes.NOT;
                        } else if (tokenizer.sval == "or") {
                            temp_type = OperatorTypes.OR;
                        } else if (tokenizer.sval == "var") {
                            next_token = new VarToken(line);
                        } else if (tokenizer.sval == "type") {
                            next_token = new TypeToken(line);
                        } else if (tokenizer.sval == "xor") {
                            temp_type = OperatorTypes.XOR;
                        } else if (tokenizer.sval == "shl") {
                            temp_type = OperatorTypes.SHIFTLEFT;
                        } else if (tokenizer.sval == "shr") {
                            temp_type = OperatorTypes.SHIFTRIGHT;
                        } else if (tokenizer.sval == "div") {
                            temp_type = OperatorTypes.DIV;
                        } else if (tokenizer.sval == "mod") {
                            temp_type = OperatorTypes.MOD;
                        } else if (tokenizer.sval == "procedure") {
                            next_token = new ProcedureToken(line);
                        } else if (tokenizer.sval == "function") {
                            next_token = new FunctionToken(line);
                        } else if (tokenizer.sval == "program") {
                            next_token = new ProgramToken(line);
                        } else if (tokenizer.sval == "else") {
                            next_token = new ElseToken(line);
                        } else if (tokenizer.sval == "for") {
                            next_token = new ForToken(line);
                        } else if (tokenizer.sval == "to") {
                            next_token = new ToToken(line);
                        } else if (tokenizer.sval == "downto") {
                            next_token = new DowntoToken(line);
                        } else if (tokenizer.sval == "repeat") {
                            next_token = new RepeatToken(line);
                        } else if (tokenizer.sval == "until") {
                            next_token = new UntilToken(line);
                        } else if (tokenizer.sval == "case") {
                            CaseToken tmp = new CaseToken(line);
                            top_of_stack.put(tmp);
                            groupers.push(tmp);
                            continue do_loop_break;
                        } else if (tokenizer.sval == "of") {
                            next_token = new OfToken(line);
                        } else if (tokenizer.sval == "const") {
                            next_token = new ConstToken(line);
                        } else if (tokenizer.sval == "false") {
                            next_token = new BooleanToken(line, false);
                        } else if (tokenizer.sval == "true") {
                            next_token = new BooleanToken(line, true);
                        } else if (tokenizer.sval == "forward") {
                            next_token = new ForwardToken(line);
                        } else if (tokenizer.sval == "array") {
                            next_token = new ArrayToken(line);
                        } else {
                            next_token = new WordToken(line, tokenizer.sval);
                        }
                        break;
                    case StreamTokenizer.TT_NUMBER:
                        if (((int) tokenizer.nval) == tokenizer.nval) {
                            next_token = new IntegerToken(line,
                                    (int) tokenizer.nval);
                        } else {
                            next_token = new DoubleToken(line, tokenizer.nval);
                        }
                        break;
                    case ';':
                        next_token = new SemicolonToken(line);
                        break;
                    case '.':
                        next_token = new PeriodToken(line);
                        break;
                    case '\'':
                        if (tokenizer.sval.length() == 1) {
                            next_token = new CharacterToken(line,
                                    tokenizer.sval.charAt(0));
                        } else {
                            next_token = new StringToken(line, tokenizer.sval);
                        }
                        break;
                    case '(':
                        ParenthesizedToken p_token = new ParenthesizedToken(line);
                        top_of_stack.put(p_token);
                        groupers.push(p_token);
                        continue do_loop_break;
                    case ')':
                        if (!(groupers.pop() instanceof ParenthesizedToken)) {
                            TossException(new GroupingExceptionToken(line,
                                    GroupingExceptionTypes.MISMATCHED_PARENS));
                            return;

                        }
                        top_of_stack.put(new EOF_Token(line));
                        continue do_loop_break;
                    case '=':
                        temp_type = OperatorTypes.EQUALS;
                        break;
                    case ':':
                        int next = tokenizer.nextToken();
                        if (next == '=') {
                            next_token = new AssignmentToken(line);
                        } else {
                            tokenizer.pushBack();
                            next_token = new ColonToken(line);
                        }
                        break;
                    case '/':
                        temp_type = OperatorTypes.DIVIDE;
                        break;
                    case '*':
                        temp_type = OperatorTypes.MULTIPLY;
                        break;
                    case '+':
                        temp_type = OperatorTypes.PLUS;
                        break;
                    case '-':
                        temp_type = OperatorTypes.MINUS;
                        break;
                    case '<':
                        next = tokenizer.nextToken();
                        switch (next) {
                            case '>':
                                temp_type = OperatorTypes.NOTEQUAL;
                                break;
                            case '=':
                                temp_type = OperatorTypes.LESSEQ;
                                break;
                            default:
                                tokenizer.pushBack();
                                temp_type = OperatorTypes.LESSTHAN;
                        }
                        break;
                    case '>':
                        if (tokenizer.nextToken() == '=') {
                            temp_type = OperatorTypes.GREATEREQ;
                        } else {
                            tokenizer.pushBack();
                            temp_type = OperatorTypes.GREATERTHAN;
                        }
                        break;
                    case ',':
                        next_token = new CommaToken(line);
                        break;
                    case '[':
                        BracketedToken b_token = new BracketedToken(line);
                        top_of_stack.put(b_token);
                        groupers.push(b_token);
                        continue do_loop_break;
                    case ']':
                        if (!(groupers.pop() instanceof BracketedToken)) {
                            TossException(new GroupingExceptionToken(line,
                                    GroupingExceptionTypes.MISMATCHED_BRACKETS));
                            return;
                        }
                        top_of_stack.put(new EOF_Token(line));
                        continue do_loop_break;
                    case '{':
                        int firstToken = tokenizer.nextToken();
                        switch (firstToken) {
                            case '}':
                                continue do_loop_break;
                            case '$':
                                int secondToken = tokenizer.nextToken();
                                if (secondToken == StreamTokenizer.TT_WORD) {
                                    if (tokenizer.sval.equals("include")) {
                                        int thirdToken = tokenizer.nextToken();
                                        if (thirdToken == '\'') {
                                            addInclude(tokenizer.sval);
                                            continue do_loop_break;
                                        } else {
                                            System.err.println(line + ": $INCLUDE filename must be in single quotes");
                                        }
                                    } else {
                                        System.err.println(line + ": Warning! Unrecognized Compiler Directive!");
                                    }
                                }
                        }
                        tokenizer.ordinaryChar('\'');
                        while (tokenizer.nextToken() != '}')
                            ;
                        tokenizer.quoteChar('\'');
                        continue do_loop_break;
                    case '}':
                }

                if (temp_type != null) {
                    next_token = new OperatorToken(line, temp_type);
                    temp_type = null;
                }
                if (next_token != null) {
                    top_of_stack.put(next_token);
                }
            } catch (IOException e) {
                EnumeratedGroupingException g = new EnumeratedGroupingException(
                        line, GroupingExceptionTypes.IO_EXCEPTION);
                TossException(new GroupingExceptionToken(g));
                return;
            }
        } while (true);
    }

    void addInclude(String name) throws FileNotFoundException {
        for (ScriptSource s : searchDirectories) {
            Reader r = s.read(name);
            if (r != null) {
                tokenizers.push(getTokenizer(r));
                readers.push(r);
                return;
            }
        }
        throw new FileNotFoundException("Cannot find the $INCLUDE file " + name);
    }

    @Override
    public void run() {
        parse();
    }
}
