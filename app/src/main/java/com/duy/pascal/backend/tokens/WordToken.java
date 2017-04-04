package com.duy.pascal.backend.tokens;

import com.duy.pascal.backend.exceptions.ParsingException;
import com.duy.pascal.backend.exceptions.UnrecognizedTypeException;
import com.duy.pascal.backend.linenumber.LineInfo;
import com.duy.pascal.backend.pascaltypes.BasicType;
import com.duy.pascal.backend.pascaltypes.DeclaredType;
import com.js.interpreter.ast.expressioncontext.ExpressionContext;


public class WordToken extends Token {
    public String name;

    public WordToken(LineInfo line, String s) {
        super(line);
        this.name = s;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public WordToken get_word_value() throws ParsingException {
        return this;
    }

    public DeclaredType toBasicType(ExpressionContext context)
            throws UnrecognizedTypeException {
        String s = name.toLowerCase().intern();
        if (s.equals("integer") || s.equals("byte") || s.equals("word")
                || s.equals("shortint") || s.equals("smallint")) {
            return BasicType.Integer;
        } else if (s.equals("string") || s.equals("ansistring")) {
            return BasicType.StringBuilder;
        } else if (s.equals("single") || s.equals("extended") || s == "real" || s.equals("comp")
                || s.equals("curreny")) {
            return BasicType.Double;
        } else if (s == "longint" || s.equals("int64") || s.equals("qword")
                || s.equals("longword") || s.equals("dword")) {
            return BasicType.Long;
        } else if (s == "boolean") {
            return BasicType.Boolean;
        } else if (s == "char") {
            return BasicType.Character;
        } else {
            DeclaredType type = context.getTypedefType(s);
            if (type != null) {
                return type;
            } else {
                Object constval = context.getConstantDefinition(s);
                if (constval == null) {
                    throw new UnrecognizedTypeException(lineInfo, s);
                }
                return BasicType.anew(constval.getClass());
            }
        }
    }
}
