package com.duy.pascal.backend.parse_exception.grouping

import com.duy.pascal.backend.parse_exception.ParsingException
import com.duy.pascal.backend.linenumber.LineInfo


open class GroupingException : ParsingException {

    constructor(line: LineInfo, message: String) : super(line, message) {}

    constructor(line: LineInfo) : super(line) {}
}
