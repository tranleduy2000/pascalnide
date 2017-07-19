package com.duy.pascal.interperter.parse_exception.grouping

import com.duy.pascal.interperter.linenumber.LineInfo

class StrayCharacterException : GroupingException {
    var charCode: Char

    constructor(line: LineInfo, charCode: Char) : super(line,
            "Stray character in program: $charCode\nChar code ${charCode.toInt()}") {
        this.charCode = charCode
    }
}

