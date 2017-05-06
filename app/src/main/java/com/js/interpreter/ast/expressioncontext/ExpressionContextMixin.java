package com.js.interpreter.ast.expressioncontext;

import android.util.Log;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.LibraryNotFoundException;
import com.duy.pascal.backend.exceptions.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.NonIntegerException;
import com.duy.pascal.backend.exceptions.OverridingFunctionException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.SameNameException;
import com.duy.pascal.backend.exceptions.UnConvertibleTypeException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.lib.PascalLibraryManager;
import com.duy.pascal.backend.pascaltypes.ArrayType;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.SystemConstants;
import com.duy.pascal.backend.tokens.OperatorToken;
import com.duy.pascal.backend.tokens.OperatorTypes;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.basic.ColonToken;
import com.duy.pascal.backend.tokens.basic.ConstToken;
import com.duy.pascal.backend.tokens.basic.FunctionToken;
import com.duy.pascal.backend.tokens.basic.ProcedureToken;
import com.duy.pascal.backend.tokens.basic.SemicolonToken;
import com.duy.pascal.backend.tokens.basic.TypeToken;
import com.duy.pascal.backend.tokens.basic.UsesToken;
import com.duy.pascal.backend.tokens.basic.VarToken;
import com.duy.pascal.backend.tokens.grouping.BeginEndToken;
import com.duy.pascal.backend.tokens.grouping.BracketedToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.tokens.grouping.ParenthesizedToken;
import com.duy.pascal.frontend.activities.RunnableActivity;
import com.duy.pascal.frontend.program_structure.viewholder.StructureType;
import com.duy.pascal.frontend.view.code_view.SuggestItem;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.RValue;
import com.js.interpreter.ast.returnsvalue.VariableAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExpressionContextMixin extends HeirarchicalExpressionContext {
    public static final String TAG = ExpressionContextMixin.class.getSimpleName();
    /**
     * list global variable
     */
    public ArrayList<VariableDeclaration> variables = new ArrayList<>();
    /**
     * activity target, uses for input and output
     */
    private RunnableActivity handler;
    /**
     * list function and procedure pascal
     */
    private ListMultimap<String, AbstractFunction> callableFunctions = ArrayListMultimap.create();
    //name of function in map callableFunctions, uses for get all function
    private ArrayList<SuggestItem> listNameFunctions = new ArrayList<>();

    /**
     * list defined constant
     */
    private Map<String, ConstantDefinition> constants = new HashMap<>();
    //list name of constant map,  use for get all constants
    private ArrayList<SuggestItem> listNameConstants = new ArrayList<>();

    /**
     * list custom operator
     */
    private Map<String, DeclaredType> typedefs = new HashMap<>();
    //uses for get all operator in map typedefs
    private ArrayList<SuggestItem> listNameTypes = new ArrayList<>();
    /**
     * list library
     */
    private ArrayList<String> librarieNames = new ArrayList<>();

    private PascalLibraryManager pascalLibraryManager;

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent,
                                  ListMultimap<String, AbstractFunction> callableFunctions,
                                  RunnableActivity handler) {
        super(root, parent);
//        this.callableFunctions = callableFunctions;
        if (callableFunctions != null)
            this.callableFunctions.putAll(callableFunctions);

        if (handler != null)
            this.handler = handler;
        pascalLibraryManager = new PascalLibraryManager(this, handler);
        //load system function
        pascalLibraryManager.loadSystemLibrary();
        //add constants
        SystemConstants.addSystemConstant(constants);
        SystemConstants.addSystemType(this);
    }


    public ArrayList<String> getLibrarieNames() {
        return librarieNames;
    }

    public ListMultimap<String, AbstractFunction> getCallableFunctions() {
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

    public FunctionDeclaration getExistingFunction(FunctionDeclaration f)
            throws ParsingException {
        for (AbstractFunction g : callableFunctions.get(f.name)) {
            if (f.headerMatches(g)) {
                if (!(g instanceof FunctionDeclaration)) {
                    throw new OverridingFunctionException(g, f);
                }
                return (FunctionDeclaration) g;
            }
        }
        callableFunctions.put(f.name, f);
        listNameFunctions.add(new SuggestItem(StructureType.TYPE_FUNCTION, f.name));
        return f;
    }

    @Override
    public RValue getIdentifierValue(WordToken name)
            throws ParsingException {
        if (functionExistsLocal(name.name)) {
            return FunctionCall.generateFunctionCall(name,
                    new ArrayList<RValue>(0), this);
        } else if (getConstantDefinitionLocal(name.name) != null) {
            ConstantDefinition constantDefinition = getConstantDefinition(name.name);
            return new ConstantAccess(constantDefinition.getValue(), constantDefinition.getType(), name.lineInfo);
        } else if (getVariableDefinitionLocal(name.name) != null) {
            return new VariableAccess(name.name, name.lineInfo);
        }
        if (parent == null) {
            throw new NoSuchFunctionOrVariableException(name.lineInfo, name.name);
        }
        return parent.getIdentifierValue(name);
    }

    public void verifyNonConflictingSymbolLocal(NamedEntity namedEntity)
            throws SameNameException {
        String name = namedEntity.name();
        if (functionExistsLocal(name)) {
            throw new SameNameException(getCallableFunctionsLocal(namedEntity.name()).get(0), namedEntity);
        } else if (getVariableDefinitionLocal(name) != null) {
            throw new SameNameException(getVariableDefinitionLocal(name), namedEntity);
        } else if (getConstantDefinitionLocal(name) != null) {
            throw new SameNameException(getConstantDefinitionLocal(name), namedEntity);
        }
    }

    public void addNextDeclaration(GrouperToken i) throws ParsingException {
        Token next = i.peek();
        if (next instanceof ProcedureToken || next instanceof FunctionToken) {
            i.take();
            boolean is_procedure = next instanceof ProcedureToken;
            FunctionDeclaration declaration = new FunctionDeclaration(this, i, is_procedure);
            declaration = getExistingFunction(declaration);
            declaration.parse_function_body(i);
        } else if (next instanceof BeginEndToken) {
            handleBeginEnd(i);
        } else if (next instanceof VarToken) {
            i.take();
            List<VariableDeclaration> d = i.get_variable_declarations(this);
            for (VariableDeclaration dec : d) {
                declareVariable(dec);
            }
        } else if (next instanceof ConstToken) {
            i.take();
            addConstDeclarations(i);
        } else if (next instanceof UsesToken) {
            i.take();
            do {
                next = i.take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Library Identifier]", next);
                }
                //check library not found
                if (pascalLibraryManager.mapLibraries.get(((WordToken) next).name) == null) {
                    throw new LibraryNotFoundException(next.lineInfo, ((WordToken) next).name);
                }
                librarieNames.add(next.toString());
                pascalLibraryManager.addMethodFromClass(
                        pascalLibraryManager.mapLibraries.get(((WordToken) next).name)
                );
                next = i.peek();
                if (next instanceof SemicolonToken) {
                    break;
                } else {
                    i.assertNextComma();
                }
            } while (true);
            i.assertNextSemicolon();
        } else if (next instanceof TypeToken) {
            i.take();
            while (i.peek() instanceof WordToken) {
                String name = i.nextWordValue();
                next = i.take();
                if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
                    throw new ExpectedTokenException("=", next);
                }

                DeclaredType type = i.get_next_pascal_type(this);

                //process string with define length
                if (type.equals(BasicType.StringBuilder)) {
                    if (i.peek() instanceof BracketedToken) {
                        BracketedToken bracketedToken = (BracketedToken) i.take();

                        RValue unconverted = bracketedToken.getNextExpression(this);
                        RValue converted = BasicType.Integer.convert(unconverted, this);

                        if (converted == null) {
                            throw new NonIntegerException(unconverted);
                        }

                        if (bracketedToken.hasNext()) {
                            throw new ExpectedTokenException("]", bracketedToken.take());
                        }
                        ((BasicType) type).setLength(converted);
                    }
                }

                declareTypedef(name, type);

                i.assertNextSemicolon();
            }
        } /*else if (next instanceof CommentToken) {
            i.take();
            //fix bug when comment in the top of the file
            addConstDeclarations(i);
        } */ else {
            handleUnrecognizedDeclaration(i.take(), i);
        }
    }


    protected abstract void handleBeginEnd(GrouperToken i) throws ParsingException;

    public VariableDeclaration getVariableDefinitionLocal(String ident) {
        for (VariableDeclaration v : variables) {
            if (v.name.equalsIgnoreCase(ident)) {
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

    public ConstantDefinition getConstantDefinitionLocal(String ident) {
        return constants.get(ident);
    }

    public DeclaredType getTypedefTypeLocal(String ident) {
        return typedefs.get(ident);
    }

    public void declareTypedef(String name, DeclaredType type) {
        typedefs.put(name, type);
        listNameTypes.add(new SuggestItem(StructureType.TYPE_DEF, name));
    }

    public void declareVariable(VariableDeclaration v) {
        variables.add(v);
    }

    public void declareFunction(AbstractFunction f) {
        callableFunctions.put(f.name().toLowerCase(), f);
        listNameFunctions.add(new SuggestItem(StructureType.TYPE_FUNCTION, f.name(), f.description()));
    }

    public void declareConst(ConstantDefinition c) {
        constants.put(c.name(), c);
        listNameConstants.add(new SuggestItem(StructureType.TYPE_CONST, c.name()));
    }

    public void addConstDeclarations(GrouperToken token) throws ParsingException {
        Token next;
        while (token.peek() instanceof WordToken) {
            WordToken constName = (WordToken) token.take(); //const a : integer = 2; const a = 2;
            next = token.take();
            if (next instanceof ColonToken) {// const a : array[1..3] of integer = (1, 2, 3);
                DeclaredType type = token.get_next_pascal_type(this);
                Object defaultValue;
                if (token.peek() instanceof OperatorToken) {
                    if (((OperatorToken) token.peek()).type == OperatorTypes.EQUALS) {
                        token.take(); //ignore equal name
                        //set default value for array
                        if (type instanceof ArrayType) {
                            DeclaredType elementTypeOfArray = ((ArrayType) type).element_type;
                            ParenthesizedToken bracketedToken = (ParenthesizedToken) token.take();
                            int size = ((ArrayType) type).getBounds().size;
                            Object[] objects = new Object[size];
                            for (int i = 0; i < size; i++) {
                                if (!bracketedToken.hasNext()) {
                                    // TODO: 27-Apr-17  exception
                                }
                                objects[i] = token.getDefaultValueArray(this, bracketedToken, elementTypeOfArray);
                            }
                            Log.d(TAG, "getDefaultValueArray: " + Arrays.toString(objects));
                            defaultValue = objects;
                        } else {
                            RValue unconverted = token.getNextExpression(this);
                            RValue converted = type.convert(unconverted, this);
                            if (converted == null) {
                                throw new UnConvertibleTypeException(unconverted,
                                        unconverted.get_type(this).declType, type,
                                        true);
                            }
                            defaultValue = converted.compileTimeValue(this);
                            if (defaultValue == null) {
                                throw new NonConstantExpressionException(converted);
                            }


                        }
                        ConstantDefinition constantDefinition = new ConstantDefinition(constName.name,
                                type, defaultValue, constName.lineInfo);
                        declareConst(constantDefinition);
                        token.assertNextSemicolon();
                    }
                } else {
                    // TODO: 08-Apr-17
                }
            } else if (next instanceof OperatorToken) { //const a = 2; , non define operator
                if (((OperatorToken) next).type != OperatorTypes.EQUALS) {
                    throw new ExpectedTokenException("=", constName);
                }
                RValue value = token.getNextExpression(this);
                Object compileVal = value.compileTimeValue(this);
                if (compileVal == null) {
                    throw new NonConstantExpressionException(value);
                }
                ConstantDefinition constantDefinition = new ConstantDefinition(constName.name,
                        compileVal, constName.lineInfo);
                this.constants.put(constantDefinition.name(), constantDefinition);
                token.assertNextSemicolon();
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
        ParsingException e;
        try {
            Executable result = handleUnrecognizedStatementImpl(next, container);
            if (result != null) {
                return result;
            }
        } catch (ParsingException ex) {
            e = ex;
        }

        Executable result = parent == null ? null : parent
                .handleUnrecognizedStatement(next, container);
        if (result == null) {
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
            throw new UnrecognizedTokenException(next);
        }
        return true;
    }


    public ArrayList<SuggestItem> getListNameFunctions() {
        return listNameFunctions;
    }

    public ArrayList<SuggestItem> getListNameConstants() {
        return listNameConstants;
    }

    public ArrayList<SuggestItem> getListNameTypes() {
        return listNameTypes;
    }
}
