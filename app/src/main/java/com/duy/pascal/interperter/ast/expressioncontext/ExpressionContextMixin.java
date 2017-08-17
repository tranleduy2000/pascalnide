package com.duy.pascal.interperter.ast.expressioncontext;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.pascal.frontend.DLog;
import com.duy.pascal.frontend.editor.view.adapters.InfoItem;
import com.duy.pascal.frontend.runnable.ProgramHandler;
import com.duy.pascal.frontend.structure.viewholder.StructureType;
import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.codeunit.RuntimePascalClass;
import com.duy.pascal.interperter.ast.codeunit.RuntimeUnitPascal;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.FunctionCall;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.ast.runtime_value.value.access.ConstantAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.LibraryIdentifierAccess;
import com.duy.pascal.interperter.ast.runtime_value.value.access.VariableAccess;
import com.duy.pascal.interperter.builtin_libraries.PascalLibrary;
import com.duy.pascal.interperter.builtin_libraries.PascalLibraryManager;
import com.duy.pascal.interperter.builtin_libraries.file.FileLib;
import com.duy.pascal.interperter.builtin_libraries.io.IOLib;
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
import com.google.common.collect.ArrayListMultimap;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.duy.pascal.interperter.builtin_libraries.PascalLibraryManager.MAP_LIBRARIES;

public abstract class ExpressionContextMixin extends HierarchicalExpressionContext implements Cloneable {
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
    //name of function in map callableFunctions, uses for get all function
    private ArrayList<InfoItem> mListNameFunctions = new ArrayList<>();
    /*defined constants*/
    private HashMap<Name, ConstantDefinition> mConstants = new HashMap<>();
    /*defined libraries*/
    private HashMap<PascalUnitDeclaration, RuntimeUnitPascal> mRuntimeUnitMap = new HashMap<>();

    private Map<Name, RuntimePascalClass> mRuntimePascalClassMap = new HashMap<>();

    /**
     * define labels
     */
    private HashMap<Name, LabelDeclaration> labelsMap = new HashMap<>();
    //list name of constant map,  use for get all constants
    private ArrayList<InfoItem> mListNameConstants = new ArrayList<>();
    /*user define type*/
    private HashMap<Name, Type> typedefs = new HashMap<>();
    /**
     * uses for get all type in map typedefs
     */
    private ArrayList<InfoItem> mListNameTypes = new ArrayList<>();
    /**
     * List name library which program are in use
     */
    private ArrayList<Name> mLibrariesNames = new ArrayList<>();

