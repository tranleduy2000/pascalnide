package com.duy.pascal.backend.exceptions.grouping;

import com.duy.pascal.backend.linenumber.LineInfo;

public class StrayCharacterException extends com.duy.pascal.backend.exceptions.grouping.GroupingException {

    public StrayCharacterException(LineInfo line, char character) {
        super(line, "Stray character in program: " + character
                + "\nChar code " + (int) character);
    }

}
