package com.duy.interpreter.exceptions.grouping;

import com.duy.interpreter.exceptions.grouping.*;
import com.duy.interpreter.linenumber.LineInfo;

public class StrayCharacterException extends com.duy.interpreter.exceptions.grouping.GroupingException {

    public StrayCharacterException(LineInfo line, char character) {
        super(line, "Stray character in program: <" + character
                + ">, char code " + (int) character);
    }

}
