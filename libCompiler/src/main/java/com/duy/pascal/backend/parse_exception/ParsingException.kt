package com.duy.pascal.backend.parse_exception


import com.duy.pascal.backend.linenumber.LineInfo

/**
 * Exception when parse syntax of the program
 */
open class ParsingException : Exception {

    /**
     * The lineInfo of lineInfo error
     */
    var lineInfo: LineInfo? = null

    constructor(lineInfo: LineInfo?, message: String) : super(lineInfo?.toString() + " " + message) {
        this.lineInfo = lineInfo
    }

    constructor(lineInfo: LineInfo?) : super() {
        this.lineInfo = lineInfo
    }

    override fun toString(): String {
        return lineInfo.toString() + ":" + message
    }

    open val isAutoFix: Boolean
        get() = false
}
