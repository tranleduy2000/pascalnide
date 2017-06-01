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

package com.duy.pascal.backend.parse_exception.define


import com.duy.pascal.backend.ast.expressioncontext.ExpressionContext
import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.parse_exception.ParsingException

class BadFunctionCallException(line: LineInfo, var functionName: String,
                               var functionExists: Boolean, numargsMatch: Boolean,
                               args: ArrayList<String>,
                               function: ArrayList<String>,
                               var scope: ExpressionContext) : ParsingException(line) {

    var argsMatch: Boolean = numargsMatch
    var args: List<String>? = args;
    var functions: List<String>? = function;

    override val message: String?
        get() {
            if (functionExists) {
                if (argsMatch) {
                    return "One or more arguments has an incorrect type when calling function \"$functionName\"."
                } else {
                    return "Either too few or two many arguments are being passed to function \"$functionName\"."
                }
            } else {
                return "Can not call function or procedure \"$functionName\", which is not defined."
            }
        }

}
