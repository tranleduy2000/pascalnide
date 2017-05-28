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

package com.duy.pascal.backend.exceptions.value

import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.linenumber.LineInfo

/**
 * Created by Duy on 13-Apr-17.
 */

class ChangeValueConstantException : ParsingException {
    var name: String = ""
    var value: Any? = null

    constructor(line: LineInfo, message: String, name: String) : super(line, message) {
        this.name = name
    }

    constructor(line: LineInfo, message: String, name: String, value: Any) : super(line, message) {
        this.name = name
        this.value = value
    }

    constructor(line: LineInfo) : super(line) {}
}
