package com.duy.pascal.backend.tokens.value;


import com.duy.pascal.backend.linenumber.LineInfo;

public class CharacterToken extends ValueToken {
    private char aChar;
    private String nonParse;
    private boolean isRaw = false;

    public CharacterToken(LineInfo line, char character) {
        super(line);
        this.aChar = character;
        this.nonParse = "#" + ((int) character);
    }

    public CharacterToken(LineInfo line, String nonParse) {
        super(line);
        try {
            String number = nonParse.substring(1, nonParse.length());
            int value = Integer.parseInt(number);
            this.aChar = (char) value;
        } catch (Exception e) {
            aChar = '?';
        }
        this.isRaw = true;
        this.nonParse = nonParse;
    }

    @Override
    public String toCode() {
        if (!isRaw) {
            return "\'" + Character.toString(aChar) + "\'";
        } else {
            return nonParse;
        }
    }

    @Override
    public Object getValue() {
        return aChar;
    }

}
