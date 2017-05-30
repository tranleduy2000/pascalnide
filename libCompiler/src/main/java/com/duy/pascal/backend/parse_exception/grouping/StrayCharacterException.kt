package com.duy.pascal.backend.parse_exception.grouping

import com.duy.pascal.backend.linenumber.LineInfo

class StrayCharacterException : GroupingException {
    var charCode: Char

    constructor(line: LineInfo, charCode: Char) : super(line,
            "Stray character in program: $charCode\nChar code ${charCode.toInt()}") {
        this.charCode = charCode
    }
}

