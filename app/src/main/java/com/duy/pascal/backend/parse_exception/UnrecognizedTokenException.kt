package com.duy.pascal.backend.parse_exception

import com.duy.pascal.backend.tokens.Token

class UnrecognizedTokenException(var token: Token)
    : ParsingException(token.lineNumber, "The following name doesn't belong here: $token") {

}
