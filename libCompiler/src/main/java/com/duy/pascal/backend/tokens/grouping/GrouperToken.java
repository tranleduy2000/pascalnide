package com.duy.pascal.backend.tokens.grouping;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.duy.pascal.backend.exceptions.syntax.NotAStatementException;
import com.duy.pascal.backend.exceptions.syntax.WrongIfElseStatement;
import com.duy.pascal.backend.exceptions.value.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.value.NonIntegerException;
import com.duy.pascal.backend.exceptions.value.UnAssignableTypeException;
import com.duy.pascal.backend.function_declaretion.MethodDeclaration;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.ClassType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.duy.pascal.backend.pascaltypes.RecordType;
import com.duy.pascal.backend.pascaltypes.RuntimeType;
import com.duy.pascal.backend.pascaltypes.enumtype.EnumElementValue;
import com.duy.pascal.backend.pascaltypes.enumtype.EnumGroupType;
import com.duy.pascal.backend.pascaltypes.rangetype.EnumSubrangeType;
import com.duy.pascal.backend.pascaltypes.rangetype.IntegerSubrangeType;
import com.duy.pascal.backend.pascaltypes.rangetype.SubrangeType;
import com.duy.pascal.backend.pascaltypes.set.SetType;
import com.duy.pascal.backend.tokens.CommentToken;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
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
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.SetToken;
import com.duy.pascal.backend.tokens.basic.ToToken;
import com.duy.pascal.backend.tokens.basic.WhileToken;
import com.duy.pascal.backend.tokens.basic.WithToken;
import com.duy.pascal.backend.tokens.closing.UntilToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.expressioncontext.ExpressionContext;
import com.js.interpreter.instructions.Assignment;
import com.js.interpreter.instructions.BreakInstruction;
import com.js.interpreter.instructions.ContinueInstruction;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.instructions.ExitInstruction;
import com.js.interpreter.instructions.InstructionGrouper;
import com.js.interpreter.instructions.NoneInstruction;
import com.js.interpreter.instructions.case_statement.CaseInstruction;
import com.js.interpreter.instructions.conditional.ForDowntoStatement;
import com.js.interpreter.instructions.conditional.ForToStatement;
import com.js.interpreter.instructions.conditional.IfStatement;
import com.js.interpreter.instructions.conditional.RepeatInstruction;
import com.js.interpreter.instructions.conditional.WhileStatement;
import com.js.interpreter.instructions.with_statement.WithStatement;
import com.duy.pascal.backend.runtime.value.AssignableValue;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.FieldAccess;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.UnaryOperatorEvaluation;
import com.duy.pascal.backend.runtime.operators.number.BinaryOperatorEvaluation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
            throw new ExpectedAnotherTokenException(result.getLineNumber());
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

    public Token takeEOF() throws GroupingException {
        Token result = getNext();
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
            throw new ExpectedAnotherTokenException(result.getLineNumber());
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
            throw new ExpectedTokenException(new SemicolonToken(null), t);
        }
    }

    public void assertNextComma() throws ParsingException {
        Token t = take();
        if (!(t instanceof CommaToken)) {
            throw new ExpectedTokenException(new CommaToken(null), t);
        }
    }

    public DeclaredType getNextPascalType(ExpressionContext context)
            throws ParsingException {
        Token n = take();
        if (n instanceof ArrayToken) {
            return getArrayType(context);
        } else if (n instanceof SetToken) {
            return getSetType(context, n.getLineNumber());
        } else if (n instanceof ParenthesizedToken) {
            return getEnumType(context, (ParenthesizedToken) n);
        } else if (n instanceof RecordToken) {
            RecordToken r = (RecordToken) n;
            RecordType result = new RecordType();
            result.setVariableDeclarations(r.getVariableDeclarations(context));
            return result;
        } else if (n instanceof OperatorToken && ((OperatorToken) n).type == OperatorTypes.DEREF) {
            DeclaredType pointed_type = getNextPascalType(context);
            return new PointerType(pointed_type);
        } else if (n instanceof ClassToken) {
            ClassToken o = (ClassToken) n;
            ClassType result = new ClassType();
            throw new ExpectedTokenException("[]", n);
        } else if (!(n instanceof WordToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        DeclaredType declaredType = ((WordToken) n).toBasicType(context);
        //process string with define length
        if (declaredType.equals(BasicType.StringBuilder)) {
            if (peek() instanceof BracketedToken) {
                BracketedToken bracketedToken = (BracketedToken) take();

                RuntimeValue unconverted = bracketedToken.getNextExpression(context);
                RuntimeValue converted = BasicType.Integer.convert(unconverted, context);

                if (converted == null) {
                    throw new NonIntegerException(unconverted);
                }

                if (bracketedToken.hasNext()) {
                    throw new ExpectedTokenException("]", bracketedToken.take());
                }
                try {
                    ((BasicType) declaredType).setLength(converted);
                } catch (UnsupportedOutputFormatException e) {
                    throw new UnsupportedOutputFormatException(getLineNumber());
                }
            }
        }


        return declaredType;
    }

    private DeclaredType getEnumType(ExpressionContext c, ParenthesizedToken n) throws ParsingException {
        LinkedList<EnumElementValue> elements = new LinkedList<>();
        EnumGroupType enumGroupType = new EnumGroupType(elements);
        AtomicInteger index = new AtomicInteger(0);
        while (n.hasNext()) {
            Token token = n.take();
            if (!(token instanceof WordToken)) {
                throw new ExpectedTokenException("identifier", token);
            }
            WordToken wordToken = (WordToken) token;
            if (n.peek() instanceof OperatorToken) {
                OperatorToken operator = (OperatorToken) n.take();
                if (operator.type == OperatorTypes.EQUALS) {
                    RuntimeValue value = n.getNextExpression(c);
                    value = value.compileTimeExpressionFold(c);
                    RuntimeValue convert = BasicType.Integer.convert(value, c);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(value, BasicType.Integer,
                                value.getType(c).declType, false);
                    }

                    //create new enum
                    EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType, (Integer) convert.compileTimeValue(c),
                            token.getLineNumber());
                    //add to parent
                    elements.add(e);
                    //add as constant
                    ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType, e, e.getLineNumber());

                    c.verifyNonConflictingSymbol(constant);
                    c.declareConst(constant);
                } else {
                    throw new ExpectedTokenException(operator, ",", "=");
                }
            } else {
                //create new enum
                EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType, index.get(), token.getLineNumber());
                //add to container
                elements.add(e);
                //add as constant
                ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType, e, e.getLineNumber());
                c.declareConst(constant);
            }
            index.getAndIncrement();
            //if has next, check comma token
            if (n.hasNext()) {
                n.assertNextComma();
            }
        }
        return enumGroupType;
    }

    private DeclaredType getSetType(ExpressionContext context, LineInfo lineInfo) throws ParsingException {
        Token n = peekNoEOF();
        if (!(n instanceof OfToken)) {
            throw new ExpectedTokenException(new OfToken(null), n);
        }
        take();
        DeclaredType elementType = getNextPascalType(context);
        return new SetType<>(elementType, lineInfo);
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
            return new ArrayType<>(elementType, new IntegerSubrangeType());
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    private DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context)
            throws ParsingException {
        SubrangeType bound;
        try {
            bound = new EnumSubrangeType(bounds, context);
        } catch (Exception e) {
            e.printStackTrace();
            bound = new IntegerSubrangeType(bounds, context);
        }
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
        return new ArrayType<>(elementType, bound);
    }

    @NonNull
    public RuntimeValue getNextExpression(ExpressionContext context,
                                          precedence precedence, Token next) throws ParsingException {

        RuntimeValue nextTerm;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.canBeUnary() || nextOperator.postfix()) {
                throw new BadOperationTypeException(next.getLineNumber(),
                        nextOperator.type);
            }
            nextTerm = UnaryOperatorEvaluation.generateOp(context, getNextExpression(context,
                    nextOperator.type.getPrecedence()), nextOperator.type, nextOperator.getLineNumber());
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
                            nextOperator.getLineNumber());
                }
                RuntimeValue nextValue = getNextExpression(context,
                        nextOperator.type.getPrecedence());
                OperatorTypes operationType = ((OperatorToken) next).type;
                DeclaredType type1 = nextTerm.getType(context).declType;
                DeclaredType type2 = nextValue.getType(context).declType;
                try {
                    operationType.verifyBinaryOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.getLineNumber(), type1,
                            type2, nextTerm, nextValue, operationType);
                }
                nextTerm = BinaryOperatorEvaluation.generateOp(context,
                        nextTerm, nextValue, operationType,
                        nextOperator.getLineNumber());
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
                BracketedToken bracket = (BracketedToken) next;
                nextTerm = generateArrayAccess(nextTerm, context, bracket);

                while (bracket.hasNext()) {
                    next = bracket.take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException("]", next);
                    }
                    nextTerm = generateArrayAccess(nextTerm, context, bracket);
                }
            }
        }
        return nextTerm;
    }

    private RuntimeValue generateArrayAccess(RuntimeValue parent, ExpressionContext f,
                                             BracketedToken b)
            throws ParsingException {

        RuntimeType type = parent.getType(f);
        RuntimeValue unconvert = b.getNextExpression(f);

        //try convert to integer
        RuntimeValue converted = BasicType.Integer.convert(unconvert, f);

        if (converted == null) { //can not convert to integer -> it can be enum index
            if (type.declType instanceof ArrayType) {//check if container is array
                ArrayType arrayType = (ArrayType) type.declType;

                //if the type of index is enum type
                if (arrayType.getBounds() instanceof EnumSubrangeType
                        && unconvert.getType(f).declType instanceof EnumGroupType) {
                    EnumSubrangeType bounds = (EnumSubrangeType) arrayType.getBounds();

                    converted = bounds.getEnumGroupType().convert(unconvert, f);
                    if (converted != null) {
                        ConstantAccess value = (ConstantAccess) converted;
                        converted = new ConstantAccess(((EnumElementValue) value.compileTimeValue(f)).getIndex(),
                                BasicType.Integer, value.getLineNumber());
                    }
                }
            }
        }

        if (converted == null) {
            throw new NonIntegerIndexException(unconvert);
        }
        return type.declType.generateArrayAccess(parent, converted);
    }

    public RuntimeValue getNextExpression(ExpressionContext context,
                                          precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public RuntimeValue getNextTerm(ExpressionContext context, Token next)
            throws ParsingException {
        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).getSingleValue(context);

        } else if (next instanceof ValueToken) {
            return new ConstantAccess(((ValueToken) next).getValue(), next.getLineNumber());

        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();

            if (next instanceof ParenthesizedToken) {
                List<RuntimeValue> arguments;
                if (name.name.equalsIgnoreCase("writeln") || name.name.equalsIgnoreCase("write")) {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForOutput(context);
                } else {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForCall(context);
                }
                return FunctionCall.generateFunctionCall(name, arguments, context);
            } else {
                return context.getIdentifierValue(name);
            }

        } else if (next instanceof BracketedToken) {
            LinkedList<Object> setConstant = getSetConstant(context, (BracketedToken) next, null);
            Log.d(TAG, "getNextTerm: " + setConstant);
            SetType setType = new SetType(BasicType.Character, setConstant, lineNumber);
            ConstantAccess constantAccess = new ConstantAccess(setType.initialize(), setType, lineNumber);
            return constantAccess;
        } else {
            if (next instanceof ElseToken) {
                throw new WrongIfElseStatement(next);
            }
            throw new UnrecognizedTokenException(next);
        }
    }

    public RuntimeValue getNextTerm(ExpressionContext context)
            throws ParsingException {
        return getNextTerm(context, take());
    }

    public RuntimeValue getNextExpression(ExpressionContext context)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence);
    }

    public RuntimeValue getNextExpression(ExpressionContext context, Token first)
            throws ParsingException {
        return getNextExpression(context, precedence.NoPrecedence, first);
    }

    public ArrayList<VariableDeclaration> getVariableDeclarations(
            ExpressionContext context) throws ParsingException {
        ArrayList<VariableDeclaration> result = new ArrayList<>();
        /*
         * reusing it, so it is further inType of scope than necessary
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


            Object defaultValue = null;
            if (peek() instanceof OperatorToken) {
                if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                    take();
                    //set default value for array
                    if (type instanceof ArrayType) {
                        defaultValue = getArrayConstant(context, (ArrayType) type);
                    } else if (type instanceof SetType) {
                        if (!(peek() instanceof ParenthesizedToken)) {
                            throw new ExpectedTokenException(new ParenthesizedToken(null), peek());
                        }
                        defaultValue = getEnumConstant(context, (ParenthesizedToken) take(), type);
                    } else { //set default single value
                        RuntimeValue unConvert = getNextExpression(context);
                        RuntimeValue converted = type.convert(unConvert, context);
                        if (converted == null) {
                            throw new UnConvertibleTypeException(unConvert, type, unConvert.getType(context).declType,
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
                VariableDeclaration v = new VariableDeclaration(s.name, type, defaultValue, s.getLineNumber());
                verifyNonConflictingSymbol(context, result, v);
                result.add(v);
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    protected LinkedList<Object> getEnumConstant(ExpressionContext context, ParenthesizedToken parentheses,
                                                 DeclaredType elementType) throws ParsingException {
        LinkedList<Object> linkedList = new LinkedList<>();
        while (parentheses.hasNext()) {
            linkedList.add(getConstantElement(context, parentheses, elementType));
            if (parentheses.hasNext()) {
                parentheses.assertNextSemicolon(parentheses);
            }
        }
        return linkedList;
    }

    protected LinkedList<Object> getSetConstant(ExpressionContext context, BracketedToken bracketedToken,
                                                @Nullable DeclaredType elementType) throws ParsingException {
        LinkedList<Object> linkedList = new LinkedList<>();
        while (bracketedToken.hasNext()) {
            linkedList.add(getConstantElement(context, bracketedToken, elementType));
            if (bracketedToken.hasNext()) {
                bracketedToken.assertNextComma();
            }
        }
        return linkedList;
    }


    private Object getArrayConstant(ExpressionContext context, ArrayType type) throws ParsingException {
        DeclaredType elementTypeOfArray = type.elementType;
        ParenthesizedToken bracketedToken = (ParenthesizedToken) take();
        int size = type.getBounds().size;
        Object[] objects = new Object[size];
        for (int i = 0; i < size; i++) {
            if (!bracketedToken.hasNext()) {
                throw new ExpectedTokenException(",", peek());
            }
            objects[i] = getConstantElement(context, bracketedToken, elementTypeOfArray);
        }
        Log.d(TAG, "getConstantElement: " + Arrays.toString(objects));
        return objects;
    }

    public Object getConstantElement(@NonNull ExpressionContext context,
                                     @NonNull GrouperToken parentheses,
                                     @Nullable DeclaredType elementType) throws ParsingException {
        if (parentheses.hasNext()) {
            if (elementType instanceof ArrayType) {
                if (parentheses.peek() instanceof ParenthesizedToken) {
                    GrouperToken child = (GrouperToken) parentheses.take();
                    Object[] objects = new Object[((ArrayType) elementType).getBounds().size];
                    for (int i = 0; i < objects.length; i++) {
                        objects[i] = getConstantElement(context, child, ((ArrayType) elementType).elementType);
                    }
                    if (child.hasNext()) {
                        throw new ExpectedTokenException(new CommaToken(null), child.peek());
                    }
                    if (parentheses.hasNext()) {
                        parentheses.assertNextComma();
                    }
                    return objects;
                } else {
                    throw new ExpectedTokenException(new ParenthesizedToken(null),
                            parentheses.peek());
                }
            } else {
                RuntimeValue unconvert = parentheses.getNextExpression(context);
                if (elementType != null) {
                    RuntimeValue converted = elementType.convert(unconvert, context);
                    if (converted == null) {
                        throw new UnConvertibleTypeException(unconvert, elementType,
                                unconvert.getType(context).declType, false);
                    }
                    if (parentheses.hasNext()) {
                        parentheses.assertNextComma();
                    }
                    return converted.compileTimeValue(context);
                } else {
                    return unconvert.compileTimeValue(context);
                }
            }
        } else {
            throw new ExpectedTokenException(new CommaToken(null), peek());
        }
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
    public RuntimeValue getSingleValue(ExpressionContext context) throws ParsingException {
        RuntimeValue result = getNextExpression(context);
        if (hasNext()) {
            Token next = take();
            throw new ExpectedTokenException(getClosingText(), next);
        }
        return result;
    }

    public Executable getNextCommand(ExpressionContext context) throws ParsingException {
        Token next = take();
        LineInfo lineNumber = next.getLineNumber();
        if (next instanceof IfToken) {
            return new IfStatement(context, this, lineNumber);
        } else if (next instanceof WhileToken) {
            return new WhileStatement(context, this, lineNumber);
        } else if (next instanceof BeginEndToken) {

            InstructionGrouper beginEndPreprocessed = new InstructionGrouper(lineNumber);
            BeginEndToken castToken = (BeginEndToken) next;

            while (castToken.hasNext()) {
                beginEndPreprocessed.addCommand(castToken.getNextCommand(context));
                Token token = castToken.next;
                if (castToken.hasNext()) {
                    castToken.assertNextSemicolon(token);
                }
            }
            return beginEndPreprocessed;

        } else if (next instanceof ForToken) {
            return generateForStatement(context, lineNumber);
        } else if (next instanceof RepeatToken) {
            InstructionGrouper command = new InstructionGrouper(lineNumber);

            while (!(peekNoEOF() instanceof UntilToken)) {
                command.addCommand(getNextCommand(context));
                if (!(peekNoEOF() instanceof UntilToken)) {
                    assertNextSemicolon(next);
                }
            }
            next = take();
            if (!(next instanceof UntilToken)) {
                throw new ExpectedTokenException("until", next);
            }
            RuntimeValue condition = getNextExpression(context);
            return new RepeatInstruction(command, condition, lineNumber);
        } else if (next instanceof CaseToken) {
            return new CaseInstruction((CaseToken) next, context);
        } else if (next instanceof SemicolonToken) {
            return new NoneInstruction(next.getLineNumber());
        } else if (next instanceof BreakToken) {
            return new BreakInstruction(next.getLineNumber());
        } else if (next instanceof ContinueToken) {
            return new ContinueInstruction(next.getLineNumber());
        } else if (next instanceof WithToken) {
            return (Executable) new WithStatement(context, this).generate();
        } else if (next instanceof ExitToken) {
            return new ExitInstruction(next.getLineNumber());
        } else {
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException ignored) {
            }

            RuntimeValue r = getNextExpression(context, next);
            next = peek();
            if (next instanceof AssignmentToken) {
                take();
                AssignableValue left = r.asAssignableValue(context);

                if (left == null) {
                    throw new UnAssignableTypeException(r);
                }
                RuntimeValue valueToAssign = getNextExpression(context);
                DeclaredType outputType = left.getType(context).declType;
                DeclaredType inputType = valueToAssign.getType(context).declType;
                /*
                 * Does not have to be writable to assign value to variable.
				 */
                RuntimeValue converted = outputType.convert(valueToAssign, context);
                if (converted == null) {
                    throw new UnConvertibleTypeException(valueToAssign, outputType, inputType, true);
                }
                return new Assignment(left, outputType.cloneValue(converted), next.getLineNumber());
            } else if (r instanceof Executable) {
                return (Executable) r;
            } else if (r instanceof FieldAccess) {
                FieldAccess fieldAccess = (FieldAccess) r;
                RuntimeValue container = fieldAccess.getContainer();
                return (Executable) getMethodFromClass(context, container, fieldAccess.getName());
            } else {
                throw new NotAStatementException(r);
            }
        }
    }

    private Executable generateForStatement(ExpressionContext context, LineInfo lineNumber) throws ParsingException {
        RuntimeValue tmpVal = getNextExpression(context);
        AssignableValue tmpVariable = tmpVal.asAssignableValue(context);
        if (tmpVariable == null) {
            throw new UnAssignableTypeException(tmpVal);
        }
        Token next = take();
        if (!(next instanceof AssignmentToken
                || next instanceof OperatorToken)) {
            throw new ExpectedTokenException("\":=\" or \"in\"", next);
        }
        Executable result = null;
        if (next instanceof AssignmentToken) {
            RuntimeValue firstValue = getNextExpression(context);
//            RuntimeValue converted = tmpVal.getType(context).convert(firstValue, context);
       /*     if (converted == null) {
                throw new UnConvertibleTypeException(firstValue, tmpVariable.getType(context).declType,
                        firstValue.getType(context).declType, tmpVal);
            }*/
//            firstValue = converted;

            next = take();
            boolean downto = false;
            if (next instanceof DowntoToken) {
                downto = true;
            } else if (!(next instanceof ToToken)) {
                throw new ExpectedTokenException(next, "to", "downto");
            }
            RuntimeValue lastValue = getNextExpression(context);
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
        } else {
            if (((OperatorToken) next).type == OperatorTypes.IN) {

            } else {
                throw new ExpectedTokenException(next, ":=", "in");
            }
        }
        return result;
    }

    private RuntimeValue getMethodFromClass(ExpressionContext context, RuntimeValue container, String
            methodName) throws ParsingException {

        RuntimeType type = container.getType(context);

        //access method of java class
        if (type.declType instanceof JavaClassBasedType) {
            JavaClassBasedType javaType = (JavaClassBasedType) type.declType;
            Class<?> storageClass = javaType.getStorageClass();

            //indexOf arguments
            List<RuntimeValue> argumentsForCall = new ArrayList<>();
            if (hasNext()) {
                if (peek() instanceof ParenthesizedToken) {
                    ParenthesizedToken token = (ParenthesizedToken) take();
                    argumentsForCall = token.getArgumentsForCall(context);
                }
            }

            //indexOf method, ignore case
            Method[] declaredMethods = storageClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equalsIgnoreCase(methodName)) {
                    MethodDeclaration methodDeclaration =
                            new MethodDeclaration(container, declaredMethod, javaType);
                    FunctionCall functionCall = methodDeclaration.generateCall(getLineNumber(),
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
