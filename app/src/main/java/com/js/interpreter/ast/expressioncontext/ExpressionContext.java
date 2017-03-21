package com.js.interpreter.ast.expressioncontext;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.SameNameException;
import com.duy.interpreter.tokens.Token;
import com.duy.interpreter.tokens.WordToken;
import com.duy.interpreter.tokens.grouping.GrouperToken;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.returnsvalue.ReturnsValue;

import java.util.List;

public interface ExpressionContext extends CompileTimeContext {
    public ReturnsValue getIdentifierValue(WordToken name)
            throws ParsingException;

    void verifyNonConflictingSymbol(NamedEntity n) throws SameNameException;

    public VariableDeclaration getVariableDefinition(String ident);

    public void getCallableFunctions(String name,
                                     List<List<AbstractFunction>> listsofar);

    public boolean functionExists(String name);

    public CodeUnit root();

    public abstract Executable handleUnrecognizedStatement(Token next,
                                                           GrouperToken container) throws ParsingException;

    public abstract boolean handleUnrecognizedDeclaration(Token next,
                                                          GrouperToken container) throws ParsingException;

}
