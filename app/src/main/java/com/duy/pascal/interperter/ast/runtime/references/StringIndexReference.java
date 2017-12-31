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

package com.duy.pascal.interperter.ast.runtime.references;

import com.duy.pascal.interperter.exceptions.runtime.PascalStringIndexOutOfBoundsException;
import com.duy.pascal.interperter.exceptions.runtime.RuntimePascalException;

public class StringIndexReference implements Reference {
    private StringBuilder container;
    private int index;

    public StringIndexReference(StringBuilder container, int index) {
        this.container = container;
        this.index = index;
    }

    @Override
    public void set(Object value) {
        try {
            container.setCharAt(index - 1, (char) value);
        } catch (StringIndexOutOfBoundsException e) {
            throw new PascalStringIndexOutOfBoundsException(index);
        }
    }

    @Override
    public Object get() throws RuntimePascalException {
        try {
            return container.charAt(index - 1);
        }catch (StringIndexOutOfBoundsException e){
            throw new PascalStringIndexOutOfBoundsException(index);
        }
    }

    @Override
    public Reference clone() {
        return null;
    }
}
