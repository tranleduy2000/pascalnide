package com.js.interpreter.ast.expressioncontext;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.js.interpreter.ast.AbstractFunction;
import com.js.interpreter.ast.NamedEntity;
import com.js.interpreter.ast.VariableDeclaration;
import com.js.interpreter.ast.codeunit.CodeUnit;
import com.js.interpreter.ast.instructions.Executable;
import com.js.interpreter.ast.runtime_value.RuntimeValue;

import java.util.List;

public interface ExpressionContext extends CompileTimeContext {
    RuntimeValue getIdentifierValue(WordToken name)
            throws ParsingException;

    void verifyNonConflictingSymbol(NamedEntity n) throws SameNameException;

    VariableDeclaration getVariableDefinition(String ident);

    void getCallableFunctions(String name,
                              List<List<AbstractFunction>> listsofar);

    boolean functionExists(String name);

    CodeUnit root();

    Executable handleUnrecognizedStatement(Token next, GrouperToken container)
            throws ParsingException;

    boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
            throws ParsingException;

}
