/*
 *  Copyright 2017 Tran Le Duy
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

package com.duy.pascal.backend.pascaltypes;

import com.js.interpreter.VariableDeclaration;

public class RecordType extends CustomType {
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\n");
        for (VariableDeclaration variableType : variableDeclarations) {
            out.append(variableType.toString()).append("\n");
        }
        return out.toString();
    }
}
