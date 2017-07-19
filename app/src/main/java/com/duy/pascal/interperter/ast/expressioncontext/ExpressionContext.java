package com.duy.pascal.interperter.ast.expressioncontext;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.LabelDeclaration;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.parse_exception.ParsingException;
import com.duy.pascal.interperter.parse_exception.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;
import com.duy.pascal.interperter.declaration.lang.types.Type;

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
