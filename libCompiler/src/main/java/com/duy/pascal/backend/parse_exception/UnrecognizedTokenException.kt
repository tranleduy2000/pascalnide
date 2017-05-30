package com.duy.pascal.backend.parse_exception

import com.duy.pascal.backend.tokens.Token

class UnrecognizedTokenException : ParsingException {
    var token: Token

    constructor(token: Token) : super(token.lineNumber, "The following name doesn't belong here: $token") {
        this.token = token
    }
}
