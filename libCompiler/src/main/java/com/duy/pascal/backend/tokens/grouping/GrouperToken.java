package com.duy.pascal.backend.tokens.grouping;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.backend.ast.ConstantDefinition;
import com.duy.pascal.backend.ast.LabelDeclaration;
import com.duy.pascal.backend.ast.MethodDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.classunit.ClassExpressionContext;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.ast.instructions.BreakInstruction;
import com.duy.pascal.backend.ast.instructions.CompoundStatement;
import com.duy.pascal.backend.ast.instructions.ContinueInstruction;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.instructions.ExitInstruction;
import com.duy.pascal.backend.ast.instructions.LabelInstruction;
import com.duy.pascal.backend.ast.instructions.NopeInstruction;
import com.duy.pascal.backend.ast.instructions.assign_statement.AssignStatement;
import com.duy.pascal.backend.ast.instructions.assign_statement.DivAssignStatement;
import com.duy.pascal.backend.ast.instructions.assign_statement.MinusAssignStatement;
import com.duy.pascal.backend.ast.instructions.assign_statement.MulAssignStatement;
import com.duy.pascal.backend.ast.instructions.assign_statement.PlusAssignStatement;
import com.duy.pascal.backend.ast.instructions.case_statement.CaseInstruction;
import com.duy.pascal.backend.ast.instructions.conditional.ForDowntoStatement;
import com.duy.pascal.backend.ast.instructions.conditional.ForInStatement;
import com.duy.pascal.backend.ast.instructions.conditional.ForToStatement;
import com.duy.pascal.backend.ast.instructions.conditional.IfStatement;
import com.duy.pascal.backend.ast.instructions.conditional.RepeatInstruction;
import com.duy.pascal.backend.ast.instructions.conditional.WhileStatement;
import com.duy.pascal.backend.ast.instructions.with_statement.WithStatement;
import com.duy.pascal.backend.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.UnaryOperatorEval;
import com.duy.pascal.backend.ast.runtime_value.operators.pointer.DerefEval;
import com.duy.pascal.backend.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.backend.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.backend.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.ast.runtime_value.value.access.ClassFunctionAccess;
import com.duy.pascal.backend.ast.runtime_value.value.access.ClassVariableAccess;
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.backend.ast.runtime_value.value.access.FieldAccess;
import com.duy.pascal.backend.ast.runtime_value.variables.RecordValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnSupportTokenException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.parse_exception.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.backend.parse_exception.define.MethodNotFoundException;
import com.duy.pascal.backend.parse_exception.define.UnknownFieldException;
import com.duy.pascal.backend.parse_exception.grouping.GroupingException;
import com.duy.pascal.backend.parse_exception.index.LowerGreaterUpperBoundException;
import com.duy.pascal.backend.parse_exception.index.NonIntegerIndexException;
import com.duy.pascal.backend.parse_exception.missing.MissingCommaTokenException;
import com.duy.pascal.backend.parse_exception.operator.BadOperationTypeException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectDoTokenException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedAnotherTokenException;
import com.duy.pascal.backend.parse_exception.syntax.ExpectedTokenException;
import com.duy.pascal.backend.parse_exception.syntax.NotAStatementException;
import com.duy.pascal.backend.parse_exception.syntax.WrongIfElseStatement;
import com.duy.pascal.backend.parse_exception.value.ChangeValueConstantException;
import com.duy.pascal.backend.parse_exception.value.DuplicateElementException;
import com.duy.pascal.backend.parse_exception.value.NonConstantExpressionException;
import com.duy.pascal.backend.parse_exception.value.NonIntegerException;
import com.duy.pascal.backend.parse_exception.value.UnAssignableTypeException;
import com.duy.pascal.backend.tokens.EOFToken;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ArrayToken;
import com.duy.pascal.backend.tokens.basic.AssignmentToken;
import com.duy.pascal.backend.tokens.basic.BasicToken;
import com.duy.pascal.backend.tokens.basic.BreakToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.CommaToken;
import com.duy.pascal.backend.tokens.basic.ContinueToken;
import com.duy.pascal.backend.tokens.basic.DivAssignToken;
import com.duy.pascal.backend.tokens.basic.DoToken;
import com.duy.pascal.backend.tokens.basic.DotDotToken;
import com.duy.pascal.backend.tokens.basic.DowntoToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.ExitToken;
import com.duy.pascal.backend.tokens.basic.ForToken;
import com.duy.pascal.backend.tokens.basic.GotoToken;
import com.duy.pascal.backend.tokens.basic.IfToken;
import com.duy.pascal.backend.tokens.basic.MinusAssignToken;
import com.duy.pascal.backend.tokens.basic.MultiplyAssignToken;
import com.duy.pascal.backend.tokens.basic.OfToken;
import com.duy.pascal.backend.tokens.basic.PeriodToken;
import com.duy.pascal.backend.tokens.basic.PlusAssignToken;
import com.duy.pascal.backend.tokens.basic.RepeatToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.SetToken;
import com.duy.pascal.backend.tokens.basic.ToToken;
import com.duy.pascal.backend.tokens.basic.WhileToken;
import com.duy.pascal.backend.tokens.basic.WithToken;
import com.duy.pascal.backend.tokens.ignore.CommentToken;
import com.duy.pascal.backend.tokens.ignore.GroupingExceptionToken;
import com.duy.pascal.backend.tokens.value.ValueToken;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.JavaClassBasedType;
import com.duy.pascal.backend.types.OperatorTypes;
import com.duy.pascal.backend.types.PascalClassType;
import com.duy.pascal.backend.types.PointerType;
import com.duy.pascal.backend.types.RecordType;
import com.duy.pascal.backend.types.RuntimeType;
import com.duy.pascal.backend.types.StringLimitType;
import com.duy.pascal.backend.types.set.ArrayType;
import com.duy.pascal.backend.types.set.EnumGroupType;
import com.duy.pascal.backend.types.set.SetType;
import com.duy.pascal.backend.types.subrange.BooleanSubrangeType;
import com.duy.pascal.backend.types.subrange.DoubleSubrangeType;
import com.duy.pascal.backend.types.subrange.EnumSubrangeType;
import com.duy.pascal.backend.types.subrange.IntegerRange;
import com.duy.pascal.backend.types.subrange.IntegerSubrangeType;
import com.duy.pascal.backend.types.util.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public abstract class GrouperToken extends Token {
    private static final String TAG = GrouperToken.class.getSimpleName();
    public Token next = null;
    protected LinkedBlockingQueue<Token> queue;
    protected LineInfo endLine;

    public GrouperToken(LineInfo line) {
        super(line);
        queue = new LinkedBlockingQueue<>();
    }

    public LineInfo getEndLine() {
        return endLine;
    }

    public void setEndLine(LineInfo endLine) {
        this.endLine = endLine;
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

    public void assertNextSemicolon() throws ParsingException {
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
            ClassToken classToken = (ClassToken) n;
            PascalClassType result = new PascalClassType(context.root(), context);
            while (classToken.hasNext()) {
                classToken.addDeclaresTo(result, result.getClassContext());
            }
            return result;
        } else if (n instanceof ValueToken || n instanceof OperatorToken) {
            RuntimeValue first = getNextExpression(context, n);

            if (!(peek() instanceof DotDotToken)) {
                throw new ExpectedTokenException("[Type Identifier]", n);
            }
            take(); //dot dot
            RuntimeValue last = getNextExpression(context, Precedence.Relational);
            RuntimeType firstType = first.getType(context);
            RuntimeValue convert = firstType.convert(last, context);
            if (convert == null) {
                throw new UnConvertibleTypeException(last, firstType.declType,
                        last.getType(context).declType, context);
            }
            Object v1 = first.compileTimeValue(context);
            if (v1 == null) {
                throw new NonConstantExpressionException(first);
            }
            Object v2 = last.compileTimeValue(context);
            if (v2 == null) {
                throw new NonConstantExpressionException(last);
            }
            if (TypeUtils.isIntegerType(firstType.getDeclType().getStorageClass())) {
                Integer i1 = Integer.valueOf(v1.toString()); //first value
                Integer size = Integer.valueOf(v2.toString()); //last value
                if (i1 > size) {
                    throw new LowerGreaterUpperBoundException(i1, size, first.getLineNumber());
                }
                size = size - i1 + 1; //size of range
                return new IntegerSubrangeType(i1, size);
            } else if (TypeUtils.isRealType(firstType.getDeclType().getStorageClass())) {
                Double d1 = Double.valueOf(v1.toString());
                Double d2 = Double.valueOf(v2.toString());
                if (d1 > d2) {
                    throw new LowerGreaterUpperBoundException(d1, d2, first.getLineNumber());
                }
                return new DoubleSubrangeType(d1, d2);
            } else if (firstType.getDeclType().getStorageClass() == Boolean.class) {
                Boolean e1 = (Boolean) v1;
                Boolean e2 = (Boolean) v2;
                if (e1.compareTo(e2) > 0) {
                    throw new LowerGreaterUpperBoundException(e1, e2, first.getLineNumber());
                }
                return new BooleanSubrangeType(e1, e2);
            } else if (firstType.getDeclType() instanceof EnumGroupType) {
                EnumElementValue e1 = (EnumElementValue) v1;
                EnumElementValue e2 = (EnumElementValue) v2;
                if (e1.compareTo(e2) > 0) {
                    throw new LowerGreaterUpperBoundException(e1, e2, first.getLineNumber());
                }
                return new EnumSubrangeType(e1, e2);
            }
        } else if (!(n instanceof WordToken)) {
            System.out.println("token " + n.getClass().getName() + " " + n.toString());
            throw new ExpectedTokenException("[Type Identifier]", n);

        }
        DeclaredType declaredType = ((WordToken) n).toBasicType(context);
        //process string with define length
        if (declaredType.equals(BasicType.StringBuilder)) {
            if (peek() instanceof BracketedToken) {
                BracketedToken bracketedToken = (BracketedToken) take();

                RuntimeValue lengthRaw = bracketedToken.getNextExpression(context);
                RuntimeValue lengthInteger = BasicType.Integer.convert(lengthRaw, context);

                if (lengthInteger == null) {
                    throw new NonIntegerException(lengthRaw);
                }

                if (bracketedToken.hasNext()) {
                    throw new ExpectedTokenException("]", bracketedToken.take());
                }
                StringLimitType stringLimitType = new StringLimitType();
                stringLimitType.setLength(lengthInteger);
                return stringLimitType;
            }
        }


        return declaredType;
    }

    private DeclaredType getEnumType(ExpressionContext c, ParenthesizedToken n)
            throws ParsingException {
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
                                value.getType(c).declType, c);
                    }
                    Object oddValue = convert.compileTimeValue(c);
                    if (oddValue == null) {
                        throw new NonConstantExpressionException(convert);
                    }
                    EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType,
                            index.get(), token.getLineNumber());   //create new enum
                    e.setValue((Integer) oddValue);
                    elements.add(e);                    //add to parent
                    ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType,
                            e, e.getLineNumber());
                    c.verifyNonConflictingSymbol(constant); //check duplicate value
                    c.declareConst(constant);                    //add as constant
                } else {
                    throw new ExpectedTokenException(operator, ",", "=");
                }
            } else {

                EnumElementValue e = new EnumElementValue(wordToken.name, enumGroupType, index.get(),
                        token.getLineNumber()); //create new enum
                e.setValue(index.get());
                elements.add(e);        //add to container
                ConstantDefinition constant = new ConstantDefinition(wordToken.name, enumGroupType, e,
                        e.getLineNumber());
                c.declareConst(constant);  //add as constant
            }
            index.getAndIncrement();
            //if has next, check comma token
            if (n.hasNext()) {
                n.assertNextComma();
            }
        }
        return enumGroupType;
    }

    private DeclaredType getSetType(ExpressionContext context, LineInfo lineInfo)
            throws ParsingException {
        Token n = peekNoEOF();
        if (!(n instanceof OfToken)) {
            throw new ExpectedTokenException(new OfToken(null), n);
        }
        take(); //of token
        DeclaredType elementType = getNextPascalType(context);

        return new SetType<>(elementType, lineInfo);
    }

    private DeclaredType getArrayType(ExpressionContext context) throws ParsingException {
        Token n = peekNoEOF();
        if (n instanceof BracketedToken) {
            BracketedToken bracket = (BracketedToken) take();
            return getArrayType(bracket, context);
        } else if (n instanceof OfToken) {
            take();
            DeclaredType elementType = getNextPascalType(context);
            return new ArrayType<>(elementType, null);
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    private DeclaredType getArrayType(BracketedToken bounds, ExpressionContext context)
            throws ParsingException {
        DeclaredType pascalType = bounds.getNextPascalType(context);
        if (pascalType instanceof EnumGroupType) {
            pascalType = new EnumSubrangeType((EnumGroupType) pascalType);
        }
        if (!(pascalType instanceof IntegerRange)) {
            throw new RuntimeException();
            //// TODO: 14-Jun-17  check exception
        }
        IntegerRange bound = (IntegerRange) pascalType;
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
    public RuntimeValue getNextExpression(ExpressionContext context, Precedence precedence,
                                          Token next) throws ParsingException {

        RuntimeValue term;
        Token tmp = next;
        if (next instanceof OperatorToken) {
            OperatorToken nextOperator = (OperatorToken) next;
            if (!nextOperator.canBeUnary() || nextOperator.postfix()) {
                throw new BadOperationTypeException(next.getLineNumber(), nextOperator.type);
            }
            term = UnaryOperatorEval.generateOp(context, getNextExpression(context,
                    nextOperator.type.getPrecedence()), nextOperator.type, nextOperator.getLineNumber());
        } else {
            term = getNextTerm(context, next);
        }

        while ((next = peek()).getOperatorPrecedence() != null) {
            if (next instanceof OperatorToken) {
                OperatorToken nextOperator = (OperatorToken) next;
                if (nextOperator.type.getPrecedence().compareTo(precedence) >= 0) {
                    break;
                }
                take();
                if (nextOperator.postfix()) {
                    return UnaryOperatorEval.generateOp(context, term, nextOperator.type,
                            nextOperator.getLineNumber());
                }
                RuntimeValue nextValue = getNextExpression(context, nextOperator.type.getPrecedence());
                OperatorTypes operationType = ((OperatorToken) next).type;
                DeclaredType type1 = term.getType(context).declType;
                DeclaredType type2 = nextValue.getType(context).declType;
                try {
                    operationType.verifyBinaryOperation(type1, type2);
                } catch (BadOperationTypeException e) {
                    throw new BadOperationTypeException(next.getLineNumber(), type1,
                            type2, term, nextValue, operationType);
                }
                term = BinaryOperatorEval.generateOp(context,
                        term, nextValue, operationType, nextOperator.getLineNumber());
            } else if (next instanceof PeriodToken) {
                take();
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Element Name]", next);
                }


                //call method of java class
                RuntimeType runtimeType = term.getType(context);
                //access method of java class
                if (runtimeType.declType instanceof JavaClassBasedType) {
                    term = getMethodFromJavaClass(context, term, ((WordToken) next).getName());

                } else if (runtimeType.declType instanceof PascalClassType) {
                    PascalClassType pascalClassType = (PascalClassType) runtimeType.declType;
                    ClassExpressionContext classContext = pascalClassType.getClassContext();
                    try {
                        term = getFunctionFromPascalClass(context, term, (WordToken) next);
                    } catch (Exception e) {
                        String name = term.toString();
                        if (classContext.getVariableDefinitionLocal(((WordToken) next).getName()) != null) {
                            term = new ClassVariableAccess(name, ((WordToken) next).getName(),
                                    next.getLineNumber(), classContext);
                        } else if (classContext.getConstantDefinitionLocal(name) != null) {
                            ConstantDefinition c = classContext
                                    .getConstantDefinitionLocal(((WordToken) next).getName());
                            ConstantAccess<Object> constant = new ConstantAccess<>(c.getValue(),
                                    c.getType(), next.getLineNumber());
                            constant.setName(((WordToken) next).getName());
                            term = constant;
                        }
                    }
                } else {
                    if (runtimeType.declType instanceof RecordType) {
                        VariableDeclaration field =
                                ((RecordType) runtimeType.declType).findField(((WordToken) next).getName());
                        if (field == null) { //can not find field
                            // TODO: 03-Jun-17 declare field
                            throw new UnknownFieldException(next.getLineNumber(),
                                    (RecordType) runtimeType.getDeclType(),
                                    ((WordToken) next).getName(), context);
                        }
                    }
                    term = new FieldAccess(term, (WordToken) next);

                    //access pointer value
                    if (peek() instanceof OperatorToken && ((OperatorToken) peek()).type == OperatorTypes.DEREF) {
                        OperatorToken nextOperator = (OperatorToken) take();
                        term = new DerefEval(term, term.getLineNumber());
                    }
                }
            } else if (next instanceof BracketedToken) {
                take(); //comma token
                BracketedToken bracket = (BracketedToken) next;
                term = generateArrayAccess(term, context, bracket);

                while (bracket.hasNext()) {
                    next = bracket.take();
                    if (!(next instanceof CommaToken)) {
                        throw new ExpectedTokenException("]", next);
                    }
                    term = generateArrayAccess(term, context, bracket);
                }
            }
        }
        term.setLineNumber(tmp.getLineNumber());
        return term;
    }

    private RuntimeValue getFunctionFromPascalClass(ExpressionContext context, RuntimeValue container,
                                                    WordToken methodName) throws ParsingException {
        RuntimeType type = container.getType(context);

        //access method of java class

        PascalClassType classType = (PascalClassType) type.declType;
        //get arguments
        List<RuntimeValue> args = new ArrayList<>();
        if (hasNext()) {
            if (peek() instanceof ParenthesizedToken) {
                ParenthesizedToken token = (ParenthesizedToken) take();
                args = token.getArgumentsForCall(context);
            }
        }
        ClassExpressionContext classContext = classType.getClassContext();
        FunctionCall functionCall = FunctionCall.generateFunctionCall(methodName, args, classContext);
        return new ClassFunctionAccess(container.toString(),
                functionCall, methodName.getLineNumber());
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
                if (arrayType.getBound() instanceof EnumSubrangeType
                        && unconvert.getType(f).declType instanceof EnumGroupType) {
                    EnumSubrangeType bounds = (EnumSubrangeType) arrayType.getBound();
                    converted = bounds.getEnumGroupType().convert(unconvert, f);
                    if (converted != null) {
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
                                          Precedence precedence) throws ParsingException {
        return getNextExpression(context, precedence, take());
    }

    public RuntimeValue getNextTerm(ExpressionContext context, Token next)
            throws ParsingException {

        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).getSingleValue(context);

        } else if (next instanceof ValueToken) {
            return new ConstantAccess<>(((ValueToken) next).getValue(), next.getLineNumber());

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
                DeclaredType typedefType = context.getTypedefType(name.getName());
                if (typedefType != null) {
                    if (typedefType instanceof PascalClassType) {
                        PascalClassType classType = (PascalClassType) typedefType;
                        if (next instanceof PeriodToken) {
                            take();
                            next = take();
                            if (next instanceof WordToken) {
                                List<RuntimeValue> arguments = new ArrayList<>();
                                if (peek() instanceof ParenthesizedToken) {
                                    arguments.addAll(((ParenthesizedToken) take()).getArgumentsForCall(context));
                                }
                                return classType.generateConstructor((WordToken) next, arguments,
                                        classType.getClassContext());
                            } else {
                                throw new ExpectedTokenException("[Constructor]", next);
                            }
                        } else {
                            throw new ExpectedTokenException(".", next);
                        }
                    }
                }
                RuntimeValue identifier = context.getIdentifierValue(name);
                //uses for show line error
                identifier.getLineNumber().setLength(name.name.length());
                if (peek() instanceof OperatorToken && ((OperatorToken) peek()).type == OperatorTypes.DEREF) {
                    take();
                    identifier = new DerefEval(identifier, identifier.getLineNumber());
                }
                return identifier;
            }
        } else if (next instanceof BracketedToken) {
            AtomicReference<DeclaredType> elementTypeReference =
                    new AtomicReference<>(null);
            ConstantAccess<LinkedList> constant = getSetConstant(context, next, elementTypeReference);
            LinkedList setValue = constant.getValue();
            SetType<DeclaredType> setType = new SetType<>(elementTypeReference.get(), setValue, line);
            return new ConstantAccess<>(setType.initialize(), setType, constant.getLineNumber());

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
        return getNextExpression(context, Precedence.NoPrecedence);
    }

    public RuntimeValue getNextExpression(ExpressionContext context, Token first)
            throws ParsingException {
        return getNextExpression(context, Precedence.NoPrecedence, first);
    }

    public ArrayList<VariableDeclaration> getVariableDeclarations(ExpressionContext context)
            throws ParsingException {
        ArrayList<VariableDeclaration> result = new ArrayList<>();
        /*
         * reusing it, so it is further out of scope than necessary
		 */
        List<WordToken> names = new ArrayList<>();
        Token next;
        do {
            //get list name of variable
            do {
                next = take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Variable Identifier]", next);
                }
                names.add((WordToken) next);
                next = take();
            }
            while (next instanceof CommaToken); //multi variable, example <code>var a, b, c: integer</code>

            if (!(next instanceof ColonToken)) {
                throw new ExpectedTokenException(":", next);
            }

            //type of list variable
            DeclaredType type = getNextPascalType(context);

            //default value
            Object defaultValue = null;
            if (peek() instanceof OperatorToken) {
                if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                    take(); //ignore equal token
                    defaultValue = getConstantValue(context, type);
                }
            }

            if (hasNext()) {
                assertNextSemicolon();
            }

            for (WordToken s : names) {
                VariableDeclaration v = new VariableDeclaration(s.name, type, defaultValue, s.getLineNumber());
                //check duplicate name
                checkDuplicateVariableIdentifier(context, result, v);
                result.add(v);
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    public Object getConstantValue(ExpressionContext context, DeclaredType type)
            throws ParsingException {
        return getConstantValue(context, type, null);
    }

    /**
     * This method will be parse and get a constant
     *
     * @param context -scope
     * @param type    - the target type of constant. If the type of constant
     *                will be got not equal or can not convert to {type}, this
     *                method will be throw an {@link UnConvertibleTypeException}
     * @param left    - The variable can be assign by constant, it can be null.
     *                (Example <code>x := 1</code> with x is variable)
     * @return then constant with expect type
     */
    public Object getConstantValue(ExpressionContext context, DeclaredType type,
                                   @Nullable RuntimeValue left) throws ParsingException {
        Object defaultValue;
        //set default value for array
        if (type instanceof ArrayType) {
            defaultValue = getArrayConstant(context, take(), (ArrayType) type).getValue();

        } else if (type instanceof EnumGroupType) {
            defaultValue = getEnumConstant(context, take(), type).getValue();

        } else if (type instanceof SetType) {

            AtomicReference<DeclaredType> elementTypeReference
                    = new AtomicReference<>(((SetType) type).getElementType());
            defaultValue = getSetConstant(context, take(), elementTypeReference).getValue();

        } else if (type instanceof RecordType) {
            defaultValue = getRecordConstant(context, take(), (RecordType) type);

        } else { //set default single value

            RuntimeValue unConvert = getNextExpression(context);
            RuntimeValue converted = type.convert(unConvert, context);

            if (converted == null) {
                throw new UnConvertibleTypeException(unConvert, type,
                        unConvert.getType(context).declType, left, context);
            }

            defaultValue = converted.compileTimeValue(context);
            if (defaultValue == null) {
                throw new NonConstantExpressionException(converted);
            }
           /* if (names.size() != 1) {
                throw new MultipleDefaultValuesException(converted.getLineNumber());
            }*/
        }
        return defaultValue;
    }

    private ConstantAccess<RecordValue> getRecordConstant(ExpressionContext context, Token take,
                                                          RecordType ztype) throws ParsingException {
        if (take instanceof ParenthesizedToken) {
            ParenthesizedToken group = (ParenthesizedToken) take;
            RecordType recordType = ztype.clone();
            HashMap<String, Object> initialized = new HashMap<>();
            while (group.hasNext()) {
                Token name = group.take();
                if (!(name instanceof WordToken)) {
                    throw new ExpectedTokenException("[field identifier]", name);
                }
                VariableDeclaration field = recordType.findField(((WordToken) name).name);
                if (field == null) {
                    throw new UnknownFieldException(name.getLineNumber(), recordType,
                            ((WordToken) name).getName(), context);
                }
                if (group.peek() instanceof ColonToken) {
                    group.take();
                } else {
                    throw new ExpectedTokenException(":", group.peek());
                }
                RuntimeValue value = group.getNextExpression(context);
                RuntimeValue convert = field.getType().convert(value, context);
                if (convert == null) {
                    throw new UnConvertibleTypeException(value, field.getType(),
                            value.getType(context).getDeclType(), context);
                }
                Object o = value.compileTimeValue(context);
                if (o == null) {
                    throw new NonConstantExpressionException(value);
                }
                recordType.setFieldValue(((WordToken) name).getName(), o);
                if (group.hasNext()) {
                    group.assertNextSemicolon();
                }
            }
            return new ConstantAccess<>(recordType.initialize(), recordType,
                    group.getLineNumber());
        } else {
            throw new ExpectedTokenException(take, new ParenthesizedToken(null));
        }
    }

    /**
     * @param targetType - type of enum
     * @return the enum constant, I define the enum as {@link LinkedList}
     */
    public ConstantAccess<EnumElementValue> getEnumConstant(ExpressionContext context, Token token,
                                                            DeclaredType targetType) throws ParsingException {
        RuntimeValue expression = getNextExpression(context, token);
        Object constant = expression.compileTimeValue(context);
        if (constant == null) {
            throw new NonConstantExpressionException(expression);
        }
        RuntimeValue convert = targetType.convert(expression, context);
        if (convert == null) {
            throw new UnConvertibleTypeException(expression, targetType, expression.getType(context).declType, context);
        }
        Object o = convert.compileTimeValue(context);
        return new ConstantAccess<>((EnumElementValue) o, targetType, token.getLineNumber());
    }

    public ConstantAccess<LinkedList> getEnumGroupConstant(ExpressionContext context, Token token,
                                                           DeclaredType targetType) throws ParsingException {
        if (!(token instanceof ParenthesizedToken)) {
            throw new ExpectedTokenException("(", token);
        }
        ParenthesizedToken parentheses = (ParenthesizedToken) token;
        LinkedList<Object> linkedList = new LinkedList<>();
        while (parentheses.hasNext()) {
            linkedList.add(getConstantElement(context, parentheses, targetType).getValue());
        }
        return new ConstantAccess<LinkedList>(linkedList, targetType, parentheses.getLineNumber());
    }

    /**
     * @param typeReference - type of set (example: set of char => type is "char")
     * @return the set constant, I define the enum as {@link LinkedList}
     */
    public ConstantAccess<LinkedList> getSetConstant(ExpressionContext context, Token token,
                                                     AtomicReference<DeclaredType> typeReference)
            throws ParsingException {
        if (!(token instanceof BracketedToken)) {
            throw new ExpectedTokenException(new BracketedToken(null), token);
        }

        BracketedToken bracketedToken = (BracketedToken) token;
        LinkedList<Object> linkedList = new LinkedList<>();
        DeclaredType temp = null;
        while (bracketedToken.hasNext()) {
            ConstantAccess element;
            if (typeReference.get() == null) {
                element = getConstantElement(context, bracketedToken, null);
                if (temp == null) {
                    if (element.getValue() instanceof EnumElementValue) {
                        temp = ((EnumElementValue) element.getValue()).getType(context).declType;
                    } else {
                        temp = (element.getType(context).declType);
                    }
                } else if (!(temp.getStorageClass() == Object.class)) {
                    RuntimeValue convert;
                    if (element.getValue() instanceof EnumElementValue) {
                        convert = temp.convert((EnumElementValue) element.getValue(), context);
                    } else {
                        convert = temp.convert(element, context);
                    }
                    if (convert == null) temp = BasicType.create(Object.class);
                }
            } else {
                element = getConstantElement(context, bracketedToken, typeReference.get());
            }
            for (Object o : linkedList) {
                if (o.equals(element.getValue())) {
                    throw new DuplicateElementException(element.getValue(), linkedList, element.getLineNumber());
                }
            }
            linkedList.add(element.getValue());
        }
        if (typeReference.get() == null) typeReference.set(temp);
        return new ConstantAccess<LinkedList>(linkedList, typeReference.get(), bracketedToken.getLineNumber());
    }

    /**
     * parse array constant
     *
     * @param group - parentheses token: the container of array. Example (1, 2, 3)
     * @param type  - element type of array
     * @return - the {@link ConstantAccess} include array object and lineInfo number
     * @throws ParsingException - some token is not expect
     */
    public ConstantAccess<Object[]> getArrayConstant(ExpressionContext context,
                                                     Token group, ArrayType type) throws ParsingException {


        if (!(group instanceof ParenthesizedToken)) {
            throw new ExpectedTokenException("(", group);
        }

        DeclaredType elementType = type.elementType;
        ParenthesizedToken container = (ParenthesizedToken) group;

        //size of array
        int size = type.getBound().getSize();
        //create new array
//        Object[] objects = new Object[size];
        Class<?> elementClass = elementType.getStorageClass();
        Object[] objects = (Object[]) Array.newInstance(elementClass, size);
        for (int i = 0; i < size; i++) {
            if (!container.hasNext()) {
                throw new ExpectedTokenException(",", peek());
            }
            objects[i] = this.<String>getConstantElement(context, container, elementType).getValue();
        }
        return new ConstantAccess<>(objects, type, container.getLineNumber());
    }

    /**
     * @param grouperToken - parent
     * @param elementType  - the type of element
     * @return constant object
     */
    public <T> ConstantAccess getConstantElement(@NonNull ExpressionContext context,
                                                 @NonNull GrouperToken grouperToken,
                                                 @Nullable DeclaredType elementType) throws ParsingException {

        if (grouperToken.hasNext()) {
            if (elementType instanceof ArrayType) {
                GrouperToken child = (GrouperToken) grouperToken.take();
                Object[] array = grouperToken.getArrayConstant(context, child,
                        (ArrayType) elementType).getValue();

                assertNextCommaForNextConstant(context, grouperToken, elementType);

                return new ConstantAccess<>(array, elementType, child.line);

            } else if (elementType instanceof EnumGroupType) {
                Token next = grouperToken.take();
                ConstantAccess<EnumElementValue> constant = grouperToken.getEnumConstant(context,
                        next, elementType);

                EnumElementValue enumConstant = constant.getValue();

                assertNextCommaForNextConstant(context, grouperToken, elementType);

                return new ConstantAccess<>(enumConstant, enumConstant.getType(context).declType,
                        next.getLineNumber());

            } else if (elementType instanceof RecordType) {
                Token next = grouperToken.take();
                ConstantAccess<RecordValue> constant = grouperToken.getRecordConstant(context,
                        next, (RecordType) elementType);

                RecordValue enumConstant = constant.getValue();

                assertNextCommaForNextConstant(context, grouperToken, elementType);
                return new ConstantAccess<>(enumConstant, elementType, next.getLineNumber());

            } else if (elementType instanceof SetType) {
                if (grouperToken.peek() instanceof BracketedToken) {
                    AtomicReference<DeclaredType> elementTypeReference =
                            new AtomicReference<>(((SetType) elementType).getElementType());
                    BracketedToken bracketedToken = (BracketedToken) grouperToken.take();

                    ConstantAccess<LinkedList> constant = grouperToken.getSetConstant(context,
                            bracketedToken, elementTypeReference);

                    LinkedList setConstant = constant.getValue();

                    assertNextCommaForNextConstant(context, grouperToken, elementType);

                    return new ConstantAccess<>(setConstant, elementType,
                            bracketedToken.getLineNumber());
                } else {
                    throw new ExpectedTokenException(new ParenthesizedToken(null),
                            grouperToken.peek());
                }
            } else {
                RuntimeValue unconvert = grouperToken.getNextExpression(context);
                if (elementType != null) {
                    RuntimeValue converted = elementType.convert(unconvert, context);
                    if (converted == null) {
                        throw new UnConvertibleTypeException(unconvert, elementType,
                                unconvert.getType(context).declType, context);
                    }

                    assertNextCommaForNextConstant(context, grouperToken, elementType);

                    return new ConstantAccess<>(converted.compileTimeValue(context), elementType,
                            unconvert.getLineNumber());
                } else {
                    assertNextCommaForNextConstant(context, grouperToken, elementType);

                    return new ConstantAccess<>(unconvert.compileTimeValue(context),
                            unconvert.getLineNumber());
                }
            }
        } else {
            throw new ExpectedTokenException(new CommaToken(null), peek());
        }
    }

    /**
     * Ensure that followed by a "comma", if not, throw an exception,
     * before throwing an exception, try to check if the next token
     * is a value of type equivalent to elementType,
     * If so, throw an exception that is MissingCommaTokenException
     * instead of ExpectedTokenException
     *
     * @param context      -
     * @param grouperToken
     * @param elementType
     * @throws ParsingException
     */
    private void assertNextCommaForNextConstant(ExpressionContext context, GrouperToken grouperToken,
                                                DeclaredType elementType) throws ParsingException {
        if (grouperToken.hasNext()) {       //assert next comma token
            Token t = grouperToken.peek();
            if (!(t instanceof CommaToken)) {
                try {
                    grouperToken.getConstantElement(context, grouperToken, elementType);
                } catch (Exception e) {
                    throw new ExpectedTokenException(new CommaToken(null), t);
                }
                throw new MissingCommaTokenException(t.getLineNumber());
            } else {
                grouperToken.take();
            }
        }
    }


    /**
     * check duplicate declare variable
     *
     * @param context: scope of variable
     */
    private void checkDuplicateVariableIdentifier(ExpressionContext context, List<VariableDeclaration> result,
                                                  VariableDeclaration var) throws DuplicateIdentifierException {
        for (VariableDeclaration variableDeclaration : result) {
            context.verifyNonConflictingSymbol(var);
            if (variableDeclaration.getName().equalsIgnoreCase(var.getName())) {
                throw new DuplicateIdentifierException(variableDeclaration, var);
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

            CompoundStatement beginEndPreprocessed = new CompoundStatement(lineNumber);
            BeginEndToken castToken = (BeginEndToken) next;

            while (castToken.hasNext()) {
                beginEndPreprocessed.addCommand(castToken.getNextCommand(context));
                Token token = castToken.next;
                if (castToken.hasNext()) {
                    castToken.assertNextSemicolon();
                }
            }
            beginEndPreprocessed.setEndLine(((BeginEndToken) next).getEndLine());
            return beginEndPreprocessed;

        } else if (next instanceof ForToken) {
            return generateForStatement(context, lineNumber);
        } else if (next instanceof RepeatToken) {
            return new RepeatInstruction(context, this, lineNumber);

        } else if (next instanceof CaseToken) {
            return new CaseInstruction((CaseToken) next, context);

        } else if (next instanceof SemicolonToken) {
            return new NopeInstruction(next.getLineNumber());

        } else if (next instanceof BreakToken) {
            return new BreakInstruction(next.getLineNumber());

        } else if (next instanceof ContinueToken) {
            return new ContinueInstruction(next.getLineNumber());

        } else if (next instanceof WithToken) {
            return (Executable) new WithStatement(context, this).generate();

        } else if (next instanceof ExitToken) {
            return new ExitInstruction(next.getLineNumber());

        } else if (next instanceof GotoToken) {
            throw new UnSupportTokenException(next);
        } else {
            try {
                return context.handleUnrecognizedStatement(next, this);
            } catch (ParsingException ignored) {
            }

            RuntimeValue identifier = getNextExpression(context, next);
            next = peek();
            Token assign;
            if (next instanceof AssignmentToken) {
                assign = take();
                AssignableValue left = identifier.asAssignableValue(context);

                if (left == null) {
                    if (identifier instanceof ConstantAccess
                            && ((ConstantAccess) identifier).getName() != null) {
                        throw new ChangeValueConstantException((ConstantAccess<Object>) identifier, context);
                    }
                    throw new UnAssignableTypeException(identifier);
                }
                RuntimeValue value = getNextExpression(context);
                DeclaredType valueType = value.getType(context).declType;
                DeclaredType leftType = left.getType(context).declType;

                /*
                 * Does not have to be writable to assign value to variable.
				 */
                RuntimeValue converted = leftType.convert(value, context);
                if (converted == null) {
                    throw new UnConvertibleTypeException(value, leftType, valueType,
                            identifier, context);
                }
                if (assign instanceof PlusAssignToken) {
                    return new PlusAssignStatement(context, left, leftType.cloneValue(converted),
                            next.getLineNumber());
                } else if (assign instanceof MinusAssignToken) {
                    return new MinusAssignStatement(context, left, leftType.cloneValue(converted),
                            next.getLineNumber());
                } else if (assign instanceof MultiplyAssignToken) {
                    return new MulAssignStatement(context, left, leftType.cloneValue(converted),
                            next.getLineNumber());
                } else if (assign instanceof DivAssignToken) {

                    return new DivAssignStatement(context, left, leftType.cloneValue(converted),
                            next.getLineNumber());
                }
                return new AssignStatement(left, leftType.cloneValue(converted), next.getLineNumber());
            } else if (identifier instanceof LabelDeclaration) {
                if (peek() instanceof SemicolonToken) {
                    LabelDeclaration labelLocal = context.getLabelLocal(((LabelDeclaration) identifier).getName());
                    labelLocal.setCommand(getNextCommand(context));
                    return new LabelInstruction(labelLocal.getCommand());
                } else {
                    throw new ExpectedTokenException(":", peek());
                }
            } else if (identifier instanceof Executable) { //function or procedure
                return (Executable) identifier;

            } else if (identifier instanceof FieldAccess) {
                FieldAccess fieldAccess = (FieldAccess) identifier;
                RuntimeValue container = fieldAccess.getContainer();
                return (Executable) getMethodFromJavaClass(context, container, fieldAccess.getName());
            } else if (identifier instanceof ClassFunctionAccess) {
                ClassFunctionAccess functionAccess = (ClassFunctionAccess) identifier;
                return (Executable) functionAccess;
            } else {
                throw new NotAStatementException(identifier);
            }
        }
    }

    private Executable generateForStatement(ExpressionContext context, LineInfo lineNumber) throws ParsingException {
        RuntimeValue varIdentifier = getNextTerm(context);
        AssignableValue varAssignable = varIdentifier.asAssignableValue(context);
        RuntimeType varType = varIdentifier.getType(context);

        if (varAssignable == null) {
            throw new UnAssignableTypeException(varIdentifier);
        }
        Token next = take();
        if (!(next instanceof AssignmentToken
                || next instanceof OperatorToken)) {
            throw new ExpectedTokenException(next, ":=", "in");
        }
        Executable result = null;
        if (next instanceof AssignmentToken) {
            RuntimeValue firstValue = getNextExpression(context);
            RuntimeValue convert = varIdentifier.getType(context).declType.convert(firstValue, context);
            if (convert == null) {
                throw new UnConvertibleTypeException(firstValue, varAssignable.getType(context).declType,
                        firstValue.getType(context).declType, varIdentifier, context);
            }
            firstValue = convert;

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
                if (next instanceof BasicToken) {
                    throw new ExpectedTokenException("do", next);
                } else {
                    throw new ExpectDoTokenException(next.getLineNumber());
                }
            }

            if (downto) { // TODO probably should merge these two types
                result = new ForDowntoStatement(context, varAssignable, firstValue,
                        lastValue, getNextCommand(context), lineNumber);
            } else {
                result = new ForToStatement(context, varAssignable, firstValue,
                        lastValue, getNextCommand(context), lineNumber);
            }
        } else {
            //for in statement
            if (((OperatorToken) next).type == OperatorTypes.IN) {
                //assign value
                RuntimeValue enumList = getNextExpression(context);
                DeclaredType enumType = enumList.getType(context).declType; //type of var

                //accept foreach : enum, set, array
                if (!(enumType instanceof EnumGroupType || enumType instanceof ArrayType
                        || enumType instanceof SetType)) {
                    throw new UnConvertibleTypeException(enumList, varType.declType, enumType, context);
                }

                if (enumType instanceof EnumGroupType) {
                    RuntimeValue converted = varType.convert(enumList, context);
                    if (converted == null) {
                        throw new UnConvertibleTypeException(enumList,
                                varType.declType, enumType, context);
                    }
                } else if (enumType instanceof ArrayType) { //array type
                    ArrayType arrayType = (ArrayType) enumType;
                    RuntimeValue convert = arrayType.getElementType().convert(varIdentifier, context);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(varIdentifier, arrayType.getElementType(), varType.declType, context);
                    }
                } else {
                    SetType setType = (SetType) enumType;
                    RuntimeValue convert = setType.getElementType().convert(varIdentifier, context);
                    if (convert == null) {
                        throw new UnConvertibleTypeException(varIdentifier, setType.getElementType(), varType.declType, context);
                    }
                }

                //check do token
                if (!(peek() instanceof DoToken)) {
                    throw new ExpectedTokenException(new DoToken(null), peek());
                }
                take(); //ignore do token
                //statement
                Executable command = getNextCommand(context);
                return new ForInStatement(varAssignable, enumList, command, lineNumber);
            } else {
                throw new ExpectedTokenException(next, ":=", "in");
            }
        }

        return result;
    }

    private RuntimeValue getMethodFromJavaClass(ExpressionContext context, RuntimeValue container, String
            methodName) throws ParsingException {

        RuntimeType type = container.getType(context);

        //access method of java class
        if (type.declType instanceof JavaClassBasedType) {
            JavaClassBasedType javaType = (JavaClassBasedType) type.declType;

            Class<?> clazz = javaType.getStorageClass();
            String className = clazz.getSimpleName();
            while (true) {

                //get arguments
                List<RuntimeValue> argumentsForCall = new ArrayList<>();
                if (hasNext()) {
                    if (peek() instanceof ParenthesizedToken) {
                        ParenthesizedToken token = (ParenthesizedToken) take();
                        argumentsForCall = token.getArgumentsForCall(context);
                    }
                }

                //get method, ignore case
                Method[] declaredMethods = clazz.getDeclaredMethods();
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
                Class<?>[] interfaces = clazz.getInterfaces();
                clazz = clazz.getSuperclass();
                if (clazz == null) break;
            }
            throw new MethodNotFoundException(container.getLineNumber(), methodName, className);
        } else {
            throw new NotAStatementException(container);
        }
    }

    protected abstract String getClosingText();

}
