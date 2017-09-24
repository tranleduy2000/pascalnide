package com.duy.pascal.interperter.ast.expressioncontext;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.codeunit.CodeUnit;
import com.duy.pascal.interperter.ast.instructions.Executable;
import com.duy.pascal.interperter.ast.runtime_value.value.RuntimeValue;
import com.duy.pascal.interperter.declaration.LabelDeclaration;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.function.AbstractFunction;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.declaration.lang.value.ConstantDefinition;
import com.duy.pascal.interperter.declaration.lang.value.VariableDeclaration;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.define.DuplicateIdentifierException;
import com.duy.pascal.interperter.linenumber.LineInfo;
import com.duy.pascal.interperter.tokens.Token;
import com.duy.pascal.interperter.tokens.WordToken;
import com.duy.pascal.interperter.tokens.grouping.GrouperToken;

import java.util.List;


public interface ExpressionContext extends CompileTimeContext {

    @NonNull
    LineInfo getStartPosition();

    RuntimeValue getIdentifierValue(WordToken name)
           throws Exception;

    void verifyNonConflictingSymbol(NamedEntity n) throws DuplicateIdentifierException;

    VariableDeclaration getVariableDefinition(Name ident);

    void getCallableFunctions(Name name, List<List<AbstractFunction>> listsofar);

    boolean functionExists(Name name);

    void declareConst(ConstantDefinition c);

    ConstantDefinition getConstantDefinitionLocal(Name indent);

    Type getTypedefTypeLocal(Name ident);

    VariableDeclaration getVariableDefinitionLocal(Name ident);

    List<AbstractFunction> getCallableFunctionsLocal(Name name);

    boolean functionExistsLocal(Name name);

    public LabelDeclaration getLabelLocal(Name name);

    CodeUnit root();

    Executable handleUnrecognizedStatement(Token next, GrouperToken container)
           throws Exception;

    boolean handleUnrecognizedDeclaration(Token next, GrouperToken container)
           throws Exception;

    public <T> T getListener(Class<T> c);
}
