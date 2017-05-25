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

package com.js.interpreter.codeunit.library

import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit
import com.duy.pascal.backend.runtime.VariableContext
import com.duy.pascal.backend.runtime.exception.RuntimePascalException

class RuntimeUnitPascal : RuntimeExecutableCodeUnit<UnitPascal> {
    constructor(unitPascal: UnitPascal) : super(unitPascal)

    /**
     * run instruction initialization
     */
    @Throws(RuntimePascalException::class)
    fun runInit() {
        val context = definition.context as UnitPascal.UnitExpressionContext
        context.initInstruction?.execute(this, this)
    }

    /**
     * run final instruction
     */
    @Throws(RuntimePascalException::class)
    fun runFinal() {
        val context = definition.context as UnitPascal.UnitExpressionContext
        context.finalInstruction?.execute(this, this)
    }

    @Throws(RuntimePascalException::class)
    override fun runImpl() {
        //don't working
    }

    override fun getParentContext(): VariableContext? {
        return null
    }

}
