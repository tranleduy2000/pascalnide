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

package com.duy.pascal.backend.runtime.references;

import com.duy.pascal.backend.runtime.exception.RuntimePascalException;

import java.lang.reflect.Array;

public class ArrayIndexReference implements Reference {
    private Object array;
    private int index;
    private int offset;

    public ArrayIndexReference(Object array, int index, int offset) {
        this.array = array;
        this.index = index;
        this.offset = offset;
    }

    public void set(Object value) {
        Array.set(array, index - offset, value);
    }

    public Object get() throws RuntimePascalException {
        return Array.get(array, index - offset);
    }

    @Override
    public ArrayIndexReference clone() {
        return null;
    }
}
