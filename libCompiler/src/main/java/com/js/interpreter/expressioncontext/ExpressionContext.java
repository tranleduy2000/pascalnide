package com.js.interpreter.expressioncontext;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.SameNameException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.function_declaretion.AbstractFunction;
import com.js.interpreter.NamedEntity;
import com.js.interpreter.VariableDeclaration;
import com.js.interpreter.codeunit.CodeUnit;
import com.js.interpreter.instructions.Executable;
import com.js.interpreter.runtime_value.RuntimeValue;

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
