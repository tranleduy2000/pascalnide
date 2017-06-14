package com.duy.pascal.backend.parse_exception

import com.duy.pascal.backend.tokens.Token

/**
 * Created by Duy on 16-Apr-17.
 */

class UnSupportTokenException(var token: Token) : ParsingException(token.lineNumber) {

    override val message: String?
        get() = "Unsupported token " + token
}
