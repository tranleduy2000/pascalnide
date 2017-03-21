package com.duy.interpreter.tokens.grouping;


import com.duy.interpreter.exceptions.BadOperationTypeException;
import com.duy.interpreter.exceptions.ExpectedAnotherTokenException;
import com.duy.interpreter.exceptions.ExpectedTokenException;
import com.duy.interpreter.exceptions.MultipleDefaultValuesException;
import com.duy.interpreter.exceptions.NonConstantExpressionException;
import com.duy.interpreter.exceptions.NonIntegerIndexException;
import com.duy.interpreter.exceptions.NotAStatementException;
import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnconvertibleTypeException;
import com.duy.interpreter.exceptions.UnrecognizedTokenException;
import com.duy.interpreter.exceptions.grouping.GroupingException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.ArrayType;
import com.duy.interpreter.pascaltypes.BasicType;
import com.duy.interpreter.pascaltypes.DeclaredType;
import com.duy.interpreter.pascaltypes.RecordType;
import com.duy.interpreter.pascaltypes.RuntimeType;
import com.duy.interpreter.pascaltypes.SubrangeType;
import com.duy.interpreter.tokens.CommentToken;
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
import com.duy.interpreter.tokens.basic.DoToken;
import com.duy.interpreter.tokens.basic.DowntoToken;
import com.duy.interpreter.tokens.basic.ElseToken;
import com.duy.interpreter.tokens.basic.ForToken;
import com.duy.interpreter.tokens.basic.IfToken;
import com.duy.interpreter.tokens.basic.OfToken;
import com.duy.interpreter.tokens.basic.PeriodToken;
import com.duy.interpreter.tokens.basic.RepeatToken;
import com.duy.interpreter.tokens.basic.SemicolonToken;
import com.duy.interpreter.tokens.basic.ThenToken;
import com.duy.interpreter.tokens.basic.ToToken;
import com.duy.interpreter.tokens.basic.UntilToken;
import com.duy.interpreter.tokens.basic.WhileToken;
import com.duy.interpreter.tokens.value.ValueToken;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.NopInstruction;
import com.js.interpreter.ast.instructions.case_statement.CaseInstruction;
import com.js.interpreter.ast.instructions.conditional.DowntoForStatement;
import com.js.interpreter.ast.instructions.conditional.ForStatement;
import com.js.interpreter.ast.instructions.conditional.IfStatement;
import com.js.interpreter.ast.instructions.conditional.RepeatInstruction;
import com.js.interpreter.ast.instructions.conditional.WhileStatement;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FieldAccess;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.UnaryOperatorEvaluation;
import com.js.interpreter.ast.returnsvalue.operators.BinaryOperatorEvaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GrouperToken extends Token {
    LinkedBlockingQueue<Token> queue;
    Token next = null;

    public GrouperToken(LineInfo line) {
        super(line);
        queue = new LinkedBlockingQueue<Token>();
    }

    private Token get_next() throws GroupingException {
        if (next == null) {
            while (true) {
                try {
                    next = queue.take();
                } catch (InterruptedException e) {
                    continue;
                }
                break;
            }
        }
        exceptionCheck(next);
        return next;
    }

    public boolean hasNext() throws GroupingException {
        return !(get_next() instanceof EOF_Token);
    }

    private void exceptionCheck(Token t) throws GroupingException {
        if (t instanceof GroupingExceptionToken) {
            throw ((GroupingExceptionToken) t).exception;
        }
    }

    public void put(Token t) {
        while (true) {
            try {
                queue.put(t);
            } catch (InterruptedException e) {
                continue;
            }
            break;
        }
    }

    public abstract String toCode();

    public Token take() throws ExpectedAnotherTokenException, GroupingException {
        Token result = get_next();
        if (result instanceof EOF_Token) {
            throw new ExpectedAnotherTokenException(result.lineInfo);
        }
        while (true) {
            try {
                next = queue.take();
                exceptionCheck(next);
                return result;
            } catch (InterruptedException ignored) {
            }
        }
    }

    public Token peek() throws GroupingException {
        return get_next();
    }

    public Token peek_no_EOF() throws ExpectedAnotherTokenException, GroupingException {
        Token result = peek();
        if (result instanceof EOF_Token) {
            throw new ExpectedAnotherTokenException(result.lineInfo);
        }
        return result;
    }

    @Override
    public String toString() {
        try {
            return get_next().toString() + ',' + queue.toString();
        } catch (GroupingException e) {
            return "Exception: " + e.toString();
        }
    }

    public String next_word_value() throws ParsingException {
        return take().get_word_value().name;
    }

    public void assert_next_semicolon() throws ParsingException {
        Token t = take();
        if (!(t instanceof SemicolonToken)) {
            throw new ExpectedTokenException(";", t);
        }
    }

    public DeclaredType get_next_pascal_type(ExpressionContext context) throws ParsingException {
        Token n = take();
        if (n instanceof ArrayToken) {
            return getArrayType(context);
        }
        if (n instanceof RecordToken) {
            RecordToken r = (RecordToken) n;
            RecordType result = new RecordType();
            result.variable_types = r.get_variable_declarations(context);
            return result;
        }
        if (!(n instanceof WordToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        return ((WordToken) n).to_basic_type(context);
    }

    private DeclaredType getArrayType(ExpressionContext context) throws ParsingException {
        Token n = peek_no_EOF();
        if (n instanceof BracketedToken) {
            BracketedToken bracket = (BracketedToken) take();
            return getArrayType(bracket, context);
        } else if (n instanceof OfToken) {
            take();
            DeclaredType elementType = get_next_pascal_type(context);
            return new ArrayType<DeclaredType>(elementType, new SubrangeType());
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context) throws ParsingException {
        SubrangeType bound = new SubrangeType(bounds, context);
        DeclaredType elementType;
        if (bounds.hasNext()) {
            Token t = bounds.take();
            if (!(t instanceof CommaToken)) {
                throw new ExpectedTokenException("']' or ','", t);
            }
            elementType = getArrayType(bounds, context);
        } else {
            Token next = take();
            if (!(next instanceof OfToken)) {
                throw new ExpectedTokenException("of", next);
            }
            elementType = get_next_pascal_type(context);
        }
        return new ArrayType<DeclaredType>(elementType, bound);
    }

    public ReturnsValue getNextExpression(ExpressionContext context,
                                          precedence precedence, Token next) throws ParsingException {
        ReturnsValue nextTerm;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.can_be_unary()) {
                throw new BadOperationTypeException(next.lineInfo,
                        nextOperator.type);
            }
            nextTerm = new UnaryOperatorEvaluation(getNextExpression(context,
                    nextOperator.type.getPrecedence()), nextOperator.type,
                    nextOperator.lineInfo);
        } else {
            nextTerm = getNextTerm(context, next);
        }
        while ((next = peek()).getOperatorPrecedence() != null) {
            if (next instanceof OperatorToken) {
                OperatorToken nextOperator = (OperatorToken) next;
                if (nextOperator.type.getPrecedence().compareTo(precedence) >= 0) {
                    break;
                }
                take();
                ReturnsValue nextvalue = getNextExpression(context,
                        nextOperator.type.getPrecedence());
                OperatorTypes operationtype = ((OperatorToken) next).type;
                DeclaredType type1 = nextTerm.get_type(context).declType;
                DeclaredType type2 = nextvalue.get_type(context).declType;
                try {
                    operationtype.verifyOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.lineInfo, type1,
                            type2, nextTerm, nextvalue, operationtype);
                }
                nextTerm = BinaryOperatorEvaluation.generateOp(context,
                        nextTerm, nextvalue, operationtype,
                        nextOperator.lineInfo);
            } else if (next instanceof PeriodToken) {
                take();
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Element Name]", next);
                }
                nextTerm = new FieldAccess(nextTerm, (WordToken) next);
            } else if (next instanceof BracketedToken) {
                take();
                BracketedToken b = (BracketedToken) next;
                RuntimeType t = nextTerm.get_type(context);
                ReturnsValue v = b.getNextExpression(context);
                ReturnsValue converted = BasicType.Integer.convert(v, context);
                if (converted == null) {
                    throw new NonIntegerIndexException(v);
                }
                if (b.hasNext()) {
                    throw new ExpectedTokenException("]", b.take());
                }
                nextTerm = t.declType.generateArrayAccess(nextTerm, converted);
            }
        }
        return nextTerm;
    }

    public ReturnsValue getNextExpression(ExpressionContext context,
                                          precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public ReturnsValue getNextTerm(ExpressionContext context, Token next)
            throws ParsingException {
        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).get_single_value(context);
        } else if (next instanceof ValueToken) {
            return new ConstantAccess(((ValueToken) next).getValue(),
                    next.lineInfo);
        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();

            if (next instanceof ParenthesizedToken) {
                List<ReturnsValue> arguments = ((ParenthesizedToken) take())
                        .get_arguments_for_call(context);
                return FunctionCall.generate_function_call(name, arguments,
                        context);
            } else {
                return context.getIdentifierValue(name);
            }
        } else if (next instanceof CommentToken) {
            //unhandled comment token
            next = peek();
            return getNextTerm(context, next);
        } else {
            throw new UnrecognizedTokenException(next);
        }
    }

    public ReturnsValue getNextTerm(ExpressionContext context)
            throws ParsingException {
        return getNextTerm(context, take());
    }

    public ReturnsValue getNextExpression(ExpressionContext context)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence);
    }

    public ReturnsValue getNextExpression(ExpressionContext context, Token first)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence, first);
    }

    public List<VariableDeclaration> get_variable_declarations(
            ExpressionContext context) throws ParsingException {
        List<VariableDeclaration> result = new ArrayList<VariableDeclaration>();
        /*
         * reusing it, so it is further out of scope than necessary
		 */
        List<WordToken> names = new ArrayList<WordToken>();
        Token next;
        do {
            do {
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Variable Identifier]",
                            next);
                }
                names.add((WordToken) next);
                next = take();
            } while (next instanceof CommaToken);
            if (!(next instanceof ColonToken)) {
                throw new ExpectedTokenException(":", next);
            }
            DeclaredType type;
            type = get_next_pascal_type(context);

            Object defaultValue = null;
            if (peek() instanceof OperatorToken) {
                if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                    take();
                    ReturnsValue unconverted = getNextExpression(context);
                    ReturnsValue converted = type.convert(unconverted, context);
                    if (converted == null) {
                        throw new UnconvertibleTypeException(unconverted,
                                unconverted.get_type(context).declType, type,
                                true);
                    }
                    defaultValue = converted.compileTimeValue(context);
                    if (defaultValue == null) {
                        throw new NonConstantExpressionException(converted);
                    }
                    if (names.size() != 1) {
                        throw new MultipleDefaultValuesException(
                                converted.getLineNumber());
                    }
                }
            }
            assert_next_semicolon();
            for (WordToken s : names) {
                VariableDeclaration v = new VariableDeclaration(s.name, type,
                        defaultValue, s.lineInfo);
                context.verifyNonConflictingSymbol(v);
                result.add(v);
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    public ReturnsValue get_single_value(ExpressionContext context)
            throws ParsingException {
        ReturnsValue result = getNextExpression(context);
        if (hasNext()) {
            Token next = take();
            throw new ExpectedTokenException(getClosingText(), next);
        }
        return result;
    }

    public Executable get_next_command(ExpressionContext context)
            throws ParsingException {
        Token next = take();
        LineInfo initialline = next.lineInfo;
        if (next instanceof IfToken) {
            ReturnsValue condition = getNextExpression(context);
            next = take();
            assert (next instanceof ThenToken);
            Executable command = get_next_command(context);
            Executable else_command = null;
            next = peek();
            if (next instanceof ElseToken) {
                take();
                else_command = get_next_command(context);
            }
            return new IfStatement(condition, command, else_command,
                    initialline);
        } else if (next instanceof WhileToken) {
            ReturnsValue condition = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable command = get_next_command(context);
            return new WhileStatement(condition, command, initialline);
        } else if (next instanceof BeginEndToken) {
            InstructionGrouper begin_end_preprocessed = new InstructionGrouper(
                    initialline);
            BeginEndToken cast_token = (BeginEndToken) next;

            while (cast_token.hasNext()) {
                begin_end_preprocessed.add_command(cast_token
                        .get_next_command(context));
                if (cast_token.hasNext()) {
                    cast_token.assert_next_semicolon();
                }
            }
            return begin_end_preprocessed;
        } else if (next instanceof ForToken) {
            ReturnsValue tmp_var = getNextExpression(context);
            next = take();
            assert (next instanceof AssignmentToken);
            ReturnsValue first_value = getNextExpression(context);
            next = take();
            boolean downto = false;
            if (next instanceof DowntoToken) {
                downto = true;
            } else if (!(next instanceof ToToken)) {
                throw new ExpectedTokenException("[To] or [Downto]", next);
            }
            ReturnsValue last_value = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable result;
            if (downto) { // TODO probably should merge these two types
                result = new DowntoForStatement(context, tmp_var, first_value,
                        last_value, get_next_command(context), initialline);
            } else {
                result = new ForStatement(context, tmp_var, first_value,
                        last_value, get_next_command(context), initialline);
            }
            return result;
        } else if (next instanceof RepeatToken) {
            InstructionGrouper command = new InstructionGrouper(initialline);

            while (!(peek_no_EOF() instanceof UntilToken)) {
                command.add_command(get_next_command(context));
                if (!(peek_no_EOF() instanceof UntilToken)) {
                    assert_next_semicolon();
                }
            }
            next = take();
            if (!(next instanceof UntilToken)) {
                throw new ExpectedTokenException("until", next);
            }
            ReturnsValue condition = getNextExpression(context);
            return new RepeatInstruction(command, condition, initialline);
        } else if (next instanceof CaseToken) {
            return new CaseInstruction((CaseToken) next, context);
        } else if (next instanceof SemicolonToken) {
            return new NopInstruction(next.lineInfo);
        } else {
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException e) {
            }
            ReturnsValue r = getNextExpression(context, next);
            next = peek();
            if (next instanceof AssignmentToken) {
                take();
                ReturnsValue value_to_assign = getNextExpression(context);
                DeclaredType output_type = r.get_type(context).declType;
                DeclaredType input_type = value_to_assign.get_type(context).declType;
                /*
                 * Does not have to be writable to assign value to variable.
				 */
                ReturnsValue converted = output_type.convert(value_to_assign,
                        context);
                if (converted == null) {
                    throw new UnconvertibleTypeException(value_to_assign,
                            input_type, output_type, true);
                }
                return r.createSetValueInstruction(output_type
                        .cloneValue(converted));
            } else if (r instanceof Executable) {
                return (Executable) r;
            } else {
                throw new NotAStatementException(r);
            }

        }
    }

    protected abstract String getClosingText();

}
