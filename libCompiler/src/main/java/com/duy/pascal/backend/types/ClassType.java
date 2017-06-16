package com.duy.pascal.backend.types;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.FunctionDeclaration;
import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContextMixin;
import com.duy.pascal.backend.ast.instructions.Executable;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.UnrecognizedTokenException;
import com.duy.pascal.backend.tokens.Token;
import com.duy.pascal.backend.tokens.grouping.GrouperToken;
import com.duy.pascal.frontend.activities.IRunnablePascal;

import java.util.ArrayList;
import java.util.List;

public class ClassType extends CustomType {

    private FunctionDeclaration constructor, destructor;
    private List<FunctionDeclaration> privateFunctions = new ArrayList<>();
    private List<FunctionDeclaration> publicFunctions = new ArrayList<>();

    private List<VariableDeclaration> publicVars = new ArrayList<>();
    private List<VariableDeclaration> privateVars = new ArrayList<>();


    public ClassType() {

    }

    public FunctionDeclaration getConstructor() {
        return constructor;
    }

    public void setConstructor(FunctionDeclaration constructor) {
        this.constructor = constructor;
    }

    public FunctionDeclaration getDestructor() {
        return destructor;
    }

    public void setDestructor(FunctionDeclaration destructor) {
        this.destructor = destructor;
    }

    public void addPrivateFunction(FunctionDeclaration functionDeclaration) {

    }

    public void addPublicFunction(FunctionDeclaration functionDeclaration) {

    }

    @Override
    public String getEntityType() {
        return "class";
    }


    public void addPrivateField(ArrayList<VariableDeclaration> vars) {
    }

    public void addPublicFields(List<VariableDeclaration> vars) {
    }

    public FunctionDeclaration getExistFunction(FunctionDeclaration g) throws ParsingException {
        for (FunctionDeclaration f : publicFunctions) {
            if (f.headerMatches(g)) {
                return f;
            }
        }
        return g;
    }



}
