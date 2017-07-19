package com.duy.pascal.interperter.parse_exception.grouping

import com.duy.pascal.interperter.linenumber.LineInfo
import com.duy.pascal.interperter.parse_exception.ParsingException


open class GroupingException : ParsingException {

    constructor(line: LineInfo?, message: String) : super(line, message) {}

    constructor(line: LineInfo?) : super(line) {}

    var exceptionTypes: Type? = null

    constructor(line: LineInfo?, exceptionTypes: Type) : super(line) {
        this.exceptionTypes = exceptionTypes
    }

    var caused: Exception? = null

    override val message: String?
        get() = exceptionTypes?.message +
                if (caused == null) "" else ": " + caused!!.message


    enum class Type(var message: String) {
        MISMATCHED_PARENTHESES("Mismatched parentheses"),
        MISMATCHED_BRACKETS("Mismatched brackets"),
        MISMATCHED_BEGIN_END("Mismatched begin - end construct"),
        UNFINISHED_BEGIN_END("Unfinished begin - end construct"),
        UNFINISHED_PARENTHESES("You forgot to close your parentheses"),
        UNFINISHED_BRACKETS("You forgot to close your brackets"),
        EXTRA_END("You have an extra 'end' in your program"),
        UNFINISHED_CONSTRUCT("You forgot to complete the structure you started here"),
        IO_EXCEPTION("IOException occurred while reading the input"),
        INCOMPLETE_CHAR("Incomplete character literal"),
        MISSING_INCLUDE("Missing file to include"),
        NEWLINE_IN_QUOTES("You must close your quotes before starting a new lineInfo");
    }

    override val isAutoFix: Boolean
        get() {
            if (exceptionTypes == Type.UNFINISHED_BEGIN_END) {
                return true;
            }
            return false;
        }
}
