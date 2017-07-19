package com.duy.pascal.interperter.parse_exception

import com.duy.pascal.interperter.tokens.Token

class UnrecognizedTokenException(var token: Token)
    : ParsingException(token.lineNumber, "The following name doesn't belong here: $token") {

}
