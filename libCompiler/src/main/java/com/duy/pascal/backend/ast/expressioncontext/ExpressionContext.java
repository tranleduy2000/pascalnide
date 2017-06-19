package com.duy.pascal.backend.ast.expressioncontext;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.declaration.lang.function.AbstractFunction;
import com.duy.pascal.backend.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.backend.declaration.LabelDeclaration;
import com.duy.pascal.backend.declaration.NamedEntity;
import com.duy.pascal.backend.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.backend.ast.codeunit.CodeUnit;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.WordToken;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.backend.declaration.lang.types.Type;

import java.util.List;


public interface ExpressionContext extends CompileTimeContext {

    @NonNull
    LineInfo getStartLine();

    RuntimeValue getIdentifierValue(WordToken name)
            throws ParsingException;

    void verifyNonConflictingSymbol(NamedEntity n) throws DuplicateIdentifierException;

    VariableDeclaration getVariableDefinition(String ident);

    void getCallableFunctions(String name, List<List<AbstractFunction>> listsofar);

    boolean functionExists(String name);

    void declareConst(ConstantDefinition c);

    ConstantDefinition getConstantDefinitionLocal(String indent);

    Type getTypedefTypeLocal(String ident);

    VariableDeclaration getVariableDefinitionLocal(String ident);

    List<AbstractFunction> getCallableFunctionsLocal(String name);

    boolean functionExistsLocal(String name);

    public LabelDeclaration getLabelLocal(String name);

    CodeUnit root();

    Executable handleUnrecognizedStatement(Token next, GrouperToken container)
            throws ParsingException;

    boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
            throws ParsingException;

}
