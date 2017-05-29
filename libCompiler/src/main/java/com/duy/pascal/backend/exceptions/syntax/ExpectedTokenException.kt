/*
 *  Copyright (c) 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.pascal.backend.exceptions.syntax


import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.tokens.Token

class ExpectedTokenException : ParsingException {
    var expected: Array<String?>
    var current: String

    constructor(expected: String, current: Token) : super(current.lineNumber) {
        this.current = current.toString()
        this.expected = arrayOf(expected)
    }

    constructor(expected: Token, current: Token) : super(current.lineNumber) {
        this.current = current.toString()
        this.expected = arrayOf(expected.toString())
    }

    constructor(current: Token, vararg expectToken: String) : super(current.lineNumber) {
        this.current = current.toString()
        this.expected = arrayOfNulls<String>(expectToken.size);
        expectToken.forEachIndexed { index, token -> expected[index] = token }
    }


    override val message: String? get() {
        return "Syntax error, \"$expected\" expected but \"$current\" found"
    }

    override val isAutoFix: Boolean
        get() = true
}
