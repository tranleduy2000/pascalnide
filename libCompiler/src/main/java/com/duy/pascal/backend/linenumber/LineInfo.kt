package com.duy.pascal.backend.linenumber

open class LineInfo {
    /**
     * lineInfo in code
     */
    var line: Int = 0

    var column = 0

    /**
     * length of token
     */
    var length = -1

    /**
     * name of source file
     */
    var sourceFile: String? = null
        private set

    constructor(line: Int, sourceFile: String) {
        this.line = line
        this.sourceFile = sourceFile
    }

    constructor(line: Int, column: Int, sourceFile: String) {
        this.line = line
        this.column = column
        this.sourceFile = sourceFile
    }

    constructor(line: Int, column: Int, length: Int, sourceFile: String) {
        this.line = line
        this.column = column
        this.sourceFile = sourceFile
        this.length = length;
    }

    override fun toString(): String {
        return "Line " + line + (if (column >= 0) ":" + column else "") + " " + sourceFile

    }

}
