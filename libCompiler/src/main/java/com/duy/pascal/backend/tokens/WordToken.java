package com.duy.pascal.backend.tokens;

import android.support.annotation.NonNull;

import com.duy.pascal.backend.ast.NamedEntity;
import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.parse_exception.ParsingException;
import com.duy.pascal.backend.parse_exception.define.TypeIdentifierExpectException;
import com.duy.pascal.backend.types.BasicType;
import com.duy.pascal.backend.types.DeclaredType;
import com.duy.pascal.backend.types.JavaClassBasedType;
import com.duy.pascal.backend.types.PointerType;


public class WordToken extends Token implements NamedEntity {

    //always lower case
    public String name;

    private String originalName;

    public WordToken(LineInfo line, String s) {
        super(line);
        this.name = s.toLowerCase();
        this.originalName = s;
        this.line.setLength(name.length());
    }

    @Override
    public String getEntityType() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
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
            throws TypeIdentifierExpectException {
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
                || name.equalsIgnoreCase("shortstring")
                || name.equalsIgnoreCase("UnicodeString")
                || name.equalsIgnoreCase("OpenString")
                || name.equalsIgnoreCase("WideString")) {
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
                || name.equalsIgnoreCase("dword")
                || name.equalsIgnoreCase("uint64")) {
            returnType = BasicType.Long;
        } else if (name.equalsIgnoreCase("boolean")
                || name.equalsIgnoreCase("boolean16")
                || name.equalsIgnoreCase("boolean32")
                || name.equalsIgnoreCase("boolean64")
                || name.equalsIgnoreCase("ByteBool")
                || name.equalsIgnoreCase("WordBool")
                || name.equalsIgnoreCase("LongBool")
                ) {
            returnType = BasicType.Boolean;
        } else if (name.equalsIgnoreCase("char")
                || name.equalsIgnoreCase("AnsiChar")
                || name.equalsIgnoreCase("char")) {
            returnType = BasicType.Character;
        } else if (name.equalsIgnoreCase("text")
                || name.equalsIgnoreCase("textfile")) {
            returnType = BasicType.Text;
        } else if (name.equalsIgnoreCase("pointer")) {
            returnType = new PointerType(BasicType.create(Object.class));
        } else if (name.equalsIgnoreCase("pchar")
                || name.equalsIgnoreCase("PAnsiChar")) {
            returnType = new PointerType(BasicType.create(Character.class));
        } else if (name.equalsIgnoreCase("PShortString")) {
            returnType = new PointerType(BasicType.create(StringBuilder.class));
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
                    System.err.println("Can not find type " + name);
                }
                if (returnType == null) {
                    Object constVal = context.getConstantDefinition(name);
                    if (constVal == null) {
                        throw new TypeIdentifierExpectException(line, name, context);
                    }
                    returnType = BasicType.create(constVal.getClass());
                }
            }
        }
        return returnType;
    }


}
