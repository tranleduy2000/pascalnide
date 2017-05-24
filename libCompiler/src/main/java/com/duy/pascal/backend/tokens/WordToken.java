package com.duy.pascal.backend.tokens;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.define.UnrecognizedTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.duy.pascal.backend.pascaltypes.PointerType;
import com.js.interpreter.expressioncontext.ExpressionContext;


public class WordToken extends Token {

    //always lower case
    public String name;

    private String originalName;

    public WordToken(LineInfo line, String s) {
        super(line);
        this.name = s.toLowerCase();
        this.originalName = s;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return originalName;
    }

    @NonNull
    @Override
    public WordToken getWordValue() throws ParsingException {
        return this;
    }

    public DeclaredType toBasicType(ExpressionContext context)
            throws UnrecognizedTypeException {
        DeclaredType returnType = null;
        String name = this.name.toLowerCase().intern();
        if (name.equalsIgnoreCase("integer")
                || name.equalsIgnoreCase("byte")
                || name.equalsIgnoreCase("word")
                || name.equalsIgnoreCase("shortint")
                || name.equalsIgnoreCase("smallint")
                || name.equalsIgnoreCase("cardinal")) {
            returnType = BasicType.Integer;
        } else if (name.equalsIgnoreCase("string")
                || name.equalsIgnoreCase("ansistring")
                || name.equalsIgnoreCase("shortstring")) {
            returnType = BasicType.StringBuilder;
        } else if (name.equalsIgnoreCase("single")
                || name.equalsIgnoreCase("extended")
                || name.equalsIgnoreCase("real")
                || name.equalsIgnoreCase("comp")
                || name.equalsIgnoreCase("curreny")
                || name.equalsIgnoreCase("double")) {
            returnType = BasicType.Double;
        } else if (name.equalsIgnoreCase("longint")
                || name.equalsIgnoreCase("int64")
                || name.equalsIgnoreCase("qword")
                || name.equalsIgnoreCase("longword")
                || name.equalsIgnoreCase("dword")) {
            returnType = BasicType.Long;
        } else if (name.equalsIgnoreCase("boolean")) {
            returnType = BasicType.Boolean;
        } else if (name.equalsIgnoreCase("char")) {
            returnType = BasicType.Character;
        } else if (name.equalsIgnoreCase("text")
                || name.equalsIgnoreCase("textfile")) {
            returnType = BasicType.Text;
        } else if (name.equalsIgnoreCase("pointer")) {
            returnType = new PointerType(BasicType.create(Object.class));
        } else {
            DeclaredType type = context.getTypedefType(name);
            if (type != null) {
                returnType = type;
            } else {
                try {
                    String clone = originalName.replace("_", ".");
                    Class clazz = Class.forName(clone);
                    returnType = new JavaClassBasedType(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (returnType == null) {
                    Object constVal = context.getConstantDefinition(name);
                    if (constVal == null) {
                        throw new UnrecognizedTypeException(lineNumber, name);
                    }
                    returnType = BasicType.create(constVal.getClass());
                }
            }
        }
        return returnType;
    }


}
