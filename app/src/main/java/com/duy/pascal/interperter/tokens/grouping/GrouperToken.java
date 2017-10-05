package com.duy.pascal.interperter.tokens.grouping;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.expressioncontext.ClassExpressionContext;
import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.ast.instructions.BreakInstruction;
import com.duy.pascal.interperter.ast.instructions.CompoundStatement;
import com.duy.pascal.interperter.ast.instructions.ContinueInstruction;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.instructions.ExitInstruction;
import com.duy.pascal.interperter.ast.instructions.LabelInstruction;
import com.duy.pascal.interperter.ast.instructions.NopeInstruction;
import com.duy.pascal.interperter.ast.instructions.assign_statement.AssignStatement;
import com.duy.pascal.interperter.ast.instructions.assign_statement.DivAssignStatement;
import com.duy.pascal.interperter.ast.instructions.assign_statement.MinusAssignStatement;
import com.duy.pascal.interperter.ast.instructions.assign_statement.MulAssignStatement;
import com.duy.pascal.interperter.ast.instructions.assign_statement.PlusAssignStatement;
import com.duy.pascal.interperter.ast.instructions.case_statement.CaseInstruction;
import com.duy.pascal.interperter.ast.instructions.conditional.IfStatement;
import com.duy.pascal.interperter.ast.instructions.conditional.RepeatInstruction;
import com.duy.pascal.interperter.ast.instructions.conditional.WhileStatement;
import com.duy.pascal.interperter.ast.instructions.conditional.forstatement.ForStatement;
import com.duy.pascal.interperter.ast.instructions.with_statement.WithStatement;
import com.duy.pascal.interperter.ast.runtime_value.operators.BinaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime_value.operators.UnaryOperatorEval;
import com.duy.pascal.interperter.ast.runtime_value.operators.pointer.DerefEval;
import com.duy.pascal.interperter.ast.runtime_value.value.AssignableValue;
import com.duy.pascal.interperter.ast.runtime_value.value.ClassConstructorCall;
import com.duy.pascal.interperter.ast.runtime_value.value.EnumElementValue;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RecordValue;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ClassFunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ClassVariableAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.FieldAccess;
import com.duy.pascal.interperter.declaration.LabelDeclaration;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.lang.function.MethodDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.PascalClassType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RecordType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.StringLimitType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.types.set.ArrayType;
import com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType;
import com.duy.pascal.interperter.declaration.lang.types.set.SetType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.EnumSubrangeType;
import com.duy.pascal.interperter.declaration.lang.types.subrange.IntegerRange;
import com.duy.pascal.interperter.declaration.lang.types.subrange.SubrangeType;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.Diagnostic;
import com.duy.pascal.interperter.exceptions.DiagnosticsListener;
import com.duy.pascal.interperter.exceptions.parsing.UnSupportTokenException;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.convert.UnConvertibleTypeException;
import com.duy.pascal.interperter.exceptions.parsing.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.MethodNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownFieldException;
import com.duy.pascal.interperter.exceptions.parsing.grouping.GroupingException;
import com.duy.pascal.interperter.exceptions.parsing.index.NonIntegerIndexException;
import com.duy.pascal.interperter.exceptions.parsing.missing.MissingCommaTokenException;
import com.duy.pascal.interperter.exceptions.parsing.operator.BadOperationTypeException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedAnotherTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.NotAStatementException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.WrongIfElseStatement;
import com.duy.pascal.interperter.exceptions.parsing.value.ChangeValueConstantException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonConstantExpressionException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonIntegerException;
import com.duy.pascal.interperter.exceptions.parsing.value.UnAssignableTypeException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.EOFToken;
import com.duy.pascal.interperter.tokens.OperatorToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.TokenUtil;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.ArrayToken;
import com.duy.pascal.interperter.tokens.basic.AssignmentToken;
import com.duy.pascal.interperter.tokens.basic.BreakToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.CommaToken;
import com.duy.pascal.interperter.tokens.basic.ContinueToken;
import com.duy.pascal.interperter.tokens.basic.DivAssignToken;
import com.duy.pascal.interperter.tokens.basic.ElseToken;
import com.duy.pascal.interperter.tokens.basic.ExitToken;
import com.duy.pascal.interperter.tokens.basic.ForToken;
import com.duy.pascal.interperter.tokens.basic.GotoToken;
import com.duy.pascal.interperter.tokens.basic.IfToken;
import com.duy.pascal.interperter.tokens.basic.MinusAssignToken;
import com.duy.pascal.interperter.tokens.basic.MultiplyAssignToken;
import com.duy.pascal.interperter.tokens.basic.OfToken;
import com.duy.pascal.interperter.tokens.basic.PeriodToken;
import com.duy.pascal.interperter.tokens.basic.PlusAssignToken;
import com.duy.pascal.interperter.tokens.basic.RepeatToken;
import com.duy.pascal.interperter.tokens.basic.SemicolonToken;
import com.duy.pascal.interperter.tokens.basic.SetToken;
import com.duy.pascal.interperter.tokens.basic.WhileToken;
import com.duy.pascal.interperter.tokens.basic.WithToken;
import com.duy.pascal.interperter.tokens.ignore.CommentToken;
import com.duy.pascal.interperter.tokens.ignore.GroupingExceptionToken;
import com.duy.pascal.interperter.tokens.value.ValueToken;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import static com.duy.pascal.interperter.declaration.lang.types.set.ArrayType.getArrayConstant;
import static com.duy.pascal.interperter.declaration.lang.types.set.EnumGroupType.getEnumType;

