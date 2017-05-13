package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.duy.pascal.backend.pascaltypes.JavaClassBasedType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;


public class WordToken extends Token {
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

    @Override
    public WordToken getWordValue() throws ParsingException {
        return this;
    }

    public DeclaredType toBasicType(ExpressionContext context)
            throws UnrecognizedTypeException {
        String name = this.name.toLowerCase().intern();
        if (name.equalsIgnoreCase("integer")
                || name.equalsIgnoreCase("byte")
                || name.equalsIgnoreCase("word")
                || name.equalsIgnoreCase("shortint")
                || name.equalsIgnoreCase("smallint")
                || name.equalsIgnoreCase("cardinal")) {
            return BasicType.Integer;
        } else if (name.equalsIgnoreCase("string")
                || name.equalsIgnoreCase("ansistring")
                || name.equalsIgnoreCase("shortstring")) {
            return BasicType.StringBuilder;
        } else if (name.equalsIgnoreCase("single")
                || name.equalsIgnoreCase("extended")
                || name.equalsIgnoreCase("real")
                || name.equalsIgnoreCase("comp")
                || name.equalsIgnoreCase("curreny")
                || name.equalsIgnoreCase("double")) {
            return BasicType.Double;
        } else if (name.equalsIgnoreCase("longint")
                || name.equalsIgnoreCase("int64")
                || name.equalsIgnoreCase("qword")
                || name.equalsIgnoreCase("longword")
                || name.equalsIgnoreCase("dword")) {
            return BasicType.Long;
        } else if (name.equalsIgnoreCase("boolean")) {
            return BasicType.Boolean;
        } else if (name.equalsIgnoreCase("char")) {
            return BasicType.Character;
        } else if (name.equalsIgnoreCase("text")
                || name.equalsIgnoreCase("textfile")) {
            return BasicType.Text;
        } /*else if (name.equalsIgnoreCase("socket")) {
            return new JavaClassBasedType(Socket.class);
        } */ else {
            DeclaredType type = context.getTypedefType(name);
            if (type != null) {
                return type;
            } else {

                try {
                    String clone = originalName.replace("_", ".");
                    Class clazz = Class.forName(clone);
                    return new JavaClassBasedType(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Object constVal = context.getConstantDefinition(name);
                if (constVal == null) {
                    throw new UnrecognizedTypeException(lineInfo, name);
                }
                return BasicType.create(constVal.getClass());
            }
        }
    }
}
