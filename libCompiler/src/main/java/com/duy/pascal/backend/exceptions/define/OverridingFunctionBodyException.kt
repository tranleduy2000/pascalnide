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

package com.duy.pascal.backend.exceptions.define


import com.duy.pascal.backend.function_declaretion.AbstractFunction
import com.duy.pascal.backend.function_declaretion.FunctionDeclaration
import com.duy.pascal.backend.linenumber.LineInfo

class OverridingFunctionBodyException : com.duy.pascal.backend.exceptions.ParsingException {
    var functionDeclaration: AbstractFunction
    var isMethod = false

    constructor(old: FunctionDeclaration, line: LineInfo) : super(line,
            "Redefining function body for $old, which was previous define at ${old.lineNumber}") {
        this.functionDeclaration = old
    }

    constructor(old: AbstractFunction,
                news: FunctionDeclaration) : super(news.lineNumber,
            "Attempting to override plugin definition$old") {
        this.functionDeclaration = old
        isMethod = true

    }
}
