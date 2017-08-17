package com.duy.pascal.interperter.tokens;

import android.support.annotation.NonNull;

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext;
import com.duy.pascal.interperter.declaration.Name;
import com.duy.pascal.interperter.declaration.NamedEntity;
import com.duy.pascal.interperter.declaration.lang.types.BasicType;
import com.duy.pascal.interperter.declaration.lang.types.JavaClassBasedType;
import com.duy.pascal.interperter.declaration.lang.types.PointerType;
import com.duy.pascal.interperter.declaration.lang.types.Type;
import com.duy.pascal.interperter.exceptions.parsing.ParsingException;
import com.duy.pascal.interperter.exceptions.parsing.define.TypeIdentifierExpectException;
import com.duy.pascal.interperter.linenumber.LineInfo;


public class WordToken extends Token implements NamedEntity {

    public Name name;

    private String originalName;

    public WordToken(LineInfo line, String s) {
        super(line);
        this.name = Name.create(s);
        this.originalName = s;
        this.line.setLength(name.getLength());
    }

    @NonNull
    @Override
    public String getEntityType() {
        return "name";
    }

    @NonNull
    @Override
    public Name getName() {
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
        return name.toString();
    }

    public String getCode() {
        return originalName;
    }

    @NonNull
    @Override
    public WordToken getWordValue() throws Exception {
        return this;
    }

    public Type toBasicType(ExpressionContext context)
            throws TypeIdentifierExpectException {
        Type returnType = null;
        if (name.equals("integer")
                || name.equals("byte")
                || name.equals("word")
                || name.equals("shortint")
                || name.equals("smallint")
                || name.equals("cardinal")) {
            returnType = BasicType.Integer;
        } else if (name.equals("string")
                || name.equals("ansistring")
                || name.equals("shortstring")
                || name.equals("UnicodeString")
                || name.equals("OpenString")
                || name.equals("WideString")) {
            returnType = BasicType.StringBuilder;
        } else if (name.equals("single")
                || name.equals("extended")
                || name.equals("real")
                || name.equals("comp")
                || name.equals("curreny")
                || name.equals("double")) {
            returnType = BasicType.Double;
        } else if (name.equals("longint")
                || name.equals("int64")
                || name.equals("qword")
                || name.equals("longword")
                || name.equals("dword")
                || name.equals("uint64")) {
            returnType = BasicType.Long;
        } else if (name.equals("boolean")
                || name.equals("boolean16")
                || name.equals("boolean32")
                || name.equals("boolean64")
                || name.equals("ByteBool")
                || name.equals("WordBool")
                || name.equals("LongBool")
                ) {
            returnType = BasicType.Boolean;
        } else if (name.equals("char")
                || name.equals("AnsiChar")
                || name.equals("char")) {
            returnType = BasicType.Character;
        } else if (name.equals("text")
                || name.equals("textfile")) {
            returnType = BasicType.Text;
        } else if (name.equals("pointer")) {
            returnType = new PointerType(BasicType.create(Object.class));
        } else if (name.equals("pchar")
                || name.equals("PAnsiChar")) {
            returnType = new PointerType(BasicType.create(Character.class));
        } else if (name.equals("PShortString")) {
            returnType = new PointerType(BasicType.create(StringBuilder.class));
        } else {
            Type type = context.getTypeDef(this.name);
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
                    Object constVal = context.getConstantDefinition(this.name);
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
