package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;

public class CharacterToken extends ValueToken {
    char c;

    public CharacterToken(LineInfo line, char character) {
        super(line);
        this.c = character;
    }

    @Override
    public String toCode() {
        return "\'" + Character.toString(c) + "\'";
    }

    @Override
    public Object getValue() {
        return c;
    }

}
