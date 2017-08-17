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

package com.duy.pascal.interperter.exceptions.parsing.define

import com.duy.pascal.interperter.declaration.NamedEntity
import com.duy.pascal.interperter.declaration.Name
import com.duy.pascal.interperter.exceptions.parsing.ParsingException

class DuplicateIdentifierException(previous: NamedEntity, current: NamedEntity) :
        ParsingException(current.lineNumber,
                "${current.entityType} ${current.name} conflicts with previously defined " +
                        "${previous.entityType} with the same name defined at ${previous.lineNumber}") {
    var type: String
    var name: Name? = null
    var preType: String
    var preLine: String

    init {
        this.type = current.entityType
        this.name = current.name
        this.preType = previous.entityType
        this.preLine = previous.lineNumber.toString()
    }
}
