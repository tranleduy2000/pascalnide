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

package com.duy.pascal.backend.parse_exception.index

import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.parse_exception.ParsingException

/**
 * This exception will be throw if the first > the upper of array
 *
 *
 * Created by Duy on 14-Apr-17.
 */
class LowerGreaterUpperBoundException : ParsingException {
    var low: Int = 0
    var high: Int = 0
    var size: Int = 0

    constructor(low: Int, high: Int, lineInfo: LineInfo, message: String) : super(lineInfo, message) {
        this.low = low
        this.high = high
    }

    constructor(low: Int, high: Int, lineInfo: LineInfo) : super(lineInfo) {
        this.low = low
        this.high = high
    }
}