public abstract class GrouperToken extends Token {
    private static final String TAG = GrouperToken.class.getSimpleName();
    public Token next = null;
    protected LinkedBlockingQueue<Token> queue;
    protected LineInfo endLine;

    public GrouperToken(LineInfo line) {
        super(line);
        queue = new LinkedBlockingQueue<>();
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
     * @throws Exception
     */
    private static void assertNextCommaForNextConstant(ExpressionContext context, GrouperToken grouperToken,
                                                       Type elementType) throws Exception {
        if (grouperToken.hasNext()) {       //assert next comma token
            Token t = grouperToken.peek();
            if (!(t instanceof CommaToken)) {
                try {
                    getConstantElement(context, grouperToken, elementType);
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
     * @param groupConstant - parent
     * @param elementType   - the type of element
     * @return constant object
     */
    public static ConstantAccess getConstantElement(@NonNull ExpressionContext context,
                                                    @NonNull GrouperToken groupConstant,
                                                    @Nullable Type elementType) throws Exception {

        if (groupConstant.hasNext()) {
            if (elementType instanceof ArrayType) {
                GrouperToken child = (GrouperToken) groupConstant.take();
                Object[] array = getArrayConstant(context, child,
                        (ArrayType) elementType).getValue();

                assertNextCommaForNextConstant(context, groupConstant, elementType);

                return new ConstantAccess<>(array, elementType, child.line);

            } else if (elementType instanceof EnumGroupType) {
                Token next = groupConstant.take();
                ConstantAccess<EnumElementValue> constant = EnumGroupType.getEnumConstant(groupConstant,
                        context, next, elementType);
                EnumElementValue enumConstant = constant.getValue();

                assertNextCommaForNextConstant(context, groupConstant, elementType);

                return new ConstantAccess<>(enumConstant, enumConstant.getRuntimeType(context).declType,
                        next.getLineNumber());
            } else if (elementType instanceof RecordType) {
                Token next = groupConstant.take();
                ConstantAccess<RecordValue> constant = RecordType.getRecordConstant(context,
                        next, (RecordType) elementType);

                RecordValue enumConstant = constant.getValue();

                assertNextCommaForNextConstant(context, groupConstant, elementType);
                return new ConstantAccess<>(enumConstant, elementType, next.getLineNumber());

            } else if (elementType instanceof SetType) {
                if (groupConstant.peek() instanceof BracketedToken) {
                    AtomicReference<Type> elementTypeReference =
                            new AtomicReference<>(((SetType) elementType).getElementType());
                    BracketedToken bracketedToken = (BracketedToken) groupConstant.take();

                    ConstantAccess<LinkedList> constant = SetType.getSetConstant(context,
                            bracketedToken, elementTypeReference);

                    LinkedList setConstant = constant.getValue();

                    assertNextCommaForNextConstant(context, groupConstant, elementType);

                    return new ConstantAccess<>(setConstant, elementType,
                            bracketedToken.getLineNumber());
                } else {
                    throw new ExpectedTokenException(new ParenthesizedToken(null),
                            groupConstant.peek());
                }
            } else {
                RuntimeValue unconvert = groupConstant.getNextExpression(context);
                if (elementType != null) {
                    RuntimeValue converted = elementType.convert(unconvert, context);
                    if (converted == null) {
                        throw new UnConvertibleTypeException(unconvert, elementType,
                                unconvert.getRuntimeType(context).declType, context);
                    }

                    assertNextCommaForNextConstant(context, groupConstant, elementType);

                    return new ConstantAccess<>(converted.compileTimeValue(context), elementType,
                            unconvert.getLineNumber());
                } else {
                    assertNextCommaForNextConstant(context, groupConstant, elementType);

                    return new ConstantAccess<>(unconvert.compileTimeValue(context),
                            unconvert.getLineNumber());
                }
            }
        } else {
            throw new ExpectedTokenException(new CommaToken(null), groupConstant.next);
        }
    }

    /**
     * check duplicate declare variable
     *
     * @param context: scope of variable
     */
    private static void checkDuplicateVariableIdentifier(ExpressionContext context, List<VariableDeclaration> result,
                                                         VariableDeclaration var) throws DuplicateIdentifierException {
        for (VariableDeclaration variableDeclaration : result) {
            context.verifyNonConflictingSymbol(var);
            if (variableDeclaration.getName().equals(var.getName())) {
                throw new DuplicateIdentifierException(variableDeclaration, var);
            }
        }
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

    public Name nextWordValue() throws Exception {
        return take().getWordValue().name;
    }

    public void assertNextSemicolon() throws Exception {
        Token t = take();
        if (!(t instanceof SemicolonToken)) {
            throw new ExpectedTokenException(new SemicolonToken(null), t);
        }
    }

    public void assertNextComma() throws Exception {
        Token t = take();
        if (!(t instanceof CommaToken)) {
            throw new ExpectedTokenException(new CommaToken(null), t);
        }
    }

    public Type getNextPascalType(ExpressionContext context) throws Exception {
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
            Type pointed_type = getNextPascalType(context);
            return new PointerType(pointed_type);
        } else if (n instanceof ClassToken) {
            ClassToken classToken = (ClassToken) n;
            PascalClassType result = new PascalClassType(context.root(), context);
            while (classToken.hasNext()) {
                classToken.addDeclaresTo(result, result.getClassContext());
            }
            return result;
        } else if (n instanceof ValueToken || n instanceof OperatorToken) {
            return SubrangeType.getRangeType(this, context, n);
        } else if (!(n instanceof WordToken)) {
            throw new ExpectedTokenException("[Type Identifier]", n);
        }
        Type declaredType = ((WordToken) n).toBasicType(context);
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

    private Type getSetType(ExpressionContext context, LineInfo lineInfo)
            throws Exception {
        Token n = peekNoEOF();
        if (!(n instanceof OfToken)) {
            throw new ExpectedTokenException(new OfToken(null), n);
        }
        take(); //of token
        Type elementType = getNextPascalType(context);

        return new SetType<>(elementType, lineInfo);
    }

    private Type getArrayType(ExpressionContext context) throws Exception {
        Token n = peekNoEOF();
        if (n instanceof BracketedToken) {
            BracketedToken bracket = (BracketedToken) take();
            return getArrayType(bracket, context);
        } else if (n instanceof OfToken) {
            take();
            Type elementType = getNextPascalType(context);
            return new ArrayType<>(elementType, null);
        } else {
            throw new ExpectedTokenException("of", n);
        }
    }

    private Type getArrayType(BracketedToken bounds, ExpressionContext context)
            throws Exception {
        Type pascalType = bounds.getNextPascalType(context);
        if (pascalType instanceof EnumGroupType) {
            pascalType = new EnumSubrangeType((EnumGroupType) pascalType);
        }
        if (!(pascalType instanceof IntegerRange)) {
            throw new RuntimeException();
            //// TODO: 14-Jun-17  check exception
        }
        IntegerRange bound = (IntegerRange) pascalType;
        Type elementType;
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
                                          Token next) throws Exception {

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
                Type type1 = term.getRuntimeType(context).declType;
                Type type2 = nextValue.getRuntimeType(context).declType;
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
                RuntimeType runtimeType = term.getRuntimeType(context);
                //access method of java class
                if (runtimeType.declType instanceof JavaClassBasedType) {
                    term = getMethodFromJavaClass(context, term, ((WordToken) next).getName());

                } else if (runtimeType.declType instanceof PascalClassType) {
                    PascalClassType pascalClassType = (PascalClassType) runtimeType.declType;
                    ClassExpressionContext classContext = pascalClassType.getClassContext();
                    try {
                        term = getFunctionFromPascalClass(context, term, (WordToken) next);
                    } catch (Exception e) {
                        Name name = Name.create(term.toString());
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
                                    (RecordType) runtimeType.getRawType(),
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
                                                    WordToken methodName) throws Exception {
        RuntimeType type = container.getRuntimeType(context);

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
        return new ClassFunctionCall(Name.create(container.toString()),
                functionCall, methodName.getLineNumber(), classContext);
    }

    private RuntimeValue generateArrayAccess(RuntimeValue parent, ExpressionContext f,
                                             BracketedToken b)
            throws Exception {

        RuntimeType type = parent.getRuntimeType(f);
        RuntimeValue unconvert = b.getNextExpression(f);

        //try convert to integer
        RuntimeValue converted = BasicType.Integer.convert(unconvert, f);

        if (converted == null) { //can not convert to integer -> it can be enum index
            if (type.declType instanceof ArrayType) {//check if container is array
                ArrayType arrayType = (ArrayType) type.declType;

                //if the type of index is enum type
                if (arrayType.getBound() instanceof EnumSubrangeType
                        && unconvert.getRuntimeType(f).declType instanceof EnumGroupType) {
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
                                          Precedence precedence) throws Exception {
        return getNextExpression(context, precedence, take());
    }

    public RuntimeValue getNextTerm(ExpressionContext context, Token next)
            throws Exception {

        if (next instanceof ParenthesizedToken) {
            return ((ParenthesizedToken) next).getSingleValue(context);

        } else if (next instanceof ValueToken) {
            return new ConstantAccess<>(((ValueToken) next).getValue(), next.getLineNumber());

        } else if (next instanceof BracketedToken) {
            AtomicReference<Type> elementTypeReference = new AtomicReference<>(null);
            return SetType.getSetRuntime(context, next, elementTypeReference);

        } else if (next instanceof WordToken) {
            WordToken name = ((WordToken) next);
            next = peek();

            if (next instanceof ParenthesizedToken) {
                List<RuntimeValue> arguments;
                if (name.name.equals("writeln") || name.name.equals("write")) {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForOutput(context);
                } else {
                    arguments = ((ParenthesizedToken) take()).getArgumentsForCall(context);
                }
                return FunctionCall.generateFunctionCall(name, arguments, context);
            } else {
                Type typedefType = context.getTypeDef(name.getName());
                if (typedefType != null) { //check class instance
                    if (typedefType instanceof PascalClassType) {
                        PascalClassType classType = (PascalClassType) typedefType;
                        if (next instanceof PeriodToken) {
                            take();
                            next = take();
                            if (next instanceof WordToken) {
                                WordToken idName = ((WordToken) next);
                                Type typeInClass = classType.getDeclaration()
                                        .getContext().getTypedefTypeLocal(idName.getName());
                                while (typeInClass != null && typeInClass instanceof PascalClassType) {
                                    classType = (PascalClassType) typeInClass;
                                    if (peek() instanceof PeriodToken) {
                                        take();
                                    } else {
                                    }
                                    if (peek() instanceof WordToken) {
                                        idName = (WordToken) take();
                                    } else {

                                    }
                                    typeInClass = classType.getDeclaration().getContext()
                                            .getTypedefTypeLocal(idName.getName());
                                }

                                List<RuntimeValue> arguments = new ArrayList<>();
                                if (peek() instanceof ParenthesizedToken) {
                                    arguments.addAll(((ParenthesizedToken) take()).getArgumentsForCall(context));
                                }
                                return classType.generateConstructor(idName, arguments,
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
                if (identifier.getLineNumber() != null) {
                    identifier.getLineNumber().setLength(name.name.getLength());
                }
                if (peek() instanceof OperatorToken && ((OperatorToken) peek()).type == OperatorTypes.DEREF) {
                    take();
                    identifier = new DerefEval(identifier, identifier.getLineNumber());
                }
                return identifier;
            }
        } else {
            if (next instanceof ElseToken) {
                throw new WrongIfElseStatement(next);
            }
            throw new UnrecognizedTokenException(next);
        }
    }

    public RuntimeValue getNextTerm(ExpressionContext context)
            throws Exception {
        return getNextTerm(context, take());
    }

    public RuntimeValue getNextExpression(ExpressionContext context)
            throws Exception {
        return getNextExpression(context, Precedence.NoPrecedence);
    }

    public RuntimeValue getNextExpression(ExpressionContext context, Token first)
            throws Exception {
        return getNextExpression(context, Precedence.NoPrecedence, first);
    }

    public ArrayList<VariableDeclaration> getVariableDeclarations(ExpressionContext context)
            throws Exception {
        ArrayList<VariableDeclaration> result = new ArrayList<>();
        /*
         * reusing it, so it is further out of scope than necessary
		 */
        List<WordToken> names = new ArrayList<>();
        Token next;
        do {
            do {//get list name of variable
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

            try {
                //type of list variable
                Type type = getNextPascalType(context);

                //default value
                Object defValue = null;
                if (peek() instanceof OperatorToken) {
                    if (((OperatorToken) peek()).type == OperatorTypes.EQUALS) {
                        take(); //ignore equal token
                        defValue = getConstantValue(context, type);
                    }
                }

                if (hasNext()) {
                    assertNextSemicolon();
                }

                for (WordToken s : names) {
                    VariableDeclaration v = new VariableDeclaration(s.name, type, defValue, s.getLineNumber());
                    //check duplicate name
                    checkDuplicateVariableIdentifier(context, result, v);
                    result.add(v);
                }
            } catch (Exception e) { //not found variable
                DiagnosticsListener listener = context.getListener(DiagnosticsListener.class);
                if (listener != null) {
                    listener.add(new Diagnostic(e));
                    nextStatement();
                } else {
                    throw e;
                }
            }
            names.clear(); // reusing the list object
            next = peek();
        } while (next instanceof WordToken);
        return result;
    }

    /**
     * move to next statement
     */
    public void nextStatement() {
        try {
            while (peek() != null
                    && !(peek() instanceof SemicolonToken)
                    && !(TokenUtil.isStartStatement(peek()))) {
                take();
            }
        } catch (Exception ignored) {
        }
    }

    public Object getConstantValue(ExpressionContext context, Type type)
            throws Exception {
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
    public Object getConstantValue(@NonNull ExpressionContext context, @NonNull Type type,
                                   @Nullable RuntimeValue left) throws Exception {
        Object defValue;
        //set default value for array
        if (type instanceof ArrayType) {
            defValue = ArrayType.getArrayConstant(context, take(), (ArrayType) type).getValue();

        } else if (type instanceof EnumGroupType) {
            defValue = EnumGroupType.getEnumConstant(this, context, take(), type).getValue();

        } else if (type instanceof SetType) {

            AtomicReference<Type> elementTypeReference
                    = new AtomicReference<>(((SetType) type).getElementType());
            defValue = SetType.getSetConstant(context, take(), elementTypeReference).getValue();

        } else if (type instanceof RecordType) {
            defValue = RecordType.getRecordConstant(context, take(), (RecordType) type);

        } else { //set default single value

            RuntimeValue original = getNextExpression(context);
            RuntimeValue converted = type.convert(original, context);

            if (converted == null) {
                throw new UnConvertibleTypeException(original, type,
                        original.getRuntimeType(context).declType, left, context);
            }

            defValue = converted.compileTimeValue(context);
            if (defValue == null) {
                throw new NonConstantExpressionException(converted);
            }
        }
        return defValue;
    }

    /**
     * Return single value in list value
     * example:
     * <code>parentheses token = (1, 2, 3, 4) -> return 1</code>
     */
    public RuntimeValue getSingleValue(ExpressionContext context) throws Exception {
        RuntimeValue result = getNextExpression(context);
        if (hasNext()) {
            Token next = take();
            throw new ExpectedTokenException(getClosingText(), next);
        }
        return result;
    }

    public Executable getNextCommand(ExpressionContext context) throws Exception {
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
                if (castToken.hasNext()) {
                    castToken.assertNextSemicolon();
                }
            }
            beginEndPreprocessed.setEndLine(((BeginEndToken) next).getEndLine());
            return beginEndPreprocessed;

        } else if (next instanceof ForToken) {
            return ForStatement.generateForStatement(this, context, lineNumber);
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
            } catch (Exception ignored) {
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
                        throw new ChangeValueConstantException((ConstantAccess) identifier, context);
                    }
                    throw new UnAssignableTypeException(identifier);
                }
                RuntimeValue value = getNextExpression(context);
                if (value instanceof ClassConstructorCall) {
                    ((ClassConstructorCall) value).setIdName(Name.create(left.toString()));
                }
                Type valueType = value.getRuntimeType(context).declType;
                Type leftType = left.getRuntimeType(context).declType;

                /*
                 * Does not have to be writable to assign value to variable.
				 */
                RuntimeValue converted = leftType.convert(value, context);
                if (converted == null) {
                    throw new UnConvertibleTypeException(value, leftType, valueType, identifier, context);
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
            } else {
                throw new NotAStatementException(identifier);
            }
        }
    }

    private RuntimeValue getMethodFromJavaClass(ExpressionContext context, RuntimeValue container, Name
            methodName) throws Exception {

        RuntimeType type = container.getRuntimeType(context);

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
                Method[] declaredMethods = clazz.getMethods();
                for (Method declaredMethod : declaredMethods) {
                    if (declaredMethod.getName().equalsIgnoreCase(methodName.getOriginName())) {
                        MethodDeclaration methodDeclaration =
                                new MethodDeclaration(container, declaredMethod);
                        FunctionCall functionCall = methodDeclaration.generateCall(getLineNumber(),
                                argumentsForCall, context);
                        if (functionCall != null) {
                            return functionCall;
                        }
                    }
                }
                break;
            }
            throw new MethodNotFoundException(container.getLineNumber(), methodName, className);
        } else {
            throw new NotAStatementException(container);
        }
    }

    protected abstract String getClosingText();

}
