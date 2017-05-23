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

import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.linenumber.LineError
import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.frontend.code_editor.editor_view.autofit.DefineType

class NoSuchFunctionOrVariableException(line: LineInfo, var name: String)
    : ParsingException(LineError(line, name.length), name + " is not a variable or function name") {

    var token: String? = null
    var fitType: DefineType? = DefineType.DECLARE_VAR;
    override fun isAutoFix(): Boolean {
        return true
    }
}
