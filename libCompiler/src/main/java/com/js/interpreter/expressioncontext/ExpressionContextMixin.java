package com.js.interpreter.expressioncontext;

import android.support.annotation.Nullable;
import android.util.Log;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.exceptions.convert.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.define.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.define.OverridingFunctionBodyException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.exceptions.io.LibraryNotFoundException;
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.syntax.WrongIfElseStatement;
import com.duy.pascal.backend.exceptions.value.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.value.NonIntegerException;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration;
import com.duy.pascal.backend.lib.PascalLibraryManager;
import com.duy.pascal.backend.lib.javaclasspath.JavaClassLoader;
import com.duy.pascal.backend.pascaltypes.set.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.OperatorTypes;
import com.duy.pascal.backend.runtime.value.ConstantAccess;
import com.duy.pascal.backend.runtime.value.FunctionCall;
import com.duy.pascal.backend.runtime.value.RuntimeValue;
import com.duy.pascal.backend.runtime.value.VariableAccess;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.ConstToken;
import com.duy.pascal.backend.tokens.basic.ElseToken;
import com.duy.pascal.backend.tokens.basic.FinalizationToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.ImplementationToken;
import com.duy.pascal.backend.tokens.basic.InitializationToken;
import com.duy.pascal.backend.tokens.basic.InterfaceToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.TypeToken;
import com.duy.pascal.backend.tokens.basic.UsesToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
import com.duy.pascal.backend.tokens.grouping.UnitToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.duy.pascal.frontend.code_editor.editor_view.adapters.InfoItem;
import com.duy.pascal.frontend.file.ApplicationFileManager;
import com.duy.pascal.frontend.structure.viewholder.StructureType;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ConstantDefinition;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.CodeUnit;
import com.js.interpreter.codeunit.library.RuntimeUnitPascal;
import com.js.interpreter.codeunit.library.UnitPascal;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.source_include.ScriptSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ExpressionContextMixin extends HierarchicalExpressionContext {
    public static final String TAG = ExpressionContextMixin.class.getSimpleName();
    /**
     * list global variable
     */
    public ArrayList<VariableDeclaration> variables = new ArrayList<>();
    /**
     * activity value, uses for input and output
     */
    @Nullable
    private RunnableActivity handler;
    /**
     * list function and procedure pascal, support overload function
     */
    private ArrayListMultimap<String, AbstractFunction> callableFunctions = ArrayListMultimap.create();

    //name of function in map callableFunctions, uses for indexOf all function
    private ArrayList<InfoItem> listNameFunctions = new ArrayList<>();

    /**
     * defined constants
     */
    private HashMap<String, ConstantDefinition> constants = new HashMap<>();

    /**
     * defined libraries
     */
    private HashMap<UnitPascal, RuntimeUnitPascal> unitsMap = new HashMap<>();


    //list name of constant map,  use for indexOf all constants
    private ArrayList<InfoItem> listNameConstants = new ArrayList<>();

    /**
     * list custom operator
     */
    private HashMap<String, DeclaredType> typedefs = new HashMap<>();
    //uses for indexOf all operator in map typedefs
    private ArrayList<InfoItem> listNameTypes = new ArrayList<>();
    /**
     * list library
     */
    private ArrayList<String> librariesNames = new ArrayList<>();

    private PascalLibraryManager pascalLibraryManager;

    /**
     * Class loader, load class in library rt.jar (java library)
     */
    private JavaClassLoader mClassLoader;

    private boolean isLibrary = false;

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent,
                                  ListMultimap<String, AbstractFunction> callableFunctions,
                                  @Nullable RunnableActivity handler, boolean isLibrary) {
        super(root, parent);

        this.isLibrary = isLibrary;

        if (callableFunctions != null)
            this.callableFunctions.putAll(callableFunctions);

        if (handler != null)
            this.handler = handler;
        pascalLibraryManager = new PascalLibraryManager(this, handler);
        //load system function
        pascalLibraryManager.loadSystemLibrary();

    }


    public ArrayList<String> getLibrariesNames() {
        return librariesNames;
    }

    public ArrayListMultimap<String, AbstractFunction> getCallableFunctions() {
        return callableFunctions;
    }

    public Map<String, ConstantDefinition> getConstants() {
        return constants;
    }

    public Map<String, DeclaredType> getTypedefs() {
        return typedefs;
    }

    public ArrayList<VariableDeclaration> getVariables() {
        return variables;
    }

    public FunctionDeclaration checkExistFunction(FunctionDeclaration f)
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
        listNameFunctions.add(new InfoItem(StructureType.TYPE_FUNCTION, f.getName()));
        return f;
    }

    @Override
    public RuntimeValue getIdentifierValue(WordToken name)
            throws ParsingException {
        if (functionExistsLocal(name.getName())) {
            return FunctionCall.generateFunctionCall(name, new ArrayList<RuntimeValue>(0), this);

        } else if (getConstantDefinitionLocal(name.getName()) != null) {
            ConstantDefinition c = getConstantDefinition(name.getName());
            return new ConstantAccess(c.getValue(), c.getType(), name.getLineNumber());

        } else if (getVariableDefinitionLocal(name.getName()) != null) {
            VariableAccess variableAccess = new VariableAccess(name.getName(), name.getLineNumber());
            Log.d(TAG, "getIdentifierValue() returned: " + variableAccess);
            return variableAccess;
        }

        //find identifier in library
        for (Map.Entry<UnitPascal, RuntimeUnitPascal> unit : unitsMap.entrySet()) {
            RuntimeUnitPascal value = unit.getValue();
            ExpressionContextMixin libContext = value.getDefinition().getContext();
            RuntimeValue identifierValue = libContext.getIdentifierValue(name);
            if (identifierValue != null) {
                return identifierValue;
            }
        }

        if (parent == null) {
            throw new NoSuchFunctionOrVariableException(name.getLineNumber(), name.getName());
        }
        return parent.getIdentifierValue(name);
    }

    public void verifyNonConflictingSymbolLocal(NamedEntity namedEntity)
            throws SameNameException {
        String name = namedEntity.getName();
        if (functionExistsLocal(name)) {
            throw new SameNameException(getCallableFunctionsLocal(namedEntity.getName()).get(0), namedEntity);
        } else if (getVariableDefinitionLocal(name) != null) {
            throw new SameNameException(getVariableDefinitionLocal(name), namedEntity);
        } else if (getConstantDefinitionLocal(name) != null) {
            throw new SameNameException(getConstantDefinitionLocal(name), namedEntity);
        } else if (getTypedefTypeLocal(name) != null) {
            throw new SameNameException(getTypedefTypeLocal(name), namedEntity);
        }
    }

    public void addNextDeclaration(GrouperToken i) throws ParsingException {
        Token next = i.peek();
        if (next instanceof ProcedureToken || next instanceof FunctionToken) {
            i.take();
            boolean isProcedure = next instanceof ProcedureToken;
            FunctionDeclaration function = new FunctionDeclaration(this, i, isProcedure);
            function = checkExistFunction(function);
            function.parseFunctionBody(i);
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
            declareConst(i);
        } else if (next instanceof UsesToken) {
            i.take();
            importLibraries(i);
            i.assertNextSemicolon(i.next);
        } else if (next instanceof TypeToken) {
            i.take();
            addDeclareTypes(i);
        } else {
            Token token = i.take();
            handleUnrecognizedDeclaration(token, i);
        }
    }


    /**
     * Check that the keyword is valid, as in the program
     * that contains the library keyword, throw an exception.
     *
     * @param next - keyword is checked
     */
    private void assertAcceptToken(Token next) throws UnrecognizedTokenException {
        if (next instanceof UnitToken || next instanceof InterfaceToken ||
                next instanceof InitializationToken || next instanceof FinalizationToken ||
                next instanceof ImplementationToken)
            if (!isLibrary) {
                throw new UnrecognizedTokenException(next);
            }
    }

    protected void addDeclareTypes(GrouperToken i) throws ParsingException {
        Token next;
        while (i.peek() instanceof WordToken) {
            WordToken name = (WordToken) i.take();
            next = i.take();
            if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
                throw new ExpectedTokenException("=", next);
            }

            DeclaredType type = i.getNextPascalType(this);

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
//                    type = BasicType.StringLimit;
                   /* try {
                        ((BasicType) type).setLength(converted);
                    } catch (UnsupportedOutputFormatException e) {
                        throw new UnsupportedOutputFormatException(i.getLineNumber());
                    }*/
                }
            }
            type.setLineNumber(name.getLineNumber());
            type.setName(name.getName());

            verifyNonConflictingSymbol(type);
            declareTypedef(name.getName(), type);
            i.assertNextSemicolon(i.next);
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
            //check library not found
            if (PascalLibraryManager.MAP_LIBRARIES.get(((WordToken) next).getName()) != null) {
                found.set(true);
                librariesNames.add(next.toString());
                pascalLibraryManager.addMethodFromClass(
                        PascalLibraryManager.MAP_LIBRARIES.get(((WordToken) next).getName()));

            } else {
                String libPath = (ApplicationFileManager.getApplicationPath() + ((WordToken) next).getName()) + ".pas";
                File file = new File(libPath);
                if (file.exists()) {
                    found.set(true);
                    try {
                        FileReader fileReader = new FileReader(file);

                        UnitPascal library = new UnitPascal(fileReader, ((WordToken) next).getName(),
                                ArrayListMultimap.<String, AbstractFunction>create(),
                                new ArrayList<ScriptSource>(), handler);
                        library.declareConstants(this);
                        library.declareTypes(this);
                        library.declareVariables(this);
                        library.declareFunctions(this);

                        unitsMap.put(library, library.run());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        throw new LibraryNotFoundException(next.getLineNumber(), ((WordToken) next).getName());
                    }
                } else {
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

    public VariableDeclaration getVariableDefinitionLocal(String ident) {
        for (VariableDeclaration v : variables) {
            if (v.getName().equalsIgnoreCase(ident)) {
                return v;
            }
        }
        return null;
    }

    public List<AbstractFunction> getCallableFunctionsLocal(String name) {
        return callableFunctions.get(name);
    }

    public boolean functionExistsLocal(String name) {
        return callableFunctions.containsKey(name);
    }

    public ConstantDefinition getConstantDefinitionLocal(String indent) {
        return constants.get(indent);
    }

    @Override
    public DeclaredType getTypedefTypeLocal(String identifer) {
        return typedefs.get(identifer);
    }

    /**
     * define custom type
     */
    public void declareTypedef(String name, DeclaredType type) {
        typedefs.put(name, type);
        listNameTypes.add(new InfoItem(StructureType.TYPE_DEF, name));
    }


    public void declareVariable(VariableDeclaration v) {
        variables.add(v);
    }

    public void declareFunction(AbstractFunction f) {
        callableFunctions.put(f.getName().toLowerCase(), f);
        InfoItem e = new InfoItem(StructureType.TYPE_FUNCTION, f.getName(), f.getDescription(), f.toString());
        listNameFunctions.add(e);
    }

    public void declareConst(ConstantDefinition c) {
        constants.put(c.getName(), c);
        listNameConstants.add(new InfoItem(StructureType.TYPE_CONST, c.getName()));
    }

    public void declareConst(GrouperToken token) throws ParsingException {
        Token next;
        while (token.peek() instanceof WordToken) {
            WordToken constName = (WordToken) token.take(); //const a : integer = 2; const a = 2;
            next = token.take();
            if (next instanceof ColonToken) {// const a : array[1..3] of integer = (1, 2, 3);
                DeclaredType type = token.getNextPascalType(this);
                Object defaultValue;
                if (token.peek() instanceof OperatorToken) {
                    if (((OperatorToken) token.peek()).type == OperatorTypes.EQUALS) {
                        token.take(); //ignore equal name
                        //set default value for array
                        if (type instanceof ArrayType) {
                            DeclaredType elementTypeOfArray = ((ArrayType) type).elementType;
                            ParenthesizedToken bracketedToken = (ParenthesizedToken) token.take();
                            int size = ((ArrayType) type).getBounds().size;
                            Object[] objects = new Object[size];
                            for (int i = 0; i < size; i++) {
                                if (!bracketedToken.hasNext()) {
                                    // TODO: 27-Apr-17  exception
                                }
                                objects[i] = token.getConstantElement(this, bracketedToken, elementTypeOfArray);
                            }
                            Log.d(TAG, "getConstantElement: " + Arrays.toString(objects));
                            defaultValue = objects;
                        } else {
                            RuntimeValue unconverted = token.getNextExpression(this);
                            RuntimeValue converted = type.convert(unconverted, this);
                            if (converted == null) {
                                throw new UnConvertibleTypeException(unconverted, type, unconverted.getType(this).declType
                                );
                            }
                            defaultValue = converted.compileTimeValue(this);
                            if (defaultValue == null) {
                                throw new NonConstantExpressionException(converted);
                            }


                        }
                        ConstantDefinition constantDefinition = new ConstantDefinition(constName.getName(),
                                type, defaultValue, constName.getLineNumber());
                        declareConst(constantDefinition);
                        token.assertNextSemicolon(token.next);
                    }
                } else {
                    // TODO: 08-Apr-17
                }
            } else if (next instanceof OperatorToken) { //const a = 2; , non define operator
                if (((OperatorToken) next).type != OperatorTypes.EQUALS) {
                    throw new ExpectedTokenException("=", constName);
                }
                RuntimeValue value = token.getNextExpression(this);
                Object compileVal = value.compileTimeValue(this);
                if (compileVal == null) {
                    throw new NonConstantExpressionException(value);
                }
                ConstantDefinition constantDefinition = new ConstantDefinition(constName.getName(),
                        compileVal, constName.getLineNumber());
                this.constants.put(constantDefinition.getName(), constantDefinition);
                token.assertNextSemicolon(token);
            } else {
                throw new ExpectedTokenException("=", constName);
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
        return listNameFunctions;
    }

    public ArrayList<InfoItem> getListNameConstants() {
        return listNameConstants;
    }

    public ArrayList<InfoItem> getListNameTypes() {
        return listNameTypes;
    }

    public HashMap<UnitPascal, RuntimeUnitPascal> getUnitsMap() {
        return unitsMap;
    }
}
