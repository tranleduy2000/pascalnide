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

package com.duy.pascal.backend.ast.runtime_value.references;

import com.duy.pascal.backend.ast.variablecontext.ContainsVariables;
import com.duy.pascal.backend.runtime_exception.RuntimePascalException;

public class ContainsVariablesPointer<T> implements Reference<T> {
    private final ContainsVariables container;
    private final String index;

    public ContainsVariablesPointer(ContainsVariables container, String index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public T get() throws RuntimePascalException {
        return (T) container.getVar(index);
    }

    @Override
    public void set(T value) {
        container.setVar(index, value);
    }

    @Override
    public ContainsVariablesPointer<T> clone() {
        return new ContainsVariablesPointer<>(container, index);
    }
}
