package com.duy.pascal.backend.exceptions

import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.tokens.Token

/**
 * Created by Duy on 16-Apr-17.
 */

class UnSupportTokenException(line: LineInfo, token: Token) : ParsingException(line) {
    var token: Token? = null

    override val message: String?
        get() = "Unsupported token " + token!!
}
