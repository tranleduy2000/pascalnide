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

package com.duy.pascal.interperter.parse_exception.define

import com.duy.pascal.interperter.ast.expressioncontext.ExpressionContext
import com.duy.pascal.interperter.linenumber.LineInfo
import com.duy.pascal.interperter.parse_exception.ParsingException
import com.duy.pascal.interperter.declaration.lang.types.RecordType

/**
 * Created by Duy on 03-Jun-17.
 */

class UnknownFieldException(line: LineInfo?, var container: RecordType, var name: String,
                            var scope: ExpressionContext)
    : ParsingException(line) {
    override val message: String?
        get() = "Can not find field \"$name\" of \"$container\"";
}
