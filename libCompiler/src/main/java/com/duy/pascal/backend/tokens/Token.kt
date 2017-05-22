package com.duy.pascal.backend.tokens


import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.exceptions.syntax.ExpectedTokenException
import com.duy.pascal.backend.linenumber.LineInfo

abstract class Token(var lineInfo: LineInfo) {

    open val wordValue: WordToken
        @Throws(ParsingException::class)
        get() = throw ExpectedTokenException("[Identifier]", this)

    /**
     * Null means not an operator

     * @return
     */
    open val operatorPrecedence: precedence?
        get() = null

    enum class precedence {
        Dereferencing, Negation, Multiplicative, Additive, Relational, NoPrecedence
    }

    /**
     * @return `true` if this token can declare in interface region in unit,
     * * otherwise return `false`
     */

    open fun canDeclareInInterface(): Boolean {
        return false;
    }

}
