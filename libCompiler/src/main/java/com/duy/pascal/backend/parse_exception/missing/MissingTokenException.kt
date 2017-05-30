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

package com.duy.pascal.backend.parse_exception.missing

import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.parse_exception.ParsingException
import com.duy.pascal.backend.tokens.Token

/**
 * Created by Duy on 25-May-17.
 */

abstract class MissingTokenException : ParsingException {
    var token: Token? = null
    var insertmode = true;

    constructor(line: LineInfo, token: Token) : super(line) {
        this.token = token
        this.lineInfo = line
    }

    constructor(current: Token) : super(current.lineNumber) {
        this.token = current
    }

    override val isAutoFix: Boolean
        get() = true

    abstract fun getMissingToken(): String

    override val message: String? get() {
        return "Missing token $token at $lineInfo"
    }
}
