package com.js.interpreter.ast.expressioncontext;

import android.util.Log;

import com.duy.pascal.backend.exceptions.ExpectedTokenException;
import com.duy.pascal.backend.exceptions.LibraryNotFoundException;
import com.duy.pascal.backend.exceptions.NoSuchFunctionOrVariableException;
import com.duy.pascal.backend.exceptions.NonConstantExpressionException;
import com.duy.pascal.backend.exceptions.OverridingFunctionException;
import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.SameNameException;
import com.duy.pascal.backend.exceptions.UnconvertibleTypeException;
import com.duy.pascal.backend.exceptions.UnrecognizedTokenException;
import com.duy.pascal.backend.lib.ConversionLib;
import com.duy.pascal.backend.lib.CrtLib;
import com.duy.pascal.backend.lib.DosLib;
import com.duy.pascal.backend.lib.graph.GraphLib;
import com.duy.pascal.backend.lib.LibraryUtils;
import com.duy.pascal.backend.lib.MathLib;
import com.duy.pascal.backend.lib.StringLib;
import com.duy.pascal.backend.lib.SystemLib;
import com.duy.pascal.backend.lib.file.FileLib;
import com.duy.pascal.backend.lib.io.IOLib;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.SystemConstants;
import com.duy.pascal.backend.tokens.CommentToken;
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
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.ExecuteActivity;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.ConstantDefinition;
import com.js.interpreter.ast.FunctionDeclaration;
import com.js.interpreter.ast.MethodDeclaration;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.ConstantAccess;
import com.js.interpreter.ast.returnsvalue.FunctionCall;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;
import com.js.interpreter.ast.returnsvalue.VariableAccess;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExpressionContextMixin extends HeirarchicalExpressionContext {
    public static final String TAG = ExpressionContextMixin.class.getSimpleName();
    /**
     * list global variable
     */
    public List<VariableDeclaration> variables = new ArrayList<>();
    /**
     * activity target, uses for input and output
     */
    private ExecuteActivity executeActivity;
    /**
     * list function and procedure pascal
     */
    private ListMultimap<String, AbstractFunction> callableFunctions = ArrayListMultimap.create();
    /**
     * list defined constant
     */
    private Map<String, ConstantDefinition> constants = new HashMap<>();
    /**
     * list custom type
     */
    private Map<String, DeclaredType> typedefs = new HashMap<>();

    /**
     * list library
     */
//    private ArrayList<String> libraries = new ArrayList<>();
    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent) {
        super(root, parent);
    }

    public ExpressionContextMixin(CodeUnit root, ExpressionContext parent,
                                  ListMultimap<String, AbstractFunction> callableFunctions,
                                  ExecuteActivity executeActivity) {
        super(root, parent);
//        this.callableFunctions = callableFunctions;
        if (callableFunctions != null)
            this.callableFunctions.putAll(callableFunctions);

        if (executeActivity != null)
            this.executeActivity = executeActivity;

        //load system function
        loadSystemLibrary();
        //add constants
        SystemConstants.addSystemConstant(this);
        SystemConstants.addSystemType(this);
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

    public List<VariableDeclaration> getVariables() {
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
        return f;
    }

    @Override
    public ReturnsValue getIdentifierValue(WordToken name)
            throws ParsingException {
        if (functionExistsLocal(name.name)) {
            return FunctionCall.generateFunctionCall(name,
                    new ArrayList<ReturnsValue>(0), this);
        } else if (getConstantDefinitionLocal(name.name) != null) {
            return new ConstantAccess(getConstantDefinition(name.name)
                    .getValue(), name.lineInfo);
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
            declaration.parseFunctionBody(i);
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
            addConstDeclarations(i);
        } else if (next instanceof UsesToken) {
            i.take();
            ArrayList<String> listLib = new ArrayList<>();
            int count = 0;
            do {
                next = i.take();
                if (!(next instanceof WordToken)) {
                    throw new ExpectedTokenException("[Library Identifier]", next);
                }
                //check library not found
                if (!LibraryUtils.SUPPORT_LIB.contains(((WordToken) next).name)) {
                    throw new LibraryNotFoundException(((WordToken) next).name, next);
                }
                listLib.add(next.toString());
                next = i.peek();
                if (next instanceof SemicolonToken) {
                    break;
                } else {
                    i.assert_next_comma();
                }
                count++;
            } while (true);
            i.assertNextSemicolon();
            loadLibrary(listLib);
        } else if (next instanceof TypeToken) {
            i.take();
            while (i.peek() instanceof WordToken) {
                String name = i.next_word_value();
                next = i.take();
                if (!(next instanceof OperatorToken && ((OperatorToken) next).type == OperatorTypes.EQUALS)) {
                    throw new ExpectedTokenException("=", next);
                }
                typedefs.put(name, i.getNextPascalType(this));
                i.assertNextSemicolon();
            }
        } else if (next instanceof CommentToken) {
            i.take();
            //fix bug when comment in the top of the file
            addConstDeclarations(i);
        } else {
            handleUnrecognizedDeclaration(i.take(), i);
        }
    }

    /**
     * load system method  of some class
     */
    protected void loadSystemLibrary() {
        ArrayList<Class> classes = new ArrayList<>();
        /**
         * Important: load file library before io lib. Because
         * method readln(file, ...) in {@link FileLib} will be override method readln(object...) in {@link IOLib}
         */
        classes.add(FileLib.class);
        classes.add(IOLib.class);
        classes.add(StringLib.class);
        classes.add(ConversionLib.class);
        classes.add(SystemLib.class);

        for (Class pascalPlugin : classes) {
            Object o;
            try {
                Constructor constructor = pascalPlugin.getConstructor(ExecuteActivity.class);
                o = constructor.newInstance(executeActivity);
            } catch (NoSuchMethodException
                    | IllegalArgumentException |
                    IllegalAccessException | InvocationTargetException |
                    InstantiationException e) {
                o = null;
                try {
                    Constructor constructor = pascalPlugin.getConstructor();
                    o = constructor.newInstance();
                } catch (NoSuchMethodException
                        | IllegalArgumentException |
                        IllegalAccessException | InvocationTargetException |
                        InstantiationException e1) {
                    e1.printStackTrace();
                }
            }

            for (Method m : pascalPlugin.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers())) {
                    MethodDeclaration tmp = new MethodDeclaration(o, m);
                    callableFunctions.put(tmp.name().toLowerCase(), tmp);
                    if (executeActivity != null) Log.d(TAG, "loadSystemLibrary: " + m.getName());
                }
            }

        }
    }

    protected void loadLibrary(ArrayList<String> libraries) {
        ArrayList<Class> classes = new ArrayList<>();
        for (String name : libraries) {
            if (name.equalsIgnoreCase("crt")) {
                classes.add(CrtLib.class);
            } else if (name.equalsIgnoreCase("dos")) {
                classes.add(DosLib.class);
            } else if (name.equalsIgnoreCase("math")) {
                classes.add(MathLib.class);
            } else if (name.equalsIgnoreCase("graph")) {
                classes.add(GraphLib.class);
            }
        }

        for (Class pascalPlugin : classes) {
            Object o;
            try {
                Constructor constructor = pascalPlugin.getConstructor(ExecuteActivity.class);
                o = constructor.newInstance(executeActivity);
            } catch (NoSuchMethodException
                    | IllegalArgumentException |
                    IllegalAccessException | InvocationTargetException |
                    InstantiationException e) {
                o = null;
                try {
                    Constructor constructor = pascalPlugin.getConstructor();
                    o = constructor.newInstance();
                } catch (NoSuchMethodException
                        | IllegalArgumentException |
                        IllegalAccessException | InvocationTargetException |
                        InstantiationException e1) {
                    e1.printStackTrace();
                }
            }

            for (Method m : pascalPlugin.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers())) {
                    MethodDeclaration tmp = new MethodDeclaration(o, m);
                    callableFunctions.put(tmp.name().toLowerCase(), tmp);
                    Log.d(TAG, "loadSystemLibrary: " + m.getName());
                }
            }

        }
    }

    protected abstract void handleBeginEnd(GrouperToken i) throws ParsingException;

    public VariableDeclaration getVariableDefinitionLocal(String ident) {
        for (VariableDeclaration v : variables) {
            if (v.name.equals(ident)) {
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
    }

    public void declareVariable(VariableDeclaration v) {
        variables.add(v);
    }

    public void declareFunction(FunctionDeclaration f) {
        callableFunctions.put(f.name, f);
    }

    public void declareConst(ConstantDefinition c) {
        constants.put(c.name(), c);
    }

    public void addConstDeclarations(GrouperToken i) throws ParsingException {
        Token next;
        while (i.peek() instanceof WordToken) {
            WordToken constName = (WordToken) i.take(); //const a : integer = 2; const a = 2;
            next = i.take();
            if (next instanceof ColonToken) {
                DeclaredType type = i.getNextPascalType(this);
                Object defaultValue = null;

                if (i.peek() instanceof OperatorToken) {
                    if (((OperatorToken) i.peek()).type == OperatorTypes.EQUALS) {
                        i.take(); //ignore equal name
                        ReturnsValue unconverted = i.getNextExpression(this);
                        ReturnsValue converted = type.convert(unconverted, this);
                        if (converted == null) {
                            throw new UnconvertibleTypeException(unconverted,
                                    unconverted.getType(this).declaredType, type,
                                    true);
                        }
                        defaultValue = converted.compileTimeValue(this);
                        if (defaultValue == null) {
                            throw new NonConstantExpressionException(converted);
                        }

                        ConstantDefinition constantDefinition = new ConstantDefinition(constName.name,
                                type, defaultValue, constName.lineInfo);
                        this.constants.put(constantDefinition.name(), constantDefinition);
                        i.assertNextSemicolon();
                    }
                } else {
                    // TODO: 08-Apr-17
                }
            } else if (next instanceof OperatorToken) {
                if (((OperatorToken) next).type != OperatorTypes.EQUALS) {
                    throw new ExpectedTokenException("=", constName);
                }
                ReturnsValue value = i.getNextExpression(this);
                Object compileVal = value.compileTimeValue(this);
                if (compileVal == null) {
                    throw new NonConstantExpressionException(value);
                }
                ConstantDefinition constantDefinition = new ConstantDefinition(constName.name,
                        compileVal, constName.lineInfo);
                this.constants.put(constantDefinition.name(), constantDefinition);
                i.assertNextSemicolon();
            } else {
                throw new ExpectedTokenException("=", constName);
            }
        }

//        while (i.peek() instanceof WordToken) {
//            WordToken constName = (WordToken) i.take();
//            Token equals = i.take();
//            if (!(equals instanceof OperatorToken)
//                    || ((OperatorToken) equals).type != OperatorTypes.EQUALS) {
//                throw new ExpectedTokenException("=", constName);
//            }
//            ReturnsValue value = i.getNextExpression(this);
//            Object compileVal = value.compileTimeValue(this);
//            if (compileVal == null) {
//                throw new NonConstantExpressionException(value);
//            }
//
//            ConstantDefinition constantDefinition = new ConstantDefinition(constName.name,
//                    compileVal, constName.lineInfo);
//            verifyNonConflictingSymbol(constantDefinition);
//            this.constants.put(constName.name, constantDefinition);
//            i.assertNextSemicolon();
//        }
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
        return result;
    }


}
