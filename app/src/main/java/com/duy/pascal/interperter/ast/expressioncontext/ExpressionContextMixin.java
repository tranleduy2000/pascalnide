package com.duy.pascal.interperter.ast.expressioncontext;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.interperter.ast.CodeUnitParsingException;
import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.codeunit.RuntimePascalClass;
import com.duy.pascal.interperter.ast.codeunit.RuntimeUnitPascal;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.LibraryIdentifierAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.VariableAccess;
import com.duy.pascal.interperter.datastructure.ArrayListMultimap;
import com.duy.pascal.interperter.declaration.LabelDeclaration;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.classunit.ClassConstructor;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.function.FunctionDeclaration;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.OperatorTypes;
import com.duy.pascal.interperter.declaration.lang.types.PascalClassType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.RuntimeType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.declaration.library.PascalUnitDeclaration;
import com.duy.pascal.interperter.exceptions.Diagnostic;
import com.duy.pascal.interperter.exceptions.DiagnosticsListener;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.PermissionDeniedException;
import com.duy.pascal.interperter.exceptions.parsing.UnSupportTokenException;
import com.duy.pascal.interperter.exceptions.parsing.UnrecognizedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.define.OverridingFunctionBodyException;
import com.duy.pascal.interperter.exceptions.parsing.define.UnknownIdentifierException;
import com.duy.pascal.interperter.exceptions.parsing.io.LibraryNotFoundException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.ExpectedTokenException;
import com.duy.pascal.interperter.exceptions.parsing.syntax.WrongIfElseStatement;
import com.duy.pascal.interperter.exceptions.parsing.value.NonConstantExpressionException;
import com.duy.pascal.interperter.exceptions.parsing.value.NonIntegerException;
import com.duy.pascal.interperter.javaunderpascal.classpath.JavaClassLoader;
import com.duy.pascal.interperter.libraries.IPascalLibrary;
import com.duy.pascal.interperter.libraries.PascalLibraryManager;
import com.duy.pascal.interperter.libraries.file.FileLib;
import com.duy.pascal.interperter.libraries.io.IOLib;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.source.ScriptSource;
import com.duy.pascal.interperter.tokens.OperatorToken;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.basic.ColonToken;
import com.duy.pascal.interperter.tokens.basic.ConstToken;
import com.duy.pascal.interperter.tokens.basic.ConstructorToken;
import com.duy.pascal.interperter.tokens.basic.DestructorToken;
import com.duy.pascal.interperter.tokens.basic.ElseToken;
import com.duy.pascal.interperter.tokens.basic.FunctionToken;
import com.duy.pascal.interperter.tokens.basic.LabelToken;
import com.duy.pascal.interperter.tokens.basic.PeriodToken;
import com.duy.pascal.interperter.tokens.basic.ProcedureToken;
import com.duy.pascal.interperter.tokens.basic.SemicolonToken;
import com.duy.pascal.interperter.tokens.basic.TypeToken;
import com.duy.pascal.interperter.tokens.basic.UsesToken;
import com.duy.pascal.interperter.tokens.basic.VarToken;
import com.duy.pascal.interperter.tokens.grouping.BeginEndToken;
import com.duy.pascal.interperter.tokens.grouping.BracketedToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.tokens.ignore.CompileDirectiveToken;
import com.duy.pascal.ui.runnable.ProgramHandler;
import com.duy.pascal.ui.utils.DLog;

import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.interperter.libraries.PascalLibraryManager.MAP_LIBRARIES;

