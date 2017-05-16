package com.duy.pascal.backend.tokens.grouping;


import android.util.Log;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.exceptions.UnsupportedOutputFormatException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.define.MethodNotFoundException;
import com.duy.pascal.backend.exceptions.define.MultipleDefaultValuesException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.exceptions.grouping.GroupingException;
import com.duy.pascal.backend.exceptions.index.NonIntegerIndexException;
import com.duy.pascal.backend.exceptions.operator.BadOperationTypeException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedAnotherTokenException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.syntax.MissingCommaTokenException;
import com.duy.pascal.backend.exceptions.syntax.MissingSemicolonTokenException;
import com.duy.pascal.backend.exceptions.syntax.NotAStatementException;
import com.duy.pascal.backend.exceptions.value.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.value.NonIntegerException;
import com.duy.pascal.backend.exceptions.value.UnAssignableTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
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
import com.duy.pascal.backend.tokens.basic.ContinueToken;
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
import com.duy.pascal.backend.tokens.basic.ToToken;
import com.duy.pascal.backend.tokens.basic.UntilToken;
import com.duy.pascal.backend.tokens.basic.WhileToken;
import com.duy.pascal.backend.tokens.basic.WithToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.js.interpreter.ast.MethodDeclaration;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.WrongIfElseStatement;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;
import com.js.interpreter.ast.instructions.Assignment;
import com.js.interpreter.ast.instructions.BreakInstruction;
import com.js.interpreter.ast.instructions.ContinueInstruction;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.instructions.ExitInstruction;
import com.js.interpreter.ast.instructions.InstructionGrouper;
import com.js.interpreter.ast.instructions.NoneInstruction;
import com.js.interpreter.ast.instructions.case_statement.CaseInstruction;
import com.js.interpreter.ast.instructions.conditional.ForDowntoStatement;
import com.js.interpreter.ast.instructions.conditional.ForToStatement;
import com.js.interpreter.ast.instructions.conditional.IfStatement;
import com.js.interpreter.ast.instructions.conditional.RepeatInstruction;
import com.js.interpreter.ast.instructions.conditional.WhileStatement;
import com.js.interpreter.ast.instructions.with_statement.WithStatement;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FieldAccess;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.AssignableValue;
import com.js.interpreter.ast.returnsvalue.ReturnValue;
import com.js.interpreter.ast.returnsvalue.UnaryOperatorEvaluation;
import com.js.interpreter.ast.returnsvalue.operators.number.BinaryOperatorEvaluation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class GrouperToken extends Token {
    private static final String TAG = GrouperToken.class.getSimpleName();
    public Token next = null;
    LinkedBlockingQueue<Token> queue;

    public GrouperToken(LineInfo line) {
        super(line);
        queue = new LinkedBlockingQueue<>();
    }

    private Token getNext() throws GroupingException {
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
            return getNext();
        }
        return next;
    }

    public boolean hasNext() throws GroupingException {
        return !(getNext() instanceof EOFToken);
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
        Token result = getNext();

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
        return getNext();
    }

    public Token peekNoEOF() throws ExpectedAnotherTokenException,
            GroupingException {
        Token result = peek();
        if (result instanceof EOFToken) {
            throw new ExpectedAnotherTokenException(result.lineInfo);
        }
        return result;
    }

    @Override
    public String toString() {
        try {
            return getNext().toString() + ',' + queue.toString();
        } catch (GroupingException e) {
            return "Exception: " + e.toString();
        }
    }

    public String nextWordValue() throws ParsingException {
        return take().getWordValue().name;
    }

    public void assertNextSemicolon(Token last) throws ParsingException {
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

    public DeclaredType getNextPascalType(ExpressionContext context)
            throws ParsingException {
        Token n = take();
        if (n instanceof ArrayToken) {
            return getArrayType(context);
        }
        if (n instanceof RecordToken) {
//            throw new UnSupportTokenException(n.lineInfo, n);
            RecordToken r = (RecordToken) n;
            RecordType result = new RecordType();
            result.variableDeclarations = r.getVariableDeclarations(context);
            return result;
        }
        if (n instanceof OperatorToken && ((OperatorToken) n).type == OperatorTypes.DEREF) {
            DeclaredType pointed_type = getNextPascalType(context);
            return new PointerType(pointed_type);
        }
        /*if (n instanceof ClassToken) {
            ClassToken o = (ClassToken)n;
			ClassType result = new ClassType();
			throw new ExpectedTokenException("[asdf]", n);
		}*/
        if (!(n instanceof WordToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        return ((WordToken) n).toBasicType(context);
    }

    private DeclaredType getArrayType(ExpressionContext context)
            throws ParsingException {
        Token n = peekNoEOF();
        if (n instanceof BracketedToken) {
            BracketedToken bracket = (BracketedToken) take();
            return getArrayType(bracket, context);
        } else if (n instanceof OfToken) {
            take();
            DeclaredType elementType = getNextPascalType(context);
            return new ArrayType<>(elementType, new SubrangeType());
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    private DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context)
            throws ParsingException {
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
            elementType = getNextPascalType(context);
        }
        Log.d(TAG, "getArrayType: " + elementType.toString());
        return new ArrayType<>(elementType, bound);
    }

    public ReturnValue getNextExpression(ExpressionContext context,
                                         precedence precedence, Token next) throws ParsingException {

        ReturnValue nextTerm;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.canBeUnary() || nextOperator.postfix()) {
                throw new BadOperationTypeException(next.lineInfo,
                        nextOperator.type);
            }
            nextTerm = UnaryOperatorEvaluation.generateOp(context, getNextExpression(context,
                    nextOperator.type.getPrecedence()), nextOperator.type, nextOperator.lineInfo);
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
                if (nextOperator.postfix()) {
                    return UnaryOperatorEvaluation.generateOp(context, nextTerm, nextOperator.type,
                            nextOperator.lineInfo);
                }
                ReturnValue nextValue = getNextExpression(context,
                        nextOperator.type.getPrecedence());
                OperatorTypes operationType = ((OperatorToken) next).type;
                DeclaredType type1 = nextTerm.getType(context).declType;
                DeclaredType type2 = nextValue.getType(context).declType;
                try {
                    operationType.verifyBinaryOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.lineInfo, type1,
                            type2, nextTerm, nextValue, operationType);
                }
                nextTerm = BinaryOperatorEvaluation.generateOp(context,
                        nextTerm, nextValue, operationType,
                        nextOperator.lineInfo);
            } else if (next instanceof PeriodToken) {
                take();
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Element Name]", next);
                }
                //need call method of java class
                RuntimeType type = nextTerm.getType(context);
                //access method of java class
                if (type.declType instanceof JavaClassBasedType) {
                    nextTerm = getMethodFromClass(context, nextTerm, ((WordToken) next).getName());
                } else {
                    nextTerm = new FieldAccess(nextTerm, (WordToken) next);
                }
            } else if (next instanceof BracketedToken) {
                take(); //comma token
                BracketedToken b = (BracketedToken) next;

                RuntimeType mRuntimeType = nextTerm.getType(context);
                ReturnValue mUnConverted = b.getNextExpression(context);
                ReturnValue mConverted = BasicType.Integer.convert(mUnConverted, context);

                if (mConverted == null) {
                    throw new NonIntegerIndexException(mUnConverted);
                }

                nextTerm = mRuntimeType.declType.generateArrayAccess(nextTerm, mConverted);

                while (b.hasNext()) {
                    next = b.take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException("]", next);
                    }
                    RuntimeType type = nextTerm.getType(context);
                    ReturnValue unConvert = b.getNextExpression(context);
                    ReturnValue convert = BasicType.Integer.convert(unConvert, context);

                    if (convert == null) {
                        throw new NonIntegerIndexException(unConvert);
                    }
                    nextTerm = type.declType.generateArrayAccess(nextTerm, convert);
                }
            }
        }
        return nextTerm;
    }

    public ReturnValue getNextExpression(ExpressionContext context,
                                         precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public ReturnValue getNextTerm(ExpressionContext context, Token next)
            throws ParsingException {
        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).getSingleValue(context);
        } else if (next instanceof ValueToken) {
            return new ConstantAccess(((ValueToken) next).getValue(),
                    next.lineInfo);
        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();

            if (next instanceof ParenthesizedToken) {
                List<ReturnValue> arguments;
                if (name.name.equalsIgnoreCase("writeln") || name.name.equalsIgnoreCase("write")) {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForOutput(context);
                } else {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForCall(context);
                }
                return FunctionCall.generateFunctionCall(name, arguments, context);
            } else {
                return context.getIdentifierValue(name);
            }

        } else {
            if (next instanceof ElseToken) {
                throw new WrongIfElseStatement(next);
            }
            throw new UnrecognizedTokenException(next);
        }
    }

    public ReturnValue getNextTerm(ExpressionContext context)
            throws ParsingException {
        return getNextTerm(context, take());
    }

    public ReturnValue getNextExpression(ExpressionContext context)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence);
    }

    public ReturnValue getNextExpression(ExpressionContext context, Token first)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence, first);
    }

    public List<VariableDeclaration> getVariableDeclarations(
            ExpressionContext context) throws ParsingException {
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

                    ReturnValue unconverted = bracketedToken.getNextExpression(context);
                    ReturnValue converted = BasicType.Integer.convert(unconverted, context);

                    if (converted == null) {
                        throw new NonIntegerException(unconverted);
                    }

                    if (bracketedToken.hasNext()) {
                        throw new ExpectedTokenException("]", bracketedToken.take());
                    }
                    try {
                        ((BasicType) type).setLength(converted);
                    } catch (UnsupportedOutputFormatException e) {
                        throw new UnsupportedOutputFormatException(lineInfo);
                    }
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
                        ReturnValue unConvert = getNextExpression(context);
                        ReturnValue converted = type.convert(unConvert, context);
                        if (converted == null) {
                            throw new UnConvertibleTypeException(unConvert,
                                    unConvert.getType(context).declType, type,
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
            }

            assertNextSemicolon(next);
            for (WordToken s : names) {
                VariableDeclaration v = new VariableDeclaration(s.name, type,
                        defaultValue, s.lineInfo);
                verifyNonConflictingSymbol(context, result, v);
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
                ReturnValue unconvert = parenthesizedToken.getNextExpression(context);
                ReturnValue converted = elementTypeOfArray.convert(unconvert, context);
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

    /**
     * check duplicate declare variable
     */
    private void verifyNonConflictingSymbol(ExpressionContext context, List<VariableDeclaration> result, VariableDeclaration variable) throws SameNameException {
        for (VariableDeclaration variableDeclaration : result) {
            context.verifyNonConflictingSymbol(variable);
            if (variableDeclaration.getName().equalsIgnoreCase(variable.getName())) {
                throw new SameNameException(variableDeclaration, variable);
            }
        }
    }

    /**
     * Return single value in list value
     * example:
     * <code>parentheses token = (1, 2, 3, 4) -> return 1</code>
     */
    public ReturnValue getSingleValue(ExpressionContext context) throws ParsingException {
        ReturnValue result = getNextExpression(context);
        if (hasNext()) {
            Token next = take();
            throw new ExpectedTokenException(getClosingText(), next);
        }
        return result;
    }

    public Executable getNextCommand(ExpressionContext context) throws ParsingException {
        Token next = take();
        LineInfo lineNumber = next.lineInfo;
        if (next instanceof IfToken) {
            return new IfStatement(context, this, lineNumber);
        } else if (next instanceof WhileToken) {
            return new WhileStatement(context, this, lineNumber);
        } else if (next instanceof BeginEndToken) {
            InstructionGrouper beginEndPreprocessed = new InstructionGrouper(
                    lineNumber);
            BeginEndToken castToken = (BeginEndToken) next;

            while (castToken.hasNext()) {
                beginEndPreprocessed.add_command(castToken.getNextCommand(context));
                Token token = castToken.next;
                if (castToken.hasNext()) {
                    castToken.assertNextSemicolon(token);
                }
            }
            return beginEndPreprocessed;
        } else if (next instanceof ForToken) {
            ReturnValue tmpVal = getNextExpression(context);
            AssignableValue tmpVariable = tmpVal.asAssignableValue(context);
            if (tmpVariable == null) {
                throw new UnAssignableTypeException(tmpVal);
            }
            next = take();
            if (!(next instanceof AssignmentToken
                    || next instanceof OperatorToken)) {
                throw new ExpectedTokenException("\":=\" or \"in\"", next);
            }
            Executable result = null;
            if (next instanceof AssignmentToken) {
                ReturnValue firstValue = getNextExpression(context);
                next = take();
                boolean downto = false;
                if (next instanceof DowntoToken) {
                    downto = true;
                } else if (!(next instanceof ToToken)) {
                    throw new ExpectedTokenException("[To] or [Downto]", next);
                }
                ReturnValue lastValue = getNextExpression(context);
                next = take();
                if (!(next instanceof DoToken)) {
                    throw new ExpectedTokenException("do", next);
                }
                if (downto) { // TODO probably should merge these two types
                    result = new ForDowntoStatement(context, tmpVariable, firstValue,
                            lastValue, getNextCommand(context), lineNumber);
                } else {
                    result = new ForToStatement(context, tmpVariable, firstValue,
                            lastValue, getNextCommand(context), lineNumber);
                }
            } else if (next instanceof OperatorToken) {
                if (((OperatorToken) next).type == OperatorTypes.IN) {

                } else {
                    throw new ExpectedTokenException("\":=\" or \"in\"", next);
                }
            }
            return result;
        } else if (next instanceof RepeatToken) {
            InstructionGrouper command = new InstructionGrouper(lineNumber);

            while (!(peekNoEOF() instanceof UntilToken)) {
                command.add_command(getNextCommand(context));
                if (!(peekNoEOF() instanceof UntilToken)) {
                    assertNextSemicolon(next);
                }
            }
            next = take();
            if (!(next instanceof UntilToken)) {
                throw new ExpectedTokenException("until", next);
            }
            ReturnValue condition = getNextExpression(context);
            return new RepeatInstruction(command, condition, lineNumber);
        } else if (next instanceof CaseToken) {
            return new CaseInstruction((CaseToken) next, context);
        } else if (next instanceof SemicolonToken) {
            return new NoneInstruction(next.lineInfo);
        } else if (next instanceof BreakToken) {
            return new BreakInstruction(next.lineInfo);
        } else if (next instanceof ContinueToken) {
            return new ContinueInstruction(next.lineInfo);
        } else if (next instanceof WithToken) {
            return (Executable) new WithStatement(context, this).generate();
        } else if (next instanceof ExitToken) {
            return new ExitInstruction(next.lineInfo);
        } else {
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException ignored) {
            }

            ReturnValue r = getNextExpression(context, next);
            next = peek();
            if (next instanceof AssignmentToken) {
                take();
                AssignableValue left = r.asAssignableValue(context);

                if (left == null) {
                    throw new UnAssignableTypeException(r);
                }
                ReturnValue valueToAssign = getNextExpression(context);
                DeclaredType outputType = left.getType(context).declType;
                DeclaredType inputType = valueToAssign.getType(context).declType;
                /*
                 * Does not have to be writable to assign value to variable.
				 */
                ReturnValue converted = outputType.convert(valueToAssign, context);
                if (converted == null) {
                    throw new UnConvertibleTypeException(valueToAssign,
                            inputType, outputType, true);
                }
                return new Assignment(left, outputType.cloneValue(converted), next.lineInfo);
            } else if (r instanceof Executable) {
                return (Executable) r;
            } else if (r instanceof FieldAccess) {
                FieldAccess fieldAccess = (FieldAccess) r;
                ReturnValue container = fieldAccess.getContainer();
                return (Executable) getMethodFromClass(context, container, fieldAccess.getName());
            } else {
                throw new NotAStatementException(r);
            }
        }
    }

    private ReturnValue getMethodFromClass(ExpressionContext context, ReturnValue container, String
            methodName) throws ParsingException {

        RuntimeType type = container.getType(context);

        //access method of java class
        if (type.declType instanceof JavaClassBasedType) {
            JavaClassBasedType javaType = (JavaClassBasedType) type.declType;
            Class<?> storageClass = javaType.getStorageClass();

            //get arguments
            List<ReturnValue> argumentsForCall = new ArrayList<>();
            if (hasNext()) {
                if (peek() instanceof ParenthesizedToken) {
                    ParenthesizedToken token = (ParenthesizedToken) take();
                    argumentsForCall = token.getArgumentsForCall(context);
                }
            }

            //get method, ignore case
            Method[] declaredMethods = storageClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equalsIgnoreCase(methodName)) {
                    MethodDeclaration methodDeclaration =
                            new MethodDeclaration(container, declaredMethod, javaType);
                    FunctionCall functionCall = methodDeclaration.generateCall(lineInfo,
                            argumentsForCall, context);
                    if (functionCall != null) {
                        return functionCall;
                    }
                }
            }
            throw new MethodNotFoundException(container.getLineNumber(),
                    methodName, storageClass.getName());
        } else {
            throw new NotAStatementException(container);
        }
    }

    protected abstract String getClosingText();

}