    private PascalLibraryManager mPascalLibraryManager;
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
        mPascalLibraryManager = new PascalLibraryManager(this, handler);
        mFileHandler = new FileLib(handler);
        mIOHandler = new IOLib(handler);
        try {
            //load system function
            mPascalLibraryManager.loadSystemLibrary();
            mPascalLibraryManager.addMethodFromLibrary(mFileHandler, new LineInfo(-1, "system"));
            mPascalLibraryManager.addMethodFromLibrary(mIOHandler, new LineInfo(-1, "system"));
        } catch (PermissionDeniedException | LibraryNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Name> getLibrariesNames() {
        return mLibrariesNames;
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
            throws ParsingException {
        for (AbstractFunction g : callableFunctions.get(f.getName())) {
            if (f.headerMatches(g)) {
                if (!(g instanceof FunctionDeclaration)) {
                    throw new OverridingFunctionBodyException(g, f);
                }
                return (FunctionDeclaration) g;
            }
        }
        callableFunctions.put(f.getName(), f);
        mListNameFunctions.add(new InfoItem(StructureType.TYPE_FUNCTION, f.getName()));
        return f;
    }

    @Override
    public RuntimeValue getIdentifierValue(WordToken name)
            throws ParsingException {
        if (functionExistsLocal(name.getName())) {
            return FunctionCall.generateFunctionCall(name, new ArrayList<RuntimeValue>(0), this);

        } else if (getConstantDefinitionLocal(name.getName()) != null) {
            ConstantDefinition c = getConstantDefinition(name.getName());
            ConstantAccess<Object> constant = new ConstantAccess<>(c.getValue(),
                    c.getType(), name.getLineNumber());
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

    public void addNextDeclaration(GrouperToken i) throws ParsingException {
        Token next = i.peek();
        if (next instanceof ProcedureToken || next instanceof FunctionToken) {
            i.take();
            boolean isProcedure = next instanceof ProcedureToken;
            Name name = i.nextWordValue();
            if (i.peek() instanceof PeriodToken) {
                Type typedefType = getTypeDef(name);
                if (typedefType instanceof PascalClassType) {

                } else {
                    throw new ExpectedTokenException(";", i.peek());
                }
                PascalClassType classType = (PascalClassType) typedefType;

                i.take();
                name = i.nextWordValue();
                Type typeInClass = classType.getDeclaration().getContext().getTypedefTypeLocal(name);
                while (typeInClass != null && typeInClass instanceof PascalClassType) {
                    classType = (PascalClassType) typeInClass;
                    if (i.peek() instanceof PeriodToken) {
                        i.take();
                    } else {
                    }
                    name = i.nextWordValue();
                    typeInClass = classType.getDeclaration().getContext().getTypedefTypeLocal(name);
                }
                FunctionDeclaration declaration = new FunctionDeclaration(name,
                        classType.getClassContext(), i, isProcedure);
                FunctionDeclaration function = classType.getClassContext().getExistFunction(declaration);
                function.parseFunctionBody(i);
            } else {
                FunctionDeclaration function = new FunctionDeclaration(name, this, i, isProcedure);
                function = getExistFunction(function);
                function.parseFunctionBody(i);
            }

        } else if (next instanceof BeginEndToken) {
            handleBeginEnd(i);

        } else if (next instanceof VarToken) {
            i.take();
            List<VariableDeclaration> d = i.getVariableDeclarations(this);
            for (VariableDeclaration dec : d) {
                declareVariable(dec);
            }

        } else if (next instanceof ConstToken) {
            i.take();
            addDeclareConsts(i);
        } else if (next instanceof UsesToken) {
            i.take();
            importLibraries(i);
            i.assertNextSemicolon();
        } else if (next instanceof TypeToken) {
            i.take();
            addDeclareTypes(i);
        } else if (next instanceof LabelToken) {
//            declareLabels(i);
            throw new UnSupportTokenException(next);
        } else if (next instanceof CompileDirectiveToken) {
            CompileDirectiveToken compileDirectiveToken = (CompileDirectiveToken) i.take();
            String[] message = compileDirectiveToken.getMessage();
            root().getConfig().process(message);
        } else if (next instanceof ConstructorToken) {
            i.take();
            Name name = i.nextWordValue();
            if (i.peek() instanceof PeriodToken) {
                Type typedefType = getTypeDef(name);
                if (typedefType instanceof PascalClassType) {

                } else {
                    throw new ExpectedTokenException(";", i.peek());
                }
                PascalClassType classType = (PascalClassType) typedefType;

                i.take();
                Name funcName = i.nextWordValue();
                ClassConstructor declaration = new ClassConstructor(classType, funcName,
                        classType.getClassContext(), i, true);
                FunctionDeclaration constructor = classType.getConstructor(declaration);
                if (constructor != null) {
                    constructor.parseFunctionBody(i);
                } else {
                    throw new RuntimeException();
                }
            } else {
                FunctionDeclaration function = new FunctionDeclaration(name, this, i, true);
                function = getExistFunction(function);
                function.parseFunctionBody(i);
            }
        } else if (next instanceof DestructorToken) {
            i.take();

            Name name = i.nextWordValue();
            if (i.peek() instanceof PeriodToken) {
                Type typedefType = getTypeDef(name);
                if (typedefType instanceof PascalClassType) {

                } else {
                    throw new ExpectedTokenException(";", i.peek());
                }
                PascalClassType classType = (PascalClassType) typedefType;

                i.take();
                Name funcName = i.nextWordValue();
                FunctionDeclaration declaration = new FunctionDeclaration(funcName,
                        classType.getClassContext(), i, true);
                FunctionDeclaration destructor = classType.getClassContext().getDestructor();
                if (destructor.headerMatches(declaration)) {
                    destructor.parseFunctionBody(i);
                } else {
                    throw new RuntimeException();
                }
            } else {
                FunctionDeclaration function = new FunctionDeclaration(name, this, i, true);
                function = getExistFunction(function);
                function.parseFunctionBody(i);
            }
        } else {
            Token token = i.take();
            handleUnrecognizedDeclaration(token, i);
        }
    }

    private void declareLabels(GrouperToken i) throws ParsingException {
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

    public void addDeclareTypes(GrouperToken i) throws ParsingException {
        Token next;
        Hashtable<Name, Name> forwardTypes = new Hashtable<>();

        while (i.peek() instanceof WordToken) {
            WordToken name = (WordToken) i.take();
            next = i.take();
            if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
                throw new ExpectedTokenException("=", next);
            }

            //because the type can be forward type
            //example
            //type
            //      a = ^b;
            //      b = integer;
            if (i.peek() instanceof OperatorToken && ((OperatorToken) i.peek()).type == OperatorTypes.DEREF) {
                i.take();
                String typeName = i.peek().toString();
                try {

                    Type type = i.getNextPascalType(this);
                    type.setLineNumber(name.getLineNumber());
                    type.setName(name.getName());
                    verifyNonConflictingSymbol(type);
                    declareTypedef(name.getName(), type);
                    i.assertNextSemicolon();

                } catch (Exception e) {
                    DLog.e(e);
                    PointerType type = new PointerType(null);

                    type.setLineNumber(name.getLineNumber());
                    type.setName(name.getName());
                    verifyNonConflictingSymbol(type);
                    declareTypedef(name.getName(), type);
                    i.assertNextSemicolon();

                    forwardTypes.put(name.getName(), Name.create(typeName));
                }
            } else {
                Type type = i.getNextPascalType(this);

                //process string with define length
                if (type.equals(BasicType.StringBuilder)) {
                    if (i.peek() instanceof BracketedToken) {
                        BracketedToken bracketedToken = (BracketedToken) i.take();

                        RuntimeValue unconverted = bracketedToken.getNextExpression(this);
                        RuntimeValue converted = BasicType.Integer.convert(unconverted, this);

                        if (converted == null) {
                            throw new NonIntegerException(unconverted);
                        }

                        if (bracketedToken.hasNext()) {
                            throw new ExpectedTokenException("]", bracketedToken.take());
                        }
                    }
                }
                type.setLineNumber(name.getLineNumber());
                type.setName(name.getName());

                verifyNonConflictingSymbol(type);
                declareTypedef(name.getName(), type);
                i.assertNextSemicolon();
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
    protected void importLibraries(GrouperToken i) throws ParsingException {
        Token next;
        do {
            next = i.take();
            if (!(next instanceof WordToken)) {
                throw new ExpectedTokenException("[Library Identifier]", next);
            }
            AtomicBoolean found = new AtomicBoolean(false);
            //find builtin library
            Class<? extends PascalLibrary> classLibrary = MAP_LIBRARIES.get(((WordToken) next).getName());
            if (classLibrary != null) {
                found.set(true);
                mLibrariesNames.add(((WordToken) next).name);
                mPascalLibraryManager.addMethodFromClass(classLibrary, next.getLineNumber());
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
                    PascalUnitDeclaration library = new PascalUnitDeclaration(reader, ((WordToken) next).getName().getOriginName(),
                            new ArrayList<ScriptSource>(), mHandler);
                    library.declareConstants(this);
                    library.declareTypes(this);
                    library.declareFunctions(this);
                    mRuntimeUnitMap.put(library, library.generate());
                }
            }

            if (!found.get()) {
                throw new LibraryNotFoundException(next.getLineNumber(), ((WordToken) next).getName());
            }
            next = i.peek();
            if (next instanceof SemicolonToken) {
                break;
            } else {
                i.assertNextComma();
            }
        } while (true);
    }


    protected abstract void handleBeginEnd(GrouperToken i) throws ParsingException;

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
        mListNameTypes.add(new InfoItem(StructureType.TYPE_DEF, name));
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
        InfoItem e = new InfoItem(StructureType.TYPE_FUNCTION, f.getName(), f.getDescription(), f.toString());
        mListNameFunctions.add(e);
    }

    public void declareConst(ConstantDefinition c) {
        mConstants.put(c.getName(), c);
        mListNameConstants.add(new InfoItem(StructureType.TYPE_CONST, c.getName()));
    }

    public void addDeclareConsts(GrouperToken grouperToken) throws ParsingException {
        Token next;
        while (grouperToken.peek() instanceof WordToken) {
            WordToken name = (WordToken) grouperToken.take(); //const a : integer = 2; const a = 2;
            next = grouperToken.take();
            if (next instanceof ColonToken) {// const a : array[1..3] of integer = (1, 2, 3);
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
                } else {
                    throw new ExpectedTokenException("[init value]", grouperToken.peek());
                }
            } else if (next instanceof OperatorToken) { //const a = 2; , non define operator
                if (((OperatorToken) next).type != OperatorTypes.EQUALS) {
                    throw new ExpectedTokenException("=", name);
                }
                RuntimeValue value = grouperToken.getNextExpression(this);
                RuntimeType type = value.getRuntimeType(this);
                Object constVal = value.compileTimeValue(this);
                if (constVal == null) {
                    throw new NonConstantExpressionException(value);
                }
                ConstantDefinition c = new ConstantDefinition(name.getName(), type.declType, constVal, name.getLineNumber());
                this.mConstants.put(c.getName(), c);
                grouperToken.assertNextSemicolon();
            } else {
                throw new ExpectedTokenException("=", name);
            }
        }

    }

    @Override
    public CodeUnit root() {
        return root;
    }

    @Override
    public Executable handleUnrecognizedStatement(Token next, GrouperToken container)
            throws ParsingException {
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
            throws ParsingException;

    protected abstract boolean handleUnrecognizedDeclarationImpl(Token next, GrouperToken container)
            throws ParsingException;

    @Override
    public boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
            throws ParsingException {
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


    public ArrayList<InfoItem> getListNameFunctions() {
        return mListNameFunctions;
    }

    public ArrayList<InfoItem> getListNameConstants() {
        return mListNameConstants;
    }

    public ArrayList<InfoItem> getListNameTypes() {
        return mListNameTypes;
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
