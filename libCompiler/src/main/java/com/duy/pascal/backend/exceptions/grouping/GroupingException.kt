package com.duy.pascal.backend.exceptions.grouping

import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.linenumber.LineInfo


open class GroupingException : ParsingException {

    constructor(line: LineInfo, message: String) : super(line, message) {}

    constructor(line: LineInfo) : super(line) {}
}
