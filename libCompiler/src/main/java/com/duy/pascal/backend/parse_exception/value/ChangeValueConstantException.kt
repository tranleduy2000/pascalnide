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

package com.duy.pascal.backend.parse_exception.value

import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext
import com.duy.pascal.backend.ast.runtime_value.value.access.ConstantAccess
import com.duy.pascal.backend.parse_exception.ParsingException

/**
 * Created by Duy on 13-Apr-17.
 */

class ChangeValueConstantException(var const: ConstantAccess<Any>, var scope: ExpressionContext)
    : ParsingException(const.lineNumber) {
    var name: String = ""

    override val message: String?
        get() = "can not change value of constant " + const;

    override val isAutoFix: Boolean
        get() = true
}
