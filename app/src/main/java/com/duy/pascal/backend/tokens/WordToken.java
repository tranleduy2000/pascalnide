package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;


public class WordToken extends Token {
    public String name;
    public String orginalName;

    public WordToken(LineInfo line, String s) {
        super(line);
        this.name = s.toLowerCase();
        this.orginalName = s;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getCode() {
        return orginalName;
    }

    @Override
    public WordToken getWordValue() throws ParsingException {
        return this;
    }

    public DeclaredType toBasicType(ExpressionContext context)
            throws UnrecognizedTypeException {
        String s = name.toLowerCase().intern();
        if (s.equalsIgnoreCase("integer")
                || s.equalsIgnoreCase("byte")
                || s.equalsIgnoreCase("word")
                || s.equalsIgnoreCase("shortint")
                || s.equalsIgnoreCase("smallint")
                || s.equalsIgnoreCase("cardinal")) {
            return BasicType.Integer;
        } else if (s.equalsIgnoreCase("string")
                || s.equalsIgnoreCase("ansistring")) {
            return BasicType.StringBuilder;
        } else if (s.equalsIgnoreCase("single")
                || s.equalsIgnoreCase("extended")
                || s.equalsIgnoreCase("real")
                || s.equalsIgnoreCase("comp")
                || s.equalsIgnoreCase("curreny")) {
            return BasicType.Double;
        } else if (s.equalsIgnoreCase("longint")
                || s.equalsIgnoreCase("int64")
                || s.equalsIgnoreCase("qword")
                || s.equalsIgnoreCase("longword")
                || s.equalsIgnoreCase("dword")) {
            return BasicType.Long;
        } else if (s.equalsIgnoreCase("boolean")) {
            return BasicType.Boolean;
        } else if (s.equalsIgnoreCase("char")) {
            return BasicType.Character;
        } else if (s.equalsIgnoreCase("text")) {
            return BasicType.Text;
        } else {
            DeclaredType type = context.getTypedefType(s);
            if (type != null) {
                return type;
            } else {
                Object constVal = context.getConstantDefinition(s);
                if (constVal == null) {
                    throw new UnrecognizedTypeException(lineInfo, s);
                }
                return BasicType.anew(constVal.getClass());
            }
        }
    }
}
