package com.duy.pascal.backend.tokens.value


import com.duy.pascal.backend.linenumber.LineInfo

class CharacterToken : ValueToken {
    private var aChar: Char = ' '
    private var origin: String
    private var isRaw = false

    constructor(line: LineInfo?, character: Char) : super(line) {
        this.aChar = character
        this.origin = "#" + character.toInt()

        line?.length = toCode().length
    }

    constructor(line: LineInfo, nonParse: String) : super(line) {
        try {
            val number = nonParse.substring(1, nonParse.length)
            val value = Integer.parseInt(number)
            this.aChar = value.toChar()
        } catch (e: Exception) {
            aChar = '?'
        }

        this.isRaw = true
        this.origin = nonParse
    }

    override fun toCode(): String {
        if (!isRaw) {
            return "\'" + Character.toString(aChar) + "\'"
        } else {
            return origin
        }
    }

    override fun toString(): String {
        return toCode()
    }

    override fun getValue(): Any {
        return aChar
    }

}
