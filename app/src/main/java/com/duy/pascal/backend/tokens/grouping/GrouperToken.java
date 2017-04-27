package com.duy.pascal.backend.tokens.grouping;


import android.util.Log;

import com.duy.pascal.backend.exceptions.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.ExpectedAnotherTokenException;
import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.MissingCommaTokenException;
import com.duy.pascal.backend.exceptions.MissingSemicolonTokenException;
import com.duy.pascal.backend.exceptions.MultipleDefaultValuesException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.NonIntegerException;
import com.duy.pascal.backend.exceptions.NonIntegerIndexException;
import com.duy.pascal.backend.exceptions.NotAStatementException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.SameNameException;
import com.duy.pascal.backend.exceptions.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.duy.pascal.backend.tokens.CommentToken;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ArrayToken;
import com.duy.pascal.backend.tokens.basic.AssignmentToken;
import com.duy.pascal.backend.tokens.basic.BreakToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.basic.DowntoToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.ExitToken;
import com.duy.pascal.backend.tokens.basic.ForToken;
import com.duy.pascal.backend.tokens.basic.IfToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.RepeatToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.ThenToken;
import com.duy.pascal.backend.tokens.basic.ToToken;
import com.duy.pascal.backend.tokens.basic.UntilToken;
import com.duy.pascal.backend.tokens.basic.WhileToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.BreakInstruction;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExitInstruction;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.NoneInstruction;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GrouperToken extends Token {
    private static final String TAG = GrouperToken.class.getSimpleName();
    LinkedBlockingQueue<Token> queue;
    Token next = null;

    public GrouperToken(LineInfo line) {
        super(line);
        queue = new LinkedBlockingQueue<>();
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
        if (next instanceof CommentToken) {
            next = null;
            return get_next();
        }
        return next;
    }

    public boolean hasNext() throws GroupingException {
        return !(get_next() instanceof EOFToken);
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

        if (result instanceof EOFToken) {
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
        if (result instanceof EOFToken) {
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

    public String nextWordValue() throws ParsingException {
        return take().getWordValue().name;
    }

    public void assertNextSemicolon() throws ParsingException {
        Token t = take();
        if (!(t instanceof SemicolonToken)) {
            throw new MissingSemicolonTokenException(t);
        }
    }

    public void assertNextComma() throws ParsingException {
        Token t = take();
        if (!(t instanceof CommaToken)) {
            throw new MissingCommaTokenException(t);
        }
    }

    public DeclaredType getNextPascalType(ExpressionContext context) throws ParsingException {
        Token n = take();
        if (n instanceof ArrayToken) {
            return getArrayType(context);
        }
        if (n instanceof RecordToken) {
//            throw new UnSupportTokenException(n.lineInfo, n);
            RecordToken r = (RecordToken) n;
            RecordType result = new RecordType();
            result.variableTypes = r.getVariableDeclarations(context);
            return result;
        }
        if (!(n instanceof WordToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        return ((WordToken) n).toBasicType(context);
    }

    private DeclaredType getArrayType(ExpressionContext context) throws ParsingException {
        Token n = peek_no_EOF();
        if (n instanceof BracketedToken) {
            BracketedToken bracket = (BracketedToken) take();
            return getArrayType(bracket, context);
        } else if (n instanceof OfToken) {
            take();
            DeclaredType elementType = getNextPascalType(context);
            return new ArrayType<>(elementType, new IntegerSubrangeType());
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    private DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context)
            throws ParsingException {
        IntegerSubrangeType bound = new IntegerSubrangeType(bounds, context);
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
            elementType = getNextPascalType(context);
        }
        Log.d(TAG, "getArrayType: " + elementType.toString());
        ArrayType arrayType;
        return new ArrayType<>(elementType, bound);
    }

    public ReturnsValue getNextExpression(ExpressionContext context, precedence precedence, Token next)
            throws ParsingException {

        ReturnsValue nextTerm;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.canBeUnary()) {
                throw new BadOperationTypeException(next.lineInfo, nextOperator.type);
            }
            nextTerm = new UnaryOperatorEvaluation(
                    getNextExpression(context, nextOperator.type.getPrecedence()),
                    nextOperator.type, nextOperator.lineInfo);
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
                ReturnsValue nextvalue = getNextExpression(context, nextOperator.type.getPrecedence());
                OperatorTypes operationtype = ((OperatorToken) next).type;
                DeclaredType type1 = nextTerm.getType(context).declaredType;
                DeclaredType type2 = nextvalue.getType(context).declaredType;
                try {
                    operationtype.verifyOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.lineInfo, type1,
                            type2, nextTerm, nextvalue, operationtype);
                }
                nextTerm = BinaryOperatorEvaluation.generateOp(context,
                        nextTerm, nextvalue, operationtype, nextOperator.lineInfo);
            } else if (next instanceof PeriodToken) {
                take();
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Element Name]", next);
                }
                nextTerm = new FieldAccess(nextTerm, (WordToken) next);
            } else if (next instanceof BracketedToken) {
                take(); //comma token
                BracketedToken bracketedToken = (BracketedToken) next;

                RuntimeType mRuntimeType = nextTerm.getType(context);
                ReturnsValue mUnConverted = bracketedToken.getNextExpression(context);
                ReturnsValue mConverted = BasicType.Integer.convert(mUnConverted, context);

                if (mConverted == null) {
                    throw new NonIntegerIndexException(mUnConverted);
                }

                nextTerm = mRuntimeType.declaredType.generateArrayAccess(nextTerm, mConverted);

                while (bracketedToken.hasNext()) {
                    next = bracketedToken.take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException("]", next);
                    }
                    RuntimeType type = nextTerm.getType(context);
                    ReturnsValue unconvert = bracketedToken.getNextExpression(context);
                    ReturnsValue convert = BasicType.Integer.convert(unconvert, context);

                    if (convert == null) {
                        throw new NonIntegerIndexException(unconvert);
                    }
                    nextTerm = type.declaredType.generateArrayAccess(nextTerm, convert);
                }


            }
        }
        return nextTerm;
    }

    public ReturnsValue getNextExpression(ExpressionContext context,
                                          precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public ReturnsValue getNextTerm(ExpressionContext context, Token next) throws ParsingException {
        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).getSingleValue(context);
        } else if (next instanceof ValueToken) {
            return new ConstantAccess(((ValueToken) next).getValue(), next.lineInfo);
        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();
            if (next instanceof ParenthesizedToken) {
                List<ReturnsValue> arguments;
                if (name.name.equalsIgnoreCase("writeln") || name.name.equalsIgnoreCase("write")) {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForOutput(context);
                } else {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForCall(context);
                }
                return FunctionCall.generateFunctionCall(name, arguments, context);
            } else {
                return context.getIdentifierValue(name);
            }

        }/* else if (next instanceof CommentToken) {
            return getNextTerm(context);
        } */ else {
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

    public List<VariableDeclaration> getVariableDeclarations(ExpressionContext context)
            throws ParsingException {
        List<VariableDeclaration> result = new ArrayList<>();
        /*
         * reusing it, so it is further out of scope than necessary
		 */
        List<WordToken> names = new ArrayList<>();
        Token next;
        do {
            do {
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Variable Identifier]", next);
                }
                names.add((WordToken) next);
                next = take();
            } while (next instanceof CommaToken);
            if (!(next instanceof ColonToken)) {
                throw new ExpectedTokenException(":", next);
            }
            DeclaredType type = getNextPascalType(context);

            //process string with define length
            if (type.equals(BasicType.StringBuilder)) {
                if (peek() instanceof BracketedToken) {
                    BracketedToken bracketedToken = (BracketedToken) take();

                    ReturnsValue unconverted = bracketedToken.getNextExpression(context);
                    ReturnsValue converted = BasicType.Integer.convert(unconverted, context);

                    if (converted == null) {
                        throw new NonIntegerException(unconverted);
                    }

                    if (bracketedToken.hasNext()) {
                        throw new ExpectedTokenException("]", bracketedToken.take());
                    }
                    ((BasicType) type).setLength(converted);
                }
            }


            Object defaultValue = null;
            if (peek() instanceof OperatorToken) {
                if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                    take();
                    //set default value for array
                    if (type instanceof ArrayType) {
                        DeclaredType elementTypeOfArray = ((ArrayType) type).elementType;
                        ParenthesizedToken bracketedToken = (ParenthesizedToken) take();
                        int size = ((ArrayType) type).getBounds().size;
                        Object[] objects = new Object[size];
                        for (int i = 0; i < size; i++) {
                            if (!bracketedToken.hasNext()) {
                                // TODO: 27-Apr-17  exception
                            }
                            objects[i] = getDefaultValueArray(context, bracketedToken, elementTypeOfArray);
                        }
                        Log.d(TAG, "getDefaultValueArray: " + Arrays.toString(objects));
                        defaultValue = objects;
                    } else { //set default single value
                        ReturnsValue unConvert = getNextExpression(context);
                        ReturnsValue converted = type.convert(unConvert, context);
                        if (converted == null) {
                            throw new UnConvertibleTypeException(unConvert,
                                    unConvert.getType(context).declaredType, type,
                                    true);
                        }
                        defaultValue = converted.compileTimeValue(context);
                        if (defaultValue == null) {
                            throw new NonConstantExpressionException(converted);
                        }
                        if (names.size() != 1) {
                            throw new MultipleDefaultValuesException(converted.getLine());
                        }
                    }
                }
            }

            assertNextSemicolon();
            for (WordToken word : names) {
                VariableDeclaration v = new VariableDeclaration(word.name, type, defaultValue, word.lineInfo);
                verifyNonConflictingSymbol(result, v);
                result.add(v);
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    public Object getDefaultValueArray(ExpressionContext context,
                                       ParenthesizedToken parenthesizedToken,
                                       DeclaredType elementTypeOfArray) throws ParsingException {
        if (parenthesizedToken.hasNext()) {
            if (elementTypeOfArray instanceof ArrayType) {
                if (parenthesizedToken.peek() instanceof ParenthesizedToken) {
                    ParenthesizedToken child = (ParenthesizedToken) parenthesizedToken.take();
                    Object[] objects = new Object[((ArrayType) elementTypeOfArray).getBounds().size];
                    for (int i = 0; i < objects.length; i++) {
                        objects[i] = getDefaultValueArray(context, child, ((ArrayType) elementTypeOfArray).elementType);
                    }
                    if (child.hasNext()) {
                        // TODO: 27-Apr-17  exception
                    }
                    if (parenthesizedToken.hasNext()) {
                        parenthesizedToken.assertNextComma();
                    }
                    return objects;
                } else {
                    // TODO: 27-Apr-17 throw exception
                }
            } else {
                ReturnsValue unconvert = parenthesizedToken.getNextExpression(context);
                ReturnsValue converted = elementTypeOfArray.convert(unconvert, context);
                if (context == null) {
                    // TODO: 27-Apr-17  throw exception
                }
                if (parenthesizedToken.hasNext()) {
                    parenthesizedToken.assertNextComma();
                }
                return converted.compileTimeValue(context);
            }
        } else {
            // TODO: 27-Apr-17  throw exception
        }
        return null;
    }

    private void verifyNonConflictingSymbol(List<VariableDeclaration> result, VariableDeclaration variable) throws SameNameException {
        for (VariableDeclaration variableDeclaration : result) {
            if (variableDeclaration.getName().equalsIgnoreCase(variable.getName())) {
                throw new SameNameException(variableDeclaration, variable);
            }
        }
    }

    public ReturnsValue getSingleValue(ExpressionContext context)
            throws ParsingException {
        ReturnsValue result = getNextExpression(context);
        if (hasNext()) {
            Token next = take();
            throw new ExpectedTokenException(getClosingText(), next);
        }
        return result;
    }

    public Executable getNextCommand(ExpressionContext context) throws ParsingException {
        Token next = take();
        LineInfo initialline = next.lineInfo;
        if (next instanceof IfToken) {
            ReturnsValue condition = getNextExpression(context);
            next = take();
            assert (next instanceof ThenToken);
            Executable command = getNextCommand(context);
            Executable else_command = null;
            next = peek();
            if (next instanceof ElseToken) {
                take();
                else_command = getNextCommand(context);
            }
            return new IfStatement(condition, command, else_command, initialline);
        } else if (next instanceof WhileToken) {
            ReturnsValue condition = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable command = getNextCommand(context);
            return new WhileStatement(condition, command, initialline);
        } else if (next instanceof BeginEndToken) {
            InstructionGrouper beginEndPreprocessed = new InstructionGrouper(initialline);
            BeginEndToken castToken = (BeginEndToken) next;
            while (castToken.hasNext()) {
                beginEndPreprocessed.addCommand(castToken.getNextCommand(context));
                if (castToken.hasNext()) {
                    castToken.assertNextSemicolon();
                }
            }
            return beginEndPreprocessed;
        } else if (next instanceof ForToken) {
            //for i := 1 to n do writeln(i);
            ReturnsValue tmpVar = getNextExpression(context);              //i
            next = take();                                                  // :=
            assert (next instanceof AssignmentToken);
            ReturnsValue firstValue = getNextExpression(context);          //1
            next = take();                                                  //to | downto
            boolean downto = false;
            if (next instanceof DowntoToken) {
                downto = true;
            } else if (!(next instanceof ToToken)) {
                throw new ExpectedTokenException("[To] or [Downto]", next);
            }
            ReturnsValue lastValue = getNextExpression(context);
            next = take();
            assert (next instanceof DoToken);
            Executable result;
            if (downto) {
                result = new DowntoForStatement(context, tmpVar, firstValue,
                        lastValue, getNextCommand(context), initialline);
            } else {
                result = new ForStatement(context, tmpVar, firstValue,
                        lastValue, getNextCommand(context), initialline);
            }
            return result;
        } else if (next instanceof RepeatToken) {
            InstructionGrouper command = new InstructionGrouper(initialline);
            while (!(peek_no_EOF() instanceof UntilToken)) {
                command.addCommand(getNextCommand(context));
                if (!(peek_no_EOF() instanceof UntilToken)) {
                    assertNextSemicolon();
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
            return new NoneInstruction(next.lineInfo);
        } else if (next instanceof BreakToken) {
            return new BreakInstruction(next.lineInfo);
        } else if (next instanceof ExitToken) {
            return new ExitInstruction(next.lineInfo);
        } /*else if (next instanceof CommentToken) {
            //ignore comment
            return getNextCommand(context);
        }*/ else {
            //variable
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException ignored) {
            }

            ReturnsValue variable = getNextExpression(context, next);
            next = peek();
            if (next instanceof AssignmentToken) {
                take();
                ReturnsValue valueToAssign = getNextExpression(context);
                DeclaredType variableType = variable.getType(context).declaredType;
                DeclaredType assignType = valueToAssign.getType(context).declaredType;
                /*
                 * Does not have to be writable to assign value to variable.
				 */
                ReturnsValue converted = variableType.convert(valueToAssign, context);
                if (converted == null) {
                    throw new UnConvertibleTypeException(valueToAssign, assignType, variableType, true);
                }
                return variable.createSetValueInstruction(variableType.cloneValue(converted));
            } else if (variable instanceof Executable) {
                return (Executable) variable;
            } else {
                throw new NotAStatementException(variable);
            }

        }
    }

    protected abstract String getClosingText();

}