public abstract class ExpressionContextMixin extends HierarchicalExpressionContext
        implements Cloneable, Serializable {
    private static final String TAG = "ExpressionContextMixin";

    /**
     * list global variable
     */
    public ArrayList<VariableDeclaration> variables = new ArrayList<>();

    protected String mContextName = "";
    /**
     * activity value, uses for input and output
     */
    @Nullable
    private ProgramHandler mHandler;
    /**
     * list function and procedure pascal, support overload function
     */
    private ArrayListMultimap<Name, AbstractFunction> callableFunctions = ArrayListMultimap.create();
    /*defined constants*/
    private HashMap<Name, ConstantDefinition> mConstants = new HashMap<>();
    /*defined libraries*/
    private HashMap<PascalUnitDeclaration, RuntimeUnitPascal> mRuntimeUnitMap = new HashMap<>();

    private Map<Name, RuntimePascalClass> mRuntimePascalClassMap = new HashMap<>();

    /**
     * define labels
     */
    private HashMap<Name, LabelDeclaration> labelsMap = new HashMap<>();
    /*user define type*/
    private HashMap<Name, Type> typedefs = new HashMap<>();
    /**
     * List name library which program are in use
     */
    private ArrayList<Name> mLibrariesNames = new ArrayList<>();

    private PascalLibraryManager mLibraryManager;
    /**
     * Class loader, load class in library rt.jar (java library) and other file *.class
     */
    private JavaClassLoader mClassLoader;
    /**
     * Manager read and write into a file
     */
    private FileLib mFileHandler;
    /**
     * Manager read and write to console
     */
    private IOLib mIOHandler;

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent,
                                  @NonNull ProgramHandler handler) {
        super(root, parent);

        this.mHandler = handler;
        mLibraryManager = new PascalLibraryManager(this, handler);
        mFileHandler = new FileLib(handler);
        mIOHandler = new IOLib(handler);
        try {
            //load system function
            mLibraryManager.loadSystemLibrary();
            mLibraryManager.addMethodFromLibrary(FileLib.class, mFileHandler, LineInfo.SYSTEM_LINE);
            mLibraryManager.addMethodFromLibrary(IOLib.class, mIOHandler, LineInfo.SYSTEM_LINE);
        } catch (PermissionDeniedException | LibraryNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayListMultimap<Name, AbstractFunction> getCallableFunctions() {
        return callableFunctions;
    }

    public Map<Name, ConstantDefinition> getConstants() {
        return mConstants;
    }

    public HashMap<Name, Type> getTypedefs() {
        return typedefs;
    }

    public ArrayList<VariableDeclaration> getVariables() {
        return variables;
    }

    public FunctionDeclaration getExistFunction(@NonNull FunctionDeclaration f)
            throws Exception {
        for (AbstractFunction g : callableFunctions.get(f.getName())) {
            if (f.headerMatches(g)) {
                if (!(g instanceof FunctionDeclaration)) {
                    throw new OverridingFunctionBodyException(g, f);
                }
                return (FunctionDeclaration) g;
            }
        }
        callableFunctions.put(f.getName(), f);
        return f;
    }

    @Override
    public RuntimeValue getIdentifierValue(WordToken name) throws Exception {
        if (functionExistsLocal(name.getName())) {
            return FunctionCall.generateFunctionCall(name, new ArrayList<RuntimeValue>(0), this);

        } else if (getConstantDefinitionLocal(name.getName()) != null) {
            ConstantDefinition c = getConstantDefinition(name.getName());
            ConstantAccess<Object> constant = new ConstantAccess<>(c.getValue(), c.getType(), name.getLineNumber());
            constant.setName(name.name);
            return constant;

        } else if (getVariableDefinitionLocal(name.getName()) != null) {
            return new VariableAccess(name.getName(), name.getLineNumber(), this);

        } else if (getLabelLocal(name.getName()) != null) {

        } else {
        }

        //find identifier in library
        for (Map.Entry<PascalUnitDeclaration, RuntimeUnitPascal> unit : mRuntimeUnitMap.entrySet()) {
            RuntimeUnitPascal value = unit.getValue();
            ExpressionContextMixin libContext = value.getDeclaration().getContext();
            RuntimeValue identifierValue = libContext.getIdentifierValue(name);
            if (identifierValue != null) {
                return new LibraryIdentifierAccess(value, identifierValue, identifierValue.getLineNumber());
            }
        }

        if (parent == null) {
            throw new UnknownIdentifierException(name.getLineNumber(), name.getName(), this);
        }
        try {
            return parent.getIdentifierValue(name);
        } catch (Exception e) {
            throw new UnknownIdentifierException(name.getLineNumber(), name.getName(), this);
        }
    }

    public void verifyNonConflictingSymbolLocal(NamedEntity namedEntity)
            throws DuplicateIdentifierException {
        Name name = namedEntity.getName();
        if (functionExistsLocal(name)) {
            throw new DuplicateIdentifierException(getCallableFunctionsLocal(namedEntity.getName()).get(0), namedEntity);
        } else if (getVariableDefinitionLocal(name) != null) {
            throw new DuplicateIdentifierException(getVariableDefinitionLocal(name), namedEntity);
        } else if (getConstantDefinitionLocal(name) != null) {
            throw new DuplicateIdentifierException(getConstantDefinitionLocal(name), namedEntity);
        } else if (getTypedefTypeLocal(name) != null) {
            throw new DuplicateIdentifierException(getTypedefTypeLocal(name), namedEntity);
        } else if (getLabelLocal(name) != null) {
            throw new DuplicateIdentifierException(getLabelLocal(name), namedEntity);
        }
    }

    public LabelDeclaration getLabelLocal(Name name) {
        return labelsMap.get(name);
    }

    public void addNextDeclaration(GrouperToken group) throws Exception {
        try {
            Token next = group.peek();
            if (next instanceof ProcedureToken || next instanceof FunctionToken) {
                Token functionToken = group.take();
                boolean isProcedure = functionToken instanceof ProcedureToken;
                Name name = group.nextWordValue();
                if (group.peek() instanceof PeriodToken) {
                    Type typedefType = getTypeDef(name);
                    if (typedefType instanceof PascalClassType) {
                        // TODO: 17-Aug-17 implement
                    } else {
                        throw new ExpectedTokenException(";", group.peek());
                    }
                    PascalClassType classType = (PascalClassType) typedefType;

                    group.take();
                    name = group.nextWordValue();
                    Type typeInClass = classType.getDeclaration().getContext().getTypedefTypeLocal(name);
                    while (typeInClass != null && typeInClass instanceof PascalClassType) {
                        classType = (PascalClassType) typeInClass;
                        if (group.peek() instanceof PeriodToken) {
                            group.take();
                        } else {
                        }
                        name = group.nextWordValue();
                        typeInClass = classType.getDeclaration().getContext().getTypedefTypeLocal(name);
                    }
                    FunctionDeclaration declaration = new FunctionDeclaration(name,
                            classType.getClassContext(), group, isProcedure, functionToken.getLineNumber());
                    FunctionDeclaration function = classType.getClassContext().getExistFunction(declaration);
                    function.parseFunctionBody(group);
                } else {
                    FunctionDeclaration function = new FunctionDeclaration(name, this, group,
                            isProcedure, functionToken.getLineNumber());
                    function = getExistFunction(function);
                    function.parseFunctionBody(group);
                }

            } else if (next instanceof BeginEndToken) {
                handleBeginEnd(group);

            } else if (next instanceof VarToken) { //supported diagnostic
                group.take();
                List<VariableDeclaration> d = group.getVariableDeclarations(this);
                for (VariableDeclaration dec : d) {
                    declareVariable(dec);
                }
            } else if (next instanceof ConstToken) { //supported diagnostic
                group.take();
                addDeclareConsts(group);
            } else if (next instanceof UsesToken) { //supported diagnostic
                group.take();
                importLibraries(group);
                group.assertNextSemicolon();
            } else if (next instanceof TypeToken) {//supported diagnostic
                group.take();
                addDeclareTypes(group);
            } else if (next instanceof LabelToken) {
                throw new UnSupportTokenException(next);
            } else if (next instanceof CompileDirectiveToken) {
                CompileDirectiveToken compileDirectiveToken = (CompileDirectiveToken) group.take();
                String[] message = compileDirectiveToken.getMessage();
                root().getConfig().process(message);
            } else if (next instanceof ConstructorToken) {
                Token constructorToken = group.take();
                Name name = group.nextWordValue();
                if (group.peek() instanceof PeriodToken) {
                    Type typedefType = getTypeDef(name);
                    if (typedefType instanceof PascalClassType) {

                    } else {
                        throw new ExpectedTokenException(";", group.peek());
                    }
                    PascalClassType classType = (PascalClassType) typedefType;

                    group.take();
                    Name funcName = group.nextWordValue();
                    ClassConstructor declaration = new ClassConstructor(classType, funcName,
                            classType.getClassContext(), group, true, constructorToken.getLineNumber());
                    FunctionDeclaration constructor = classType.getConstructor(declaration);
                    if (constructor != null) {
                        constructor.parseFunctionBody(group);
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    FunctionDeclaration function = new FunctionDeclaration(name, this, group, true,
                            constructorToken.getLineNumber());
                    function = getExistFunction(function);
                    function.parseFunctionBody(group);
                }
            } else if (next instanceof DestructorToken) {
                Token destructorToken = group.take();
                LineInfo startLine = destructorToken.getLineNumber();
                Name name = group.nextWordValue();
                if (group.peek() instanceof PeriodToken) {
                    Type typedefType = getTypeDef(name);
                    if (typedefType instanceof PascalClassType) {

                    } else {
                        throw new ExpectedTokenException(";", group.peek());
                    }
                    PascalClassType classType = (PascalClassType) typedefType;

                    group.take();
                    Name funcName = group.nextWordValue();
                    FunctionDeclaration declaration = new FunctionDeclaration(funcName,
                            classType.getClassContext(), group, true, startLine);
                    FunctionDeclaration destructor = classType.getClassContext().getDestructor();
                    if (destructor.headerMatches(declaration)) {
                        destructor.parseFunctionBody(group);
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    FunctionDeclaration function = new FunctionDeclaration(name, this, group, true, startLine);
                    function = getExistFunction(function);
                    function.parseFunctionBody(group);
                }
            } else {
                Token token = group.take();
                handleUnrecognizedDeclaration(token, group);
            }
        } catch (ParsingException e) {
            DiagnosticsListener listener = getListener(DiagnosticsListener.class);
            if (listener != null) {
                listener.add(new Diagnostic(e));
            } else {
                throw e;
            }
        }
    }

    private void declareLabels(GrouperToken i) throws Exception {
        Token next;
        do {
            next = i.take();
            if (!(next instanceof WordToken)) {
                throw new ExpectedTokenException("[Label Identifier]", next);
            }

            LabelDeclaration labelDeclaration = new LabelDeclaration(((WordToken) next).name,
                    next.getLineNumber());
            verifyNonConflictingSymbol(labelDeclaration);
            labelsMap.put(labelDeclaration.getName(), labelDeclaration);
            next = i.peek();
            if (next instanceof SemicolonToken) {
                break;
            } else {
                i.assertNextComma();
            }
        } while (true);
    }

    public void addDeclareTypes(GrouperToken grouperToken) throws Exception {
        Token next;
        Hashtable<Name, Name> forwardTypes = new Hashtable<>();

        while (grouperToken.peek() instanceof WordToken) {
            WordToken name = (WordToken) grouperToken.take();
            next = grouperToken.take();

            if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
                ExpectedTokenException e = new ExpectedTokenException("=", next);
                reportException(this, grouperToken, e);
                continue;
            }

            //because the type can be forward type
            //example
            //type  a = ^b;
            //      b = integer;
            if (grouperToken.peek() instanceof OperatorToken && ((OperatorToken) grouperToken.peek()).type == OperatorTypes.DEREF) {
                grouperToken.take();
                String typeName = grouperToken.peek().toString();
                try {

                    Type type = grouperToken.getNextPascalType(this);
                    type.setLineNumber(name.getLineNumber());
                    type.setName(name.getName());
                    verifyNonConflictingSymbol(type);
                    declareTypedef(name.getName(), type);
                    grouperToken.assertNextSemicolon();

                } catch (Exception e) {
                    DLog.e(e);
                    PointerType type = new PointerType(null);

                    type.setLineNumber(name.getLineNumber());
                    type.setName(name.getName());
                    verifyNonConflictingSymbol(type);
                    declareTypedef(name.getName(), type);
                    grouperToken.assertNextSemicolon();

                    forwardTypes.put(name.getName(), Name.create(typeName));
                }
            } else {
                Type type = grouperToken.getNextPascalType(this);

                //process string with define length
                if (type.equals(BasicType.StringBuilder)) {
                    if (grouperToken.peek() instanceof BracketedToken) {
                        BracketedToken bracketedToken = (BracketedToken) grouperToken.take();

                        RuntimeValue unconverted = bracketedToken.getNextExpression(this);
                        RuntimeValue converted = BasicType.Integer.convert(unconverted, this);

                        if (converted == null) {
                            NonIntegerException e = new NonIntegerException(unconverted);
                            reportException(this, grouperToken, e);
                            continue;
                        }

                        if (bracketedToken.hasNext()) {
                            ExpectedTokenException e = new ExpectedTokenException("]", bracketedToken.take());
                            reportException(this, grouperToken, e);
                            continue;
                        }
                    }
                }
                type.setLineNumber(name.getLineNumber());
                type.setName(name.getName());

                verifyNonConflictingSymbol(type);
                declareTypedef(name.getName(), type);
                grouperToken.assertNextSemicolon();
            }
        }

        //pre check forward pointer type
        for (Name s : forwardTypes.keySet()) {
            PointerType pointerType = (PointerType) getTypedefTypeLocal(s);
            Type type = getTypedefTypeLocal(forwardTypes.get(s));
            if (type == null) {
                throw new UnknownIdentifierException(pointerType.getLineNumber(), s, this);
            }
            pointerType.setPointerToType(type);
        }
    }

    /**
     * If library has builtin in application, this method will be import it
     * Else search library in application path, if not found library, throw
     * exception
     */
    protected void importLibraries(GrouperToken grouperToken) throws Exception {
        Token next;
        do {
            next = grouperToken.take();
            if (!(next instanceof WordToken)) {
                ExpectedTokenException e = new ExpectedTokenException("[Library Identifier]", next);
                reportException(this, grouperToken, e);
                return;
            }

            AtomicBoolean found = new AtomicBoolean(false);
            //find builtin library
            Class<? extends IPascalLibrary> classLibrary = MAP_LIBRARIES.get(((WordToken) next).getName());
            if (classLibrary != null) {
                found.set(true);
                mLibrariesNames.add(((WordToken) next).name);
                mLibraryManager.addMethodFromClass(classLibrary, next.getLineNumber());
            } else {
                //custom library pascal
                String libName = ((WordToken) next).getName() + ".pas";
                Reader reader = null;
                for (ScriptSource scriptSource : this.root().getIncludeDirectories()) {
                    reader = scriptSource.read(libName);
                    if (reader != null) break;
                }
                if (reader != null) {
                    found.set(true);
                    PascalUnitDeclaration library = null;
                    try {
                        String name = ((WordToken) next).getName().getOriginName();
                        library = new PascalUnitDeclaration(reader, name, new ArrayList<ScriptSource>(), mHandler);
                    } catch (CodeUnitParsingException e) {
                        e.printStackTrace();
                        throw e.getParseException();
                    }
                    library.declareConstants(this);
                    library.declareTypes(this);
                    library.declareFunctions(this);
                    mRuntimeUnitMap.put(library, library.generate());
                }
            }

            if (!found.get()) {
                LibraryNotFoundException e = new LibraryNotFoundException(next.getLineNumber(), ((WordToken) next).getName());
                reportException(this, grouperToken, e);
                return;
            }
            next = grouperToken.peek();
            if (next instanceof SemicolonToken) {
                break;
            } else {
                grouperToken.assertNextComma();
            }
        } while (true);
    }


    protected abstract void handleBeginEnd(GrouperToken i) throws Exception;

    public VariableDeclaration getVariableDefinitionLocal(Name ident) {
        for (VariableDeclaration v : variables) {
            if (v.getName().equals(ident)) {
                return v;
            }
        }
        return null;
    }

    public List<AbstractFunction> getCallableFunctionsLocal(Name name) {
        return callableFunctions.get(name);
    }

    public boolean functionExistsLocal(Name name) {
        return callableFunctions.containsKey(name);
    }

    public ConstantDefinition getConstantDefinitionLocal(Name identifier) {
        return mConstants.get(identifier);
    }

    @Override
    public Type getTypedefTypeLocal(Name identifer) {
        return typedefs.get(identifer);
    }

    /**
     * define custom type
     */
    public void declareTypedef(Name name, Type type) {
        typedefs.put(name, type);
    }

    /**
     * define custom type
     */
    public void declareTypedef(String name, Type type) {
        declareTypedef(Name.create(name), type);
    }

    public void declareTypedefs(Name name, List<Type> types) {
        for (Type type : types) {
            declareTypedef(name, type);
        }
    }

    public void declareVariable(VariableDeclaration v) {
        variables.add(v);
    }

    public void declareFunction(AbstractFunction f) {
        callableFunctions.put(f.getName(), f);
    }

    public void declareConst(ConstantDefinition c) {
        mConstants.put(c.getName(), c);
    }

    public void addDeclareConsts(GrouperToken grouperToken) throws Exception {
        Token next;
        while (grouperToken.peek() instanceof WordToken) {
            WordToken name = (WordToken) grouperToken.take(); //const a : integer = 2; const a = 2;
            next = grouperToken.take();
            if (next instanceof ColonToken) {// const a : array[1..3] of integer = (1, 2, 3);
                try {
                    Type type = grouperToken.getNextPascalType(this);
                    Object constVal;
                    if (grouperToken.peek() instanceof OperatorToken) {
                        if (((OperatorToken) grouperToken.peek()).type == OperatorTypes.EQUALS) {
                            grouperToken.take(); //ignore equal name
                            ConstantDefinition c = new ConstantDefinition(name.getName(), type, name.getLineNumber());
                            declareConst(c);

                            //value of constant
                            constVal = grouperToken.getConstantValue(this, type,
                                    getIdentifierValue(name)); //identifier for auto fix if can not convert type
                            c.setValue(constVal);

                            grouperToken.assertNextSemicolon();
                        }
                    } else { //missing init value
                        reportException(parent, grouperToken, new ExpectedTokenException("[init value]", grouperToken.peek()));
                    }
                } catch (ParsingException e) { //type not found
                    reportException(parent, grouperToken, e);
                }
            } else if (next instanceof OperatorToken) { //const a = 2; , non define operator
                if (((OperatorToken) next).type != OperatorTypes.EQUALS) {//only accept equal token
                    ExpectedTokenException e = new ExpectedTokenException("=", name);
                    reportException(parent, grouperToken, e);
                } else {
                    try {
                        RuntimeValue value = grouperToken.getNextExpression(this);
                        RuntimeType type = value.getRuntimeType(this);
                        Object constVal = value.compileTimeValue(this);
                        if (constVal == null) {
                            throw new NonConstantExpressionException(value);
                        }
                        ConstantDefinition c = new ConstantDefinition(name.getName(), type.declType, constVal, name.getLineNumber());
                        this.mConstants.put(c.getName(), c);
                        grouperToken.assertNextSemicolon();
                    } catch (ParsingException e) { //error when parsing expression value
                        reportException(parent, grouperToken, e);
                    }
                }
            } else {
                ExpectedTokenException e = new ExpectedTokenException("=", name);
                reportException(parent, grouperToken, e);
            }
        }

    }

    private void reportException(@Nullable ExpressionContext context, GrouperToken grouperToken, ParsingException e) throws Exception {
        System.out.println("ExpressionContextMixin.reportException");
        if (context == null) {
            throw e;
        }
        DiagnosticsListener listener = context.getListener(DiagnosticsListener.class);
        if (listener != null) {
            listener.add(new Diagnostic(e));
            grouperToken.nextStatement();
        } else {
            throw e;
        }
    }

    @Override
    public CodeUnit root() {
        return root;
    }

    @Override
    public Executable handleUnrecognizedStatement(Token next, GrouperToken container)
            throws Exception {
        try {
            Executable result = handleUnrecognizedStatementImpl(next, container);
            if (result != null) {
                return result;
            }
        } catch (ParsingException ignored) {
        }

        Executable result = parent == null ? null : parent
                .handleUnrecognizedStatement(next, container);

        if (result == null) {
            if (next instanceof ElseToken) {
                throw new WrongIfElseStatement(next);
            }
            throw new UnrecognizedTokenException(next);
        }
        return result;
    }

    protected abstract Executable handleUnrecognizedStatementImpl(Token next, GrouperToken container)
            throws Exception;

    protected abstract boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
            throws Exception;

    @Override
    public boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
            throws Exception {
        boolean result = handleUnrecognizedDeclarationImpl(next, container)
                || (parent != null && parent.handleUnrecognizedDeclaration(next, container));
        if (!result) {
            if (next instanceof ElseToken) {
                throw new WrongIfElseStatement(next);
            }
            throw new UnrecognizedTokenException(next);
        }
        return true;
    }


    public HashMap<PascalUnitDeclaration, RuntimeUnitPascal> getRuntimeUnitMap() {
        return mRuntimeUnitMap;
    }

    public Map<Name, RuntimePascalClass> getRuntimePascalClassMap() {
        return mRuntimePascalClassMap;
    }

    public FileLib getFileHandler() {
        return mFileHandler;
    }

    public IOLib getIOHandler() {
        return mIOHandler;
    }

    @Override
    public ExpressionContextMixin clone() throws CloneNotSupportedException {
        super.clone();
        return null;
    }


}
