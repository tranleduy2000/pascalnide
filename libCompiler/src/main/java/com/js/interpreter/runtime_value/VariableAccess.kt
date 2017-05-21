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

package com.js.interpreter.runtime_value

import com.duy.pascal.backend.debugable.DebuggableAssignableValue
import com.duy.pascal.backend.exceptions.ParsingException
import com.duy.pascal.backend.linenumber.LineInfo
import com.duy.pascal.backend.pascaltypes.RuntimeType
import com.duy.pascal.backend.tokens.WordToken
import com.js.interpreter.codeunit.RuntimeExecutableCodeUnit
import com.js.interpreter.expressioncontext.CompileTimeContext
import com.js.interpreter.expressioncontext.ExpressionContext
import com.js.interpreter.instructions.FieldReference
import com.js.interpreter.runtime.FunctionOnStack
import com.js.interpreter.runtime.VariableContext
import com.js.interpreter.runtime.exception.RuntimePascalException
import com.js.interpreter.runtime.references.Reference

class VariableAccess : DebuggableAssignableValue {
    val name: String
    private var line: LineInfo? = null

    constructor(t: WordToken) {
        this.name = t.name
        this.line = t.lineInfo
    }

    constructor(name: String, line: LineInfo) {
        this.name = name
        this.line = line
    }

    override fun getOutputFormat(): Array<RuntimeValue>? {
        return super.getOutputFormat()
    }

    override fun setOutputFormat(formatInfo: Array<RuntimeValue>?) {
        super.setOutputFormat(formatInfo)
    }

    override fun getLineNumber(): LineInfo? {
        return line
    }

    @Throws(RuntimePascalException::class)
    override fun getValueImpl(f: VariableContext, main: RuntimeExecutableCodeUnit<*>): Any? {
        val result = f.getVar(name)
        if (result != null) {
            return result
        } else {
            //get map library
            val unitsMap = main.definition.context.unitsMap
            val entries = unitsMap.entries

            //check all library
            for (entry in entries) {
                val unit = entry.value
                return unit.getVar(name);
            }
        }
        return null
    }

    @Throws(RuntimePascalException::class)
    override fun getReferenceImpl(f: VariableContext, main: RuntimeExecutableCodeUnit<*>): Reference<*> {
        try {
            if (f is FunctionOnStack) {
                val declarations = f.prototype.declarations
                val type = getType(declarations)
                return FieldReference(f, name, type)
            }
        } catch (e: ParsingException) {
            e.printStackTrace()
        }

        return FieldReference(f, name)
    }

    override fun toString(): String {
        return name
    }

    @Throws(ParsingException::class)
    override fun getType(f: ExpressionContext): RuntimeType? {
        return RuntimeType(f.getVariableDefinition(name).type, true)
    }

    @Throws(ParsingException::class)
    override fun compileTimeValue(context: CompileTimeContext): Any? {
        return null
    }


    @Throws(ParsingException::class)
    override fun compileTimeExpressionFold(context: CompileTimeContext): RuntimeValue {
        return this
    }

    companion object {
        private val TAG = "VariableAccess"
    }
}
