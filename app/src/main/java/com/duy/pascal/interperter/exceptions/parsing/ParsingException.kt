package com.duy.pascal.interperter.exceptions.parsing

import com.duy.pascal.interperter.linenumber.LineInfo


open class ParsingException : Exception {
    var lineInfo: LineInfo? = null

    constructor(lineInfo: LineInfo?, message: String) : super(message) {
        this.lineInfo = lineInfo
    }

    constructor(lineInfo: LineInfo?) {
        this.lineInfo = lineInfo
    }

    override fun toString(): String {
        return this.lineInfo.toString() + ":" + this.message
    }

    open val canAutoFix
        get() = false
}
