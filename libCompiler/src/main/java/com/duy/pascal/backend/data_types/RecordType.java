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

package com.duy.pascal.backend.data_types;

import com.duy.pascal.backend.ast.VariableDeclaration;
import com.duy.pascal.backend.ast.runtime_value.variables.CustomVariable;

public class RecordType extends CustomType implements Cloneable {
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("\n");
        for (VariableDeclaration variableType : variableDeclarations) {
            out.append(variableType.toString()).append("\n");
        }
        return out.toString();
    }

    @Override
    public String getEntityType() {
        return "record type";
    }

    /**
     * @param name - name of field
     * @return index of field in list variable of record type
     */
    public VariableDeclaration findField(String name) {
        for (VariableDeclaration variableDeclaration : variableDeclarations) {
            if (variableDeclaration.getName().equalsIgnoreCase(name)) return variableDeclaration;
        }
        return null;

    }

    @Override
    public CustomVariable initialize() {
        return super.initialize();
    }

    public boolean setFieldValue(String name, Object o) {
        for (VariableDeclaration variableDeclaration : variableDeclarations) {
            if (variableDeclaration.getName().equalsIgnoreCase(name)) {
                variableDeclaration.setInitialValue(o);
                return true;
            }
        }
        return false;
    }

    @Override
    public RecordType clone() {
        RecordType recordType = new RecordType();
        for (VariableDeclaration var : variableDeclarations) {
            recordType.addVariableDeclaration(var.clone());
        }
        recordType.setLineNumber(lineInfo);
        recordType.setName(name);
        return recordType;
    }
}
