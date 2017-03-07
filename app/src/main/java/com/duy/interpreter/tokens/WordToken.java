package com.duy.interpreter.tokens;

import com.duy.interpreter.exceptions.ParsingException;
import com.duy.interpreter.exceptions.UnrecognizedTypeException;
import com.duy.interpreter.linenumber.LineInfo;
import com.duy.interpreter.pascaltypes.BasicType;
import com.duy.interpreter.pascaltypes.DeclaredType;
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

    public DeclaredType to_basic_type(ExpressionContext context)
            throws UnrecognizedTypeException {
        String s = name.toLowerCase().intern();
        if (s.equals("integer") || s.equals("byte") || s.equals("word")
                || s.equals("shortint") || s.equals("smallint")) {
            return BasicType.Integer;
        }
        if (s == "string") {
            return BasicType.StringBuilder;
        }
        if (s == "single" || s == "extended" || s == "real" || s.equals("comp")
                || s.equals("curreny")) {
            return BasicType.Double;
        }
        if (s == "longint" || s.equals("int64") || s.equals("qword")
                || s.equals("longword") || s.equals("dword")) {
            return BasicType.Long;
        }
        if (s == "boolean") {
            return BasicType.Boolean;
        }
        if (s == "char") {
            return BasicType.Character;
        }
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
